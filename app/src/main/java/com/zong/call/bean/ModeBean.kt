package com.zong.call.bean

import com.zong.call.utils.DateUtil
import com.zong.call.utils.StringUtils
import java.text.SimpleDateFormat
import java.util.*

class ModeBean  {
    var modeName = ""
    var id = 0L
    var isSelect: Boolean = false//
    var startTime: String = "00:01"
    var endTime: String = "23:59"
    var selectDate: BooleanArray = booleanArrayOf(true, true, true, true, true, true, true)
    val dayOfWeekItems = arrayOf("周一", "周二", "周三", "周四", "周五", "周六", "周日")
    val format = "HH:mm"

    var appList: MutableList<InstalledApp>? = mutableListOf()
    fun getWeekText(isSelect: BooleanArray = selectDate): String {
        var sb = StringBuffer()
        isSelect.forEachIndexed { index, b ->
            if (b) {
                sb.append(dayOfWeekItems[index])
                sb.append("、")
            }
        }
        return sb.toString()
    }


    /**
     * 1.判断日期
     * 2.不在可用时段内
     */
    fun isReportTime(): Boolean {

        if (!getWeekText().contains(StringUtils.getWeek(System.currentTimeMillis()))) {
            return false
        }
        val nowTime: Date = SimpleDateFormat(format).parse(DateUtil.getCurrentHourAndMinute())
        val startTime: Date = SimpleDateFormat(format).parse(startTime)
        val endTime: Date = SimpleDateFormat(format).parse(endTime)
        if (nowTime.time === startTime.time || nowTime.time === endTime.time) {
            return true
        }
        val date = Calendar.getInstance()
        date.time = nowTime
        val begin = Calendar.getInstance()
        begin.time = startTime
        val end = Calendar.getInstance()
        end.time = endTime
        return date.time.before(end.time) && date.time.after(begin.time)
    }

}