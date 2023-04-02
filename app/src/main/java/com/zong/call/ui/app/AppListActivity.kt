package com.zong.call.ui.app

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.zong.call.R
import com.zong.call.adapter.AllAppAdapter
import com.zong.call.bean.InstalledApp
import com.zong.call.constant.Constant
import com.zong.call.databinding.ActivityAppListBinding
import com.zong.call.event.MessageWrap
import com.zong.call.ext.initToolbar
import com.zong.call.ext.initToolbarClose

import com.zong.call.utils.*
import com.zong.common.base.activity.BaseActivity
import com.zong.common.ext.runOnUI
import com.zong.common.utils.LogUtils
import com.zong.common.utils.MMKVUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 *  author: zzj
 *  time: 8/21/21
 *  deprecated :  选择app
 */

class AppListActivity : BaseActivity<ActivityAppListBinding>() {
    lateinit var app: InstalledApp


    override fun initIntent(intent: Intent) {
        EventBus.getDefault().register(this)
        initRv()
    }

    var list = mutableListOf<InstalledApp>()
    lateinit var adapter: AllAppAdapter
    private fun initRv() {
        binding.includeToolbar.toolbar.run {
            initToolbarClose("添加应用") {
                finish()
            }
        }
        var packname = intent.getStringExtra("packname")
        LogUtils.d(TAG, intent.getStringExtra("packname").toString())
        InstalledAPPUtils.getAPPList(this) {
            it.forEach { app ->
                if (packname!!.contains(app.packageName)) {
                    app.isSelect = true
                }
                list.add(app)
            }
            runOnUI {
                adapter.notifyDataSetChanged()
            }
        }
        adapter = AllAppAdapter(list)
        binding?.rv?.layoutManager = LinearLayoutManager(this)
//        binding?.rv?.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        binding?.rv?.adapter = adapter
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.sw -> {
                    app = list[position]
                    list[position].isSelect = !list[position].isSelect
                    EventBus.getDefault().post(app)
                    MMKVUtil.putString(Constant.INSTALL_APP, GsonUtils.toJson(list))

                }
            }
        }

        binding.sideBar.setOnSelectIndexItemListener { index ->
            for (i in list.indices) {
                if (list[i].index == index) {
                    (binding.rv.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(i, 0)
                    return@setOnSelectIndexItemListener
                }
            }
        }
        adapter.setEmptyView(layoutInflater.inflate(R.layout.empty_loading, null))

        binding.tvTips.visibility = View.GONE

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun unLockAPP(b: MessageWrap) {
        app.isUnLock = true
//            var indexOf = list.indexOf(app)
        app.isSelect = true
        var count = MMKVUtil.getInt(Constant.UNLOCK_NUM)
        MMKVUtil.putInt(Constant.UNLOCK_NUM, count + 1);
        if (MMKVUtil.getInt(Constant.UNLOCK_NUM) >= 3) {
            list.forEach {
                it.isUnLock = true
            }
        }
        adapter.notifyDataSetChanged()
        MMKVUtil.putString(Constant.INSTALL_APP, GsonUtils.toJson(list))

        EventBus.getDefault().post(app)


    }

    override fun initUi() {

    }

    override fun uiInteraction() {

    }

    override fun observerOnUi() {

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}