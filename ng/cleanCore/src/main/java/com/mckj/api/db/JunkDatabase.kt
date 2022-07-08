package com.mckj.api.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dn.vi.app.base.app.AppMod
import com.mckj.api.db.dao.JunkDbDao
import com.mckj.api.db.dao.JunkVersionDao
import com.mckj.api.db.entity.JunkDbEntity
import com.mckj.api.db.entity.JunkVersionEntity
import com.mckj.api.util.FileUtils
import com.mckj.api.util.DbUtil
import com.mckj.cleancore.R
import java.io.File

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/4 10:32
 * @desc
 */
@Database(
    entities = [JunkDbEntity::class, JunkVersionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class JunkDatabase : RoomDatabase() {

    companion object {
        private const val DB_NAME = "cleanup.db"
        private var instance: JunkDatabase? = null
        private const val TAG = "DbMonitor"

        @Synchronized
        fun getInstance(): JunkDatabase {
            if (null == instance) createDb()
            return instance!!
        }

        private fun createDb() {
            instance = if (FileUtils.isFileExists
                    (AppMod.app.applicationContext, DbUtil.getDbPath())
            ) {
                createDatabaseFromFile()
            } else {
                createDatabaseDefault()
            }
        }

        private fun createDatabaseFromFile(): JunkDatabase {
            return Room.databaseBuilder(
                AppMod.app.applicationContext, JunkDatabase::class.java,
                DB_NAME
            ).createFromFile(File(DbUtil.getDbPath()))
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()

        }

        private fun createDatabaseDefault(): JunkDatabase {
            return Room.databaseBuilder(
                AppMod.app.applicationContext, JunkDatabase::class.java,
                DB_NAME
            ).createFromInputStream {
                AppMod.app.resources.openRawResource(R.raw.cleanup)
            }
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun junkDbDao(): JunkDbDao

    abstract fun junkVersionDao(): JunkVersionDao
}