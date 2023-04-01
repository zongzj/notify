package com.zong.call.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//过滤，根据昵称或者内容
@Entity
data class FilterWord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val insertTime: Long = System.currentTimeMillis(),
    //发言人昵称
    val nickname: String = "",
    //发言内容
    val content: String = "",
)
