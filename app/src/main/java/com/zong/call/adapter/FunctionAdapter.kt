package com.zong.call.adapter

import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.DraggableModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zong.common.view.UIImageView
import com.zong.common.view.UITextView
import com.zong.call.R
import com.zong.call.bean.FunctionBean


class FunctionAdapter(data: ArrayList<FunctionBean>) :
    BaseQuickAdapter<FunctionBean, BaseViewHolder>(R.layout.item_function, data), DraggableModule {
    init {
//        addChildClickViewIds(R.id.iv_delete, R.id.tv_time_period)
    }

    override fun convert(helper: BaseViewHolder, item: FunctionBean) {
        var textView = helper.getView<TextView>(R.id.tv_name)
        var iv_icon = helper.getView<UIImageView>(R.id.iv_icon)
//        textView.setLine(R.color.lineColor3, 0.5.dp)
        textView.text = item.name
        Glide.with(context).load(item.icon).into(iv_icon)
    }


}