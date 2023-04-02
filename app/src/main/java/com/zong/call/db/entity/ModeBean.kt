package com.zong.call.db.entity

import android.os.Parcelable
import androidx.room.*
import com.zong.call.bean.InstalledApp
import com.zong.call.utils.DateUtil
import com.zong.call.utils.StringUtils
import com.zong.common.ext.GSON
import com.zong.common.ext.fromJsonArray
import com.zong.common.ext.fromJsonObject
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
@TypeConverters(ModeBean.Converters::class)
@Entity(indices = [(Index(value = ["id"], unique = true))])
class ModeBean : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var modeName = ""
    var isSelect: Boolean = false//
    var startTime: String = "00:01"
    var endTime: String = "23:59"
    var format = "HH:mm"
    var seletedTime=SelectedTime()
    var appList: MutableList<InstalledApp>? = mutableListOf()
    fun getWeekText(isSelect: BooleanArray = seletedTime.selectDate): String {
        var sb = StringBuffer()
        isSelect.forEachIndexed { index, b ->
            if (b) {
                sb.append(seletedTime.dayOfWeekItems[index])
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
    @Parcelize
    class SelectedTime : Parcelable {
        var selectDate:BooleanArray= booleanArrayOf(true, true, true, true, true, true, true)
        var dayOfWeekItems: Array<String> = arrayOf("周一", "周二", "周三", "周四", "周五", "周六", "周日")
    }

    class Converters{

        @TypeConverter
        fun appListToString(list:List<InstalledApp>) = GSON.toJson(list)

        @TypeConverter
        fun stringToAppList(json: String?) = GSON.fromJsonArray<InstalledApp>(json).getOrNull()

        @TypeConverter
        fun timeToString(time: SelectedTime) = GSON.toJson(time)

        @TypeConverter
        fun stringToTime(json: String?) = GSON.fromJsonObject<SelectedTime>(json).getOrNull()
    }
}
