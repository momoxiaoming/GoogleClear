package com.mckj.cleancore.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/16 9:36
 * @desc
 */
@Entity(tableName = "versionDB")
data class JunkVersionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val _id: Long? = 0,
    @ColumnInfo(name = "version_code")
    val version_code: Int? = 0,
    @ColumnInfo(name = "version_name")
    val version_name: String? = "",
    @ColumnInfo(name = "update")
    val update: String? = ""
)