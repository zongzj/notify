package com.zong.call.ui.app

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.zong.call.R
import com.zong.call.adapter.ModeAdapter
import com.zong.call.bean.ModeBean
import com.zong.call.service.notify.NotificationUtil
import com.zong.call.utils.InstalledAPPUtils
import com.zong.common.base.fragment.BaseBindingFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *  author: zzj
 *  time: 2021/12/18
 *  deprecated :
 */

class AppFragment : BaseBindingFragment<FragmentAppBinding>() {
    private val TAG = "AppFragment"
    lateinit var mModeList: MutableList<ModeBean>
    lateinit var adapter: ModeAdapter

    override fun viewCreated(binding: FragmentAppBinding) {
        EventBus.getDefault().register(this)
        initRV()
        binding.floating.setClick {
            startActivity(Intent(activity, ModeActivity::class.java))
        }
    }

    private fun initRV() {
        mModeList = activity?.let { InstalledAPPUtils.showModeList(it) }!!
        activity?.let { InstalledAPPUtils.getAPPList(it) }!!

        if (mModeList.size == 0) {
            var bean = ModeBean()
            bean.isSelect = true
            bean.id = System.currentTimeMillis()
            bean.modeName = "默认"
            mModeList.add(bean)
            MMKVUtil.putString(Constant.MODE, GsonUtils.toJson(mModeList))
        }
        //提前加载本地安装的app数据
        adapter = ModeAdapter(mModeList)
        binding?.rv?.layoutManager = LinearLayoutManager(activity)
//        binding?.rv?.addItemDecoration(DividerItemDecoration(activity, LinearLayout.VERTICAL))
        binding?.rv?.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            var modeBean = mModeList.get(position)
            requireActivity().openActivity(ModeActivity::class, bundleOf("modeBean" to modeBean))

//            val intent: Intent = Intent(activity, ToolsActivity::class.java)
//            startActivity(intent)
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
                    }

                    NotificationUtil.updateNotify()
                    MMKVUtil.putString(Constant.MODE, GsonUtils.toJson(mModeList))

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
                    MMKVUtil.putString(Constant.MODE, GsonUtils.toJson(mModeList))

                }
                    .setNegativeButton("取消") { dialog, which ->
                    }
                show()
            }
            true
        }

        adapter.emptyView = layoutInflater.inflate(R.layout.layout_empty_log, null)


    }


    //更新选中app
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeSelect(mode: ModeBean) {

        if (isContains(mode.id)) {
            mModeList?.forEach {
                if (it.id == mode.id) {
                    it.appList = mode.appList
//                    it.id = mode.id
                    it.startTime = mode.startTime
                    it.endTime = mode.endTime
                    it.selectDate = mode.selectDate
                    it.isSelect = mode.isSelect
                    it.modeName = mode.modeName
                }
            }
        } else {
            mModeList?.add(0, mode)
        }
        adapter.notifyDataSetChanged()
        MMKVUtil.putString(Constant.MODE, GsonUtils.toJson(mModeList))
    }

    private fun isContains(id: Long): Boolean {
        var isContain = false
        mModeList.forEach {
            if (id == it.id) {
                isContain = true
            }
        }
        return isContain
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

}