package com.zong.call.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zong.call.db.dao.ModeDao
import com.zong.call.db.dao.NotifyDao
import com.zong.call.db.entity.ModeBean
import com.zong.call.db.entity.NotifyBean
import splitties.init.appCtx
import java.util.*

val appDb by lazy {
    AppDatabase.createDatabase(appCtx)
}

@Database(version = 1, exportSchema = true, entities = [NotifyBean::class, ModeBean::class], autoMigrations = [])
abstract class AppDatabase : RoomDatabase() {

    abstract val notifyDao: NotifyDao
    abstract val modeDao: ModeDao

    companion object {
        private const val DATABASE_NAME = "notify.db"
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