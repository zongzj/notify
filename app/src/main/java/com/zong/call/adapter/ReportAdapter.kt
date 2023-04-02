package com.zong.call.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zong.call.R
import com.zong.call.bean.InstalledApp

class ReportAdapter(data: MutableList<InstalledApp>) :
    BaseQuickAdapter<InstalledApp, BaseViewHolder>(R.layout.item_app_report, data) {
    init {
        addChildClickViewIds(R.id.delete)

    }
    override fun convert(helper: BaseViewHolder, item: InstalledApp) {
        helper.setText(R.id.name, item?.appName)
        var icon = helper.getView<ImageView>(R.id.logo)
        Glide.with(context).load(item?.icon).into(icon)
    }

}