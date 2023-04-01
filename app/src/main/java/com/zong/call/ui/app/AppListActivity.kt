package com.zong.call.ui.app

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.zong.call.R
import com.zong.call.adapter.AllAppAdapter
import com.zong.call.constant.Constant

import com.zong.call.utils.*
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

    override fun bindind() = ActivityAppListBinding.inflate(layoutInflater)
    override fun initIntent(intent: Intent) {
        EventBus.getDefault().register(this)
        Thread {
            initRv()
        }.start()


    }

    lateinit var list: MutableList<InstalledApp>
    lateinit var adapter: AllAppAdapter
    private fun initRv() {
        binding.apply {
            initToolbar(layoutTitle.toolbar, "应用管理")
        }
        LogUtils.d(intent.getStringExtra("packname").toString())
        list = InstalledAPPUtils.getAPPList(this)!!
        adapter = AllAppAdapter(list, intent.getStringExtra("packname"))
        binding?.rv?.layoutManager = LinearLayoutManager(this)
//        binding?.rv?.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        binding?.rv?.adapter = adapter
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.sw -> {
                    app = list[position]
                    StatisticsUtil.onEvent(this, "close_switch", mutableMapOf("close_switch" to "close_switch"))
                    list[position].isSelect = !list[position].isSelect
                    EventBus.getDefault().post(app)
                    MMKVUtil.putString(Constant.INSTALL_APP, GsonUtils.toJson(list))

                }
            }
        }

        binding.sideBar.setOnSelectIndexItemListener { index ->
            LogUtils.d("index${index}")
            for (i in list.indices) {
                if (list.get(i).index.equals(index)) {
                    (binding.rv.getLayoutManager() as LinearLayoutManager).scrollToPositionWithOffset(i, 0)
                    return@setOnSelectIndexItemListener
                }
            }
        }

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