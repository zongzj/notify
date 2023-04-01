package com.zong.call.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zong.call.R
import com.zong.call.bean.InstalledApp

class AppLogoAdapter(data: MutableList<InstalledApp>) :
    BaseQuickAdapter<InstalledApp, BaseViewHolder>(R.layout.item_app_icon, data) {
    override fun convert(holder: BaseViewHolder, item: InstalledApp) {
        var logo = holder.getView<ImageView>(R.id.logo)
        Glide.with(context).load(item?.icon).into(logo)
    }
}
