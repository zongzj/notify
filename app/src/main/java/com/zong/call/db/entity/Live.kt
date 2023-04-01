package com.zong.call.db.entity

import androidx.room.*

@Entity(indices = [(Index(value = ["id"], unique = true))])
data class Live(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    //最大观众数
    @ColumnInfo(defaultValue = "0")
    val maxAudiencesNum: Int = 0,
    //心心数量
    val likesNum: Int = 0,
    //直播开始时间
    val startTime: Long = System.currentTimeMillis(),
    //直播结束时间
    val endTime: Long = System.currentTimeMillis(),
    //主播昵称
    val nickname: String = "",
    //标题
    val title: String = "",
    //简介
    val summary: String = "",
    //直播平台
    val livePlatform: String = "",
    //当天日期
    val date: String = "",
) {

    @Ignore
    var longTime = endTime - startTime
}
