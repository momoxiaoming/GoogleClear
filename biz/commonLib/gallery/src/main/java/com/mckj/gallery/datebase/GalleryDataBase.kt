package com.mckj.gallery.datebase

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dn.vi.app.base.app.AppMod
import com.mckj.gallery.datebase.dao.GalleryDao
import com.mckj.gallery.datebase.entity.RecycledBean

/**
 * @author leix
 * @version 1
 * @createTime 2021/7/23 14:39
 * @desc
 */
@Database(entities = [RecycledBean::class], version = 1, exportSchema = false)
abstract class GalleryDataBase : RoomDatabase() {
    companion object {
        private const val DB_NAME = "cleanUpx-db"
        private val galleryDatabase by lazy { createGalleryDatabase() }

        fun getInstance() = galleryDatabase

        fun close() {
            galleryDatabase.close()
        }

        private fun createGalleryDatabase(): GalleryDataBase {
            return Room.databaseBuilder(
                AppMod.app.applicationContext,
                GalleryDataBase::class.java, DB_NAME
            ).allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }


    abstract fun galleryDao():GalleryDao

}