package com.zong.call.db.dao

import androidx.room.*
import com.zong.call.db.entity.DanMu

@Dao
interface DanMuDao {

    @Query("SELECT * FROM DanMu")
    fun getAll(): List<DanMu>

    @Query("SELECT * FROM DanMu WHERE liveId IN (:liveId)")
    fun loadDanMuByLiveId(liveId: Long): List<DanMu>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg danMu: DanMu)

    @Update
    fun update(vararg danMu: DanMu)

    @Delete
    fun delete(vararg danMu: DanMu)
}