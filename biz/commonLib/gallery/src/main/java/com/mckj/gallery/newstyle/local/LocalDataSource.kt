package com.mckj.gallery.newstyle.local

import com.dn.vi.app.repo.kv.KvSp
import com.mckj.baselib.util.JsonUtil
import com.mckj.gallery.bean.MediaBean

object LocalDataSource {
    private const val KEY_GALLERY_HIS = "key_gallery_his"
    private const val KEY_GALLERY_CLEAN_SIZE = "key_gallery_clean_size"
    private const val KEY_GALLERY_AGAIN = "key_gallery_again"
    const val AGAIN = "again"

    fun saveGalleryHis(obj:MediaBean){
        KvSp.putObj(KEY_GALLERY_HIS, obj)
    }

    fun getGalleryHis():MediaBean? = KvSp.getKv(KEY_GALLERY_HIS).json(MediaBean::class.java)

    fun deleteGallery() = KvSp.delete(KEY_GALLERY_HIS)

    fun saveCleanSize(size:Long){
        KvSp.putLong(KEY_GALLERY_CLEAN_SIZE,size)
    }

    fun getCleanSize() = KvSp.getLong(KEY_GALLERY_CLEAN_SIZE)

    fun saveAgain(){
        KvSp.putKv(KEY_GALLERY_AGAIN,AGAIN)
    }

    fun getAgain():String = KvSp.getKv(KEY_GALLERY_AGAIN)

    fun deleteAgain(){
        KvSp.delete(KEY_GALLERY_AGAIN)
    }

    fun <T>String.json(clazz: Class<T>): T? {
        return JsonUtil.fromJson(this,clazz)
    }
}