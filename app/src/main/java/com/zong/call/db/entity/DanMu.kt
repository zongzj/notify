package com.zong.call.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
//弹幕表
@Entity(indices = [(Index(value = ["liveId"], unique = false))])
data class DanMu(
    @PrimaryKey(autoGenerate = true)
    val liveId: Long = 0,
    val sendTime: Long = System.currentTimeMillis(),

    //粉丝团xxx
    val fansClub: String = "",
    //发言人昵称
    val nickname: String = "",
    //发言内容
    val content: String = "",
    //粉丝等级
    @ColumnInfo(defaultValue = "0")
    val level: Int = 0,
    val hasRead:Boolean=false
){

}
