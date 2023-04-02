package com.zong.call.ui.app

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ServiceUtils.startService
import com.blankj.utilcode.util.ToastUtils
import com.zong.call.R
import com.zong.call.adapter.ModeAdapter
import com.zong.call.db.entity.ModeBean
import com.zong.call.constant.Constant
import com.zong.call.constant.IntentAction
import com.zong.call.databinding.FragmentAppBinding
import com.zong.call.db.appDb
import com.zong.call.db.entity.NotifyBean
import com.zong.call.ext.initToolbar
import com.zong.call.service.ForegroundService
import com.zong.call.utils.InstalledAPPUtils
import com.zong.common.base.fragment.BaseBindingFragment
import com.zong.common.ext.*
import com.zong.common.utils.MMKVUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *  author: zzj
 *  time: 2021/12/18
 *  deprecated :
 */

class AppFragment : BaseBindingFragment<FragmentAppBinding>() {
    lateinit var adapter: ModeAdapter
     var mModeList= mutableListOf<ModeBean>()


    override fun viewCreated(binding: FragmentAppBinding) {
        EventBus.getDefault().register(this)

        binding.includeToolbar.toolbar.run {
            initToolbar("首页")
            inflateMenu(R.menu.app_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.app_history -> {
                        startActivity<ReportHistoryActivity> { }
                    }
                }
                true
            }
        }
        initRV()
        binding.floating.setClick {
            startActivity(Intent(activity, ModeActivity::class.java))
        }
    }

    private fun initRV() {
        adapter = ModeAdapter(mModeList)
        binding?.rv?.layoutManager = LinearLayoutManager(activity)
        binding?.rv?.adapter = adapter
        runOnIO {
            mModeList = appDb.modeDao.getAll() as MutableList<ModeBean>
            if (mModeList.size == 0) {
                var bean = ModeBean()
                bean.isSelect = true
                bean.id = System.currentTimeMillis()
                bean.modeName = "工作模式"
                mModeList.add(bean)
                appDb.modeDao.insert(bean)

            }
            adapter.setNewInstance(mModeList)

        }

        //提前加载本地安装的app数据

        adapter.setOnItemClickListener { adapter, view, position ->
            requireActivity().openActivity(ModeActivity::class, bundleOf("id" to mModeList[position].id))
        }

        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.sw -> {
                    mModeList.forEachIndexed { index, modeBean ->
                        var bean = mModeList[position]
                        if (index == position) {
                            bean.isSelect = !bean.isSelect
                            //这个可以播报
                            MMKVUtil.putString(Constant.MODE_NAME, bean.modeName)
                            InstalledAPPUtils.reportModeBean = bean
                        } else {
                            MMKVUtil.putString(Constant.MODE_NAME, "关闭")
                            modeBean.isSelect = false
                        }
                        appDb.modeDao.insert(bean)
                    }

                    requireActivity().startService<ForegroundService>() {
                        action = IntentAction.updateNotify
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }
        adapter.setOnItemLongClickListener { adapter, view, position ->

            AlertDialog.Builder(requireActivity()).apply {
                //构建一个对话框
                //apply标准函数自动返回调用对象本身
                setTitle("移除")//表示
                setMessage("该应用将会从列表中移除")//内容
                setCancelable(true)//是否使用Back关闭对话框
                setPositiveButton("移除") {//确认按钮点击事件
                        dialog, which ->

                    if (mModeList.size == 1) {
                        ToastUtils.showShort("至少保留一种播报模式")
                        return@setPositiveButton
                    }
                    if (mModeList[position].isSelect) {
                        ToastUtils.showShort("请先关闭选中按钮")
                        return@setPositiveButton
                    }
                    mModeList.removeAt(position)
                    adapter.notifyDataSetChanged()
                    appDb.modeDao.delete(mModeList[position])

                }
                    .setNegativeButton("取消") { dialog, which ->
                    }
                show()
            }
            true
        }
        adapter.setEmptyView(layoutInflater.inflate(R.layout.layout_empty_log, null))
    }


    //更新选中app
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeSelect(mode: ModeBean) {
        if (appDb.modeDao.loadModeById(mode.id) != null) {
            mModeList?.forEach {
                if (it.id == mode.id) {
                    it.appList = mode.appList
//                    it.id = mode.id
                    it.startTime = mode.startTime
                    it.endTime = mode.endTime
                    it.seletedTime = mode.seletedTime
                    it.isSelect = mode.isSelect
                    it.modeName = mode.modeName
                }
            }
            appDb.modeDao.update(mode)
        } else {
            appDb.modeDao.insert(mode)
            mModeList?.add(0, mode)
        }
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

}