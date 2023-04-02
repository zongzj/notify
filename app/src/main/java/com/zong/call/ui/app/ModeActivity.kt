package com.zong.call.ui.app

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import com.blankj.utilcode.util.KeyboardUtils
import com.github.gzuliyujiang.wheelpicker.annotation.TimeMode
import com.github.gzuliyujiang.wheelpicker.entity.TimeEntity
import com.github.gzuliyujiang.wheelpicker.widget.TimeWheelLayout
import com.zong.call.R
import com.zong.call.adapter.ReportAdapter
import com.zong.call.bean.InstalledApp
import com.zong.call.db.entity.ModeBean
import com.zong.call.databinding.ActivityModeBinding
import com.zong.call.db.appDb
import com.zong.call.ext.initToolbarClose
import com.zong.call.view.FlowLayoutManager
import com.zong.call.view.SpaceItemDecoration
import com.zong.common.base.activity.BaseActivity
import com.zong.common.ext.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *  author: zzj
 *  time: 2021/12/14
 *  deprecated : 添加修改模式
 */
class ModeActivity : BaseActivity<ActivityModeBinding>() {
    var mAppList = mutableListOf<InstalledApp>()

    var packNameString = "";
    var mAdapter: ReportAdapter? = null
    lateinit var mModeBean: ModeBean

    override fun initIntent(intent: Intent) {
        binding.includeToolbar.toolbar.run {
            initToolbarClose("添加播报模式") {
                finish()
            }
        }
        intent.let {
            var id = intent.getLongExtra("id", -1)
            if (id > 0) {
                mModeBean = appDb.modeDao.loadModeById(id)
                binding.modeName.setText(mModeBean.modeName)
                mAppList = mModeBean.appList!!
                mAdapter?.setNewInstance(mAppList)
            } else {
                mModeBean = ModeBean()
                mModeBean.id = System.currentTimeMillis()
                mAppList = mutableListOf()
            }
            selectDate = mModeBean.seletedTime.selectDate
            binding.tvDate.text = "重复: " + mModeBean.getWeekText(selectDate)
            setDefaultTime()
        }


    }

    override fun initUi() {
        EventBus.getDefault().register(this)

        initRV()

        binding.tvDate.setOnClickListener {
            showMultiSelect();
        }
        binding.tvTime.setOnClickListener {
            showSelectTimeDialog()
        }
        binding.add.setOnClickListener {
            //每次都重新遍历，已经选中的APP包名
            packNameString = ""
            mAppList.forEach {
                packNameString += it.packageName + ","
            }
            var bundle = bundleOf("packname" to packNameString)
            openActivity(AppListActivity::class, bundle)
        }
        binding.save.setOnClickListener {
            if (binding.modeName.text.toString().isNullOrBlank()) {
                toastOnUi("请输入播报模式名称")
                return@setOnClickListener
            }
            if (mAppList.size < 1) {
                toastOnUi("请选择添加软件")
                return@setOnClickListener
            }
            mModeBean.modeName = binding.modeName.text.toString()
            mModeBean.appList = mAppList
            EventBus.getDefault().post(mModeBean)
            finish()
        }

        KeyboardUtils.hideSoftInput(this)


    }


    private fun initRV() {
        mAdapter = ReportAdapter(mAppList)
        binding.rv.layoutManager = FlowLayoutManager()
        binding.rv.addItemDecoration(SpaceItemDecoration(10))
        binding.rv.adapter = mAdapter
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.delete) {
                mAppList.removeAt(position)
                mAdapter?.notifyDataSetChanged()
            }
        }
    }


    override fun uiInteraction() {

    }

    override fun observerOnUi() {
    }

    lateinit var selectDate: BooleanArray


    private fun showSelectTimeDialog() {
        var dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_select_time, null)
        var startTime = dialogView.findViewById<TimeWheelLayout>(R.id.startTime);
        var endTime = dialogView.findViewById<TimeWheelLayout>(R.id.endTime)


        AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("播报时段")
            .setPositiveButton("确定") { dialogInterface, i ->
                mModeBean.startTime = getTime(startTime)
                mModeBean.endTime = getTime(endTime)
                setDefaultTime()
            }
            .setNegativeButton("取消") { dialogInterface, i ->
            }
            .create().show()
        arrayOf(startTime, endTime).forEach {
            it?.setTimeMode(TimeMode.HOUR_24_NO_SECOND)
            it.setTimeLabel(":", " ", "")
            when (it.id) {
                R.id.startTime -> {
                    it?.setDefaultValue(TimeEntity.target(1, 0, 0))
                }
                R.id.endTime -> {
                    it?.setDefaultValue(TimeEntity.target(23, 59, 0))

                }
            }
        }
    }

    //更新选中app
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeSelect(app: InstalledApp) {
        if (isContains(app.packageName)) {
            mAppList?.removeAt(appIndex(app.packageName))
        } else {
            mAppList?.add(0, app)
        }
        mAdapter?.notifyDataSetChanged()


    }


    override fun onBackPressedSupport() {
        super.onBackPressedSupport()

    }


    private fun isContains(packName: String): Boolean {
        var isContain = false
        mAppList.forEach {
            if (packName == it.packageName) {
                isContain = true
            }
        }
        return isContain
    }

    private fun appIndex(packName: String): Int {
        var i = 0
        mAppList.forEachIndexed { index, installedApp ->
            if (packName == installedApp.packageName) {
                i = index
            }
        }
        return i
    }


    private fun showMultiSelect() {
        val items = arrayOf("周一", "周二", "周三", "周四", "周五", "周六", "周日")

        //默认都未选中
        var builder = AlertDialog.Builder(this)
            .setTitle("播报日期")
            .setMultiChoiceItems(items, selectDate) { _, witch, isChecked ->
                selectDate[witch] = isChecked
                binding.tvDate.text = "重复: " + mModeBean.getWeekText(selectDate)

            }.setPositiveButton("确定") { dialogInterface, i ->
                mModeBean.seletedTime.selectDate = selectDate//保存限制日期
//                LogUtils.d(GsonUtils.toJson(selectDate))
            }.setNegativeButton("取消") { dialogInterface, i ->

            }
        builder.create().show()

    }

    private fun setDefaultTime() {
        binding.tvTime.text = "${mModeBean.startTime}-${mModeBean.endTime}"
    }

    private fun getTime(startTime: TimeWheelLayout) =
        "${startTime.selectedHour.Wrap}:${startTime.selectedMinute.Wrap}"

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)

    }
}