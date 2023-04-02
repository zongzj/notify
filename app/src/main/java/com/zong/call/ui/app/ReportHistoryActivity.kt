package com.zong.call.ui.app;

import android.content.Intent
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zong.call.adapter.NotifyAdapter
import com.zong.call.databinding.ActivityReportHistoryBinding
import com.zong.call.db.appDb
import com.zong.call.db.entity.NotifyBean
import com.zong.call.ext.emptyView
import com.zong.call.ext.initToolbarClose
import com.zong.call.utils.TTS
import com.zong.common.base.activity.BaseActivity
import com.zong.common.ext.runOnIO
import com.zong.common.ext.runOnUI

class ReportHistoryActivity : BaseActivity<ActivityReportHistoryBinding>() {


    var notifyList = arrayListOf<NotifyBean>()
    override fun initIntent(intent: Intent) {
    }

    override fun initUi() {
        binding.includeToolbar.toolbar.run {
            initToolbarClose("播报历史") {
                finish()
            }
        }

        var adapter = NotifyAdapter(notifyList)
        binding?.rv?.layoutManager = LinearLayoutManager(this)
        binding?.rv?.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        binding?.rv?.adapter = adapter
        adapter.setEmptyView(emptyView("无记录"))
        adapter.setOnItemClickListener { adapter, view, position ->
            var notifyBean = notifyList[position]
            if (notifyBean.appName != null) {
                TTS.instance.speak(notifyBean.appName + notifyBean.notifyContext)
            } else {
                TTS.instance.speak(notifyBean.notifyContext)

            }
//            StatisticsUtil.notifyHistoryClick(this)
        }

        runOnIO {
            var all = appDb.notifyDao.getAll()
            runOnUI {
                notifyList.addAll(all)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun uiInteraction() {

    }

    override fun observerOnUi() {

    }

    override fun onDestroy() {
        super.onDestroy()
        TTS.instance.stop()
    }

}

