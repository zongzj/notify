package com.zong.call.adapter

import android.widget.ImageView
import com.blankj.utilcode.util.AppUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zong.call.R
import com.zong.call.db.entity.NotifyBean
import com.zong.call.utils.DateUtil


class NotifyAdapter(data: ArrayList<NotifyBean>) :
    BaseQuickAdapter<NotifyBean, BaseViewHolder>(R.layout.item_report_history, data) {
    override fun convert(helper: BaseViewHolder, item: NotifyBean) {
        helper.setText(R.id.tv_name, AppUtils.getAppName(item?.appName))
        helper.setText(R.id.tv_time, item?.let { DateUtil.stampToDateTime(it.time) })
        helper.setText(R.id.tv_context, item?.notifyContext)
        var iv_logo = helper.getView<ImageView>(R.id.iv_logo);
        Glide.with(context).load(item?.appLogo).into(iv_logo)
    }


}