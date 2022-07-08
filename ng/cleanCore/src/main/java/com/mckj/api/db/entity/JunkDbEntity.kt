package com.mckj.api.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/4 10:32
 * @desc
 */
@Entity(
    tableName = "CleanDB",
    indices = [
        androidx.room.Index(value = ["junk_type", "file_type"]),
        androidx.room.Index(value = ["strategy"]),
        androidx.room.Index(value = ["file_path"]),
        androidx.room.Index(value = ["file_path", "root_path"]),
        androidx.room.Index(value = ["pkg_name"]),
    ]
)
data class JunkDbEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id") val _id: Long? = 0,

    /**
     * 包名
     */
    @ColumnInfo(name = "pkg_name") val packageName: String?,

    /**
     * 应用名
     */
    @ColumnInfo(name = "app_name") val appName: String?,

    /**
     * 垃圾分类
     *
    0其它, 1缓存，2广告，3下载.
     */
    @ColumnInfo(name = "junk_type") val junkType: Int?,

    /**
     * 文件类型
    文件类型(0其它, 1图片，2音频，3视频，4日志，5压缩包, 6安装包)
     */
    @ColumnInfo(name = "file_type") val fileType: Int?,

    /**
     * 描述
     */
    @ColumnInfo(name = "desc") val desc: String?,

    @ColumnInfo(name = "file_path") var filePath: String?,

    @ColumnInfo(name = "root_path") var rootPath: String? = "'",

    @ColumnInfo(name = "strategy") val strategy: Int?,

    /**
     * 人工修改过的路径
     */
    @ColumnInfo(name = "update") val update: String? = "",
)