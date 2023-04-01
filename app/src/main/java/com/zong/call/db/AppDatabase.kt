package com.zong.call.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zong.call.db.dao.DanMuDao
import com.zong.call.db.dao.FilterDao
import com.zong.call.db.dao.LiveDao
import com.zong.call.db.entity.DanMu
import com.zong.call.db.entity.FilterWord
import com.zong.call.db.entity.Live
import splitties.init.appCtx
import java.util.*

val appDb by lazy {
    AppDatabase.createDatabase(appCtx)
}

@Database(
    version = 1,
    exportSchema = true,
    entities = [Live::class,DanMu::class,FilterWord::class],
    autoMigrations = [
    ]
//            AutoMigration(from = 58, to = 59)
)
abstract class AppDatabase : RoomDatabase() {

    abstract val liveDao: LiveDao
    abstract val danMuDao: DanMuDao
    abstract val filterWord: FilterDao


    companion object {

        private const val DATABASE_NAME = "rs.db"

        fun createDatabase(context: Context) = Room
            .databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigrationFrom(1, 2, 3, 4, 5, 6, 7, 8, 9)
            .addMigrations(*DatabaseMigrations.migrations)
            .allowMainThreadQueries()
            .addCallback(dbCallback)
            .build()

        private val dbCallback = object : Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                db.setLocale(Locale.CHINESE)
            }

            override fun onOpen(db: SupportSQLiteDatabase) {}
        }

    }

}