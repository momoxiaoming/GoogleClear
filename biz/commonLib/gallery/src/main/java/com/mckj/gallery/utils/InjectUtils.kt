package com.mckj.gallery.utils

import com.mckj.gallery.datebase.GalleryDataBase
import com.mckj.gallery.viewmodel.GalleryRepository
import com.mckj.gallery.viewmodel.RecycleRepository


/**
 * @author xx
 * @version 1
 * @createTime 2021/7/23 15:45
 * @desc
 */
object InjectUtils {

    fun getRecycledRepository(): RecycleRepository {
        return RecycleRepository(GalleryDataBase.getInstance().galleryDao())
    }

    fun getGalleryRepository(): GalleryRepository {
        return GalleryRepository()
    }
}