package com.zong.call.db.dao

import androidx.room.*
import com.zong.call.db.entity.Live

@Dao
interface LiveDao {

    @Query("SELECT * FROM Live")
    fun getAll(): List<Live>

    @Query("SELECT * FROM Live WHERE id IN (:id)")
    fun loadLiveById(id: Long): Live

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg live: Live)

    @Update
    fun update(vararg live: Live)

    @Delete
    fun delete(vararg live: Live)
}