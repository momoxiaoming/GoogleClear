package com.dn.vi.app.repo.kv

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Kv db
 * Created by holmes on 2020/6/29.
 **/
@Database(entities = [KvEntity::class], version = 2)
abstract class KvDatabase : RoomDatabase() {

    abstract fun kvDao(): KvDao

}