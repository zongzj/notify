package com.zong.call.db.dao

import androidx.room.*
import com.zong.call.db.entity.FilterWord
import com.zong.call.db.entity.Live

@Dao
interface FilterDao {

    @Query("SELECT * FROM FilterWord")
    fun getAll(): List<FilterWord>

    @Query("SELECT * FROM FilterWord WHERE id IN (:nickname)")
    fun loadByNickname(nickname: String): FilterWord

    @Insert
    fun insert(vararg filterWord: FilterWord)

    @Update
    fun update(vararg filterWord: FilterWord)

    @Delete
    fun delete(vararg filterWord: FilterWord)
}