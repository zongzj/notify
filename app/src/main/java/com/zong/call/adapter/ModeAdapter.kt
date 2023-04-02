package com.zong.call.adapter

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.CompoundButton
import android.widget.Switch
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zong.call.R
import com.zong.call.db.entity.ModeBean
import com.zong.common.ext.imageRipple

class ModeAdapter(data: MutableList<ModeBean>) :
    BaseQuickAdapter<ModeBean, BaseViewHolder>(R.layout.item_mode_list, data) {
    init {
        addChildClickViewIds(R.id.sw,R.id.rv)
    }
    override fun convert(helper: BaseViewHolder, item: ModeBean) {
        helper.setText(R.id.mode_name, "播报模式 : " + item?.modeName)
//        helper.setText(R.id.tv_date, "时间:" + item?.getWeekText())
        var reportTime = "播报时段 : " + item?.startTime + "-" + item?.endTime

        helper.setText(R.id.tv_date, spanText(reportTime))
        var appList = mutableListOf<String>()
        item?.appList?.forEach {
            appList.add(it.appName)
        }

        helper.getView<Switch>(R.id.sw).apply {
            setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (!buttonView.isPressed) {
                    return@OnCheckedChangeListener
                }
            })
            isChecked = item?.isSelect!!
        }
        helper.getView<RecyclerView>(R.id.rv).apply {
            var linearLayoutManager = LinearLayoutManager(context);
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL;
            layoutManager = linearLayoutManager
            adapter = item?.appList?.let { AppLogoAdapter(it) }
            // holder.itemView.performClick() 是设置的和外部rv的点击事件一致
//            setOnTouchListener { view, motionEvent ->
//                if (motionEvent.action == MotionEvent.ACTION_UP) {
//                    //响应父rv的item的点击事件
//                }
//                false
//            }
            setOnClickListener {
                helper.itemView.performClick()

            }
        }
        helper.itemView.imageRipple()

    }

    private fun spanText(reportTime: String): SpannableStringBuilder {
        val builder = SpannableStringBuilder(reportTime)
        builder.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            4,  // setSpan时需要指定的 flag,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括).
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return builder
    }


}