package com.zong.call.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {

    val migrations: Array<Migration> by lazy {
        arrayOf(
        )
    }

//    private val migration_10_11 = object : Migration(10, 11) {
//        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL("DROP TABLE txtTocRules")
//            database.execSQL(
//                """CREATE TABLE txtTocRules(id INTEGER NOT NULL,
//                    name TEXT NOT NULL, rule TEXT NOT NULL, serialNumber INTEGER NOT NULL,
//                    enable INTEGER NOT NULL, PRIMARY KEY (id))"""
//            )
//        }
//    }
//
//

}