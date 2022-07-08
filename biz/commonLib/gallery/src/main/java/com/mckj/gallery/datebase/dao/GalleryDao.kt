package com.mckj.gallery.datebase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mckj.gallery.datebase.entity.RecycledBean

/**
 * @author leix
 * @version 1
 * @createTime 2021/7/23 14:50
 * @desc
 */
@Dao
interface GalleryDao {
    @Query("SELECT * FROM recycled_table")
    fun getRecycledLiveData(): LiveData<List<RecycledBean>>

    @Query("SELECT * FROM recycled_table")
    fun getRecycledData(): List<RecycledBean>


    @Query("SELECT * FROM recycled_table where mimeType = :mimeType")
    fun getRecycledDataWithMimeType(mimeType: String): LiveData<List<RecycledBean>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bean: RecycledBean):Long

    @Query("DELETE FROM recycled_table")
    suspend fun deleteAll()

    @Query("DELETE FROM recycled_table where _id = :id")
     fun deleteById(id: Long)
}