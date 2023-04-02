package com.zong.call.adapter

import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zong.call.R
import com.zong.call.bean.InstalledApp
import com.zong.call.constant.Constant
import com.zong.common.utils.MMKVUtil

class AllAppAdapter(data: MutableList<InstalledApp>) :
    BaseQuickAdapter<InstalledApp, BaseViewHolder>(R.layout.item_app_list, data) {
    init {
        addChildClickViewIds(R.id.sw)
    }

    override fun convert(helper: BaseViewHolder, item: InstalledApp) {
        helper.setText(R.id.tv_name, item?.appName)
        var icon = helper.getView<ImageView>(R.id.iv_icon)
        Glide.with(context).load(item?.icon).into(icon)
//        icon.loadImage(item?.icon)
        var position = helper.layoutPosition
        if (position == 0 || data[position - 1].index != item?.index) {
            helper.getView<TextView>(R.id.index).visibility = View.VISIBLE
            helper.setText(R.id.index, item?.index)
        } else {
            helper.getView<TextView>(R.id.index).visibility = View.GONE
        }

        helper.getView<Switch>(R.id.sw).apply {
            isChecked = item?.isSelect == true
        }

        if (MMKVUtil.getInt(Constant.UNLOCK_NUM) >= 3) {
            item!!.isUnLock = true
            helper.getView<ImageView>(R.id.iv_lock).apply {
                visibility = View.GONE
            }
        } else {
            if (item!!.isUnLock) {
                helper.getView<ImageView>(R.id.iv_lock).apply {
                    visibility = View.GONE
                }
            } else {
                helper.getView<ImageView>(R.id.iv_lock).apply {
                    visibility = View.VISIBLE
                }
            }
        }

        helper.getView<ImageView>(R.id.iv_lock).apply {
            visibility = View.GONE
        }

    }


}