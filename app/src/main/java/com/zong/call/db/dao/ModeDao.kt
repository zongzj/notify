package com.zong.call.db.dao

import androidx.room.*
import com.blankj.utilcode.util.ToastUtils.MODE
import com.zong.call.db.entity.ModeBean

@Dao
interface ModeDao {

    @Query("SELECT * FROM ModeBean")
    fun getAll(): List<ModeBean>

    @Query("SELECT * FROM ModeBean WHERE appList LIKE '%' || :packageName || '%' and isSelect = 1")
    fun getAllByFilter(packageName: String): ModeBean


    @Query("SELECT * FROM ModeBean WHERE id IN (:id)")
    fun loadModeById(id: Long): ModeBean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg mode: ModeBean)

    @Update
    fun update(vararg mode: ModeBean)

    @Delete
    fun delete(vararg mode: ModeBean)
}