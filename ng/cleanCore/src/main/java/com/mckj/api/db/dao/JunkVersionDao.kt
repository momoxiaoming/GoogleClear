package com.mckj.api.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.mckj.api.db.entity.JunkVersionEntity

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/16 9:40
 * @desc
 */
@Dao
interface JunkVersionDao {
    @Query("SELECT * FROM versionDB WHERE version_name = :name")
    fun getJunkVersionByName(name: String): JunkVersionEntity?
}