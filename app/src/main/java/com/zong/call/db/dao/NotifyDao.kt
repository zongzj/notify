package com.zong.call.db.dao

import androidx.room.*
import com.zong.call.db.entity.NotifyBean

@Dao
interface NotifyDao {

    @Query("SELECT * FROM NotifyBean")
    fun getAll(): List<NotifyBean>

    @Query("SELECT * FROM NotifyBean WHERE currentDate IN (:currentDate)")
    fun loadNotifyBeanDate(currentDate: String): List<NotifyBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg notifyBean: NotifyBean)

    @Update
    fun update(vararg notifyBean: NotifyBean)

    @Delete
    fun delete(vararg notifyBean: NotifyBean)
}