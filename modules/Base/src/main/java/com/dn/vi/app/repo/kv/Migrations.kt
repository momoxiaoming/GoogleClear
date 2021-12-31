package com.dn.vi.app.repo.kv

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * kv 数据库升级
 * version 1 to 2
 * Created by holmes on 2020/7/6.
 */
internal class Migration1to2 : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE kvlite ADD COLUMN int2 INTEGER NOT NULL DEFAULT (0)")
    }

}