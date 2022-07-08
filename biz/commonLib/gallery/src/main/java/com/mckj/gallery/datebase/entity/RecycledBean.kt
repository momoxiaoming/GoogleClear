package com.mckj.gallery.datebase.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author leix
 * @version 1
 * @createTime 2021/7/23 14:51
 * @desc 回收的媒体资源数据类型
 */
@Entity(tableName = "recycled_table")
data class RecycledBean(
    @PrimaryKey @ColumnInfo(name = "_id") val id: Long,
    //回收的媒体资源存储的位置
    @ColumnInfo(name = "originalPath") val originalPath: String?,
    //大小
    @ColumnInfo(name = "size") val size: Long?,
    //媒体类型
    @ColumnInfo(name = "mimeType") val mimeType: String?,
    //回收时间
    @ColumnInfo(name = "recycledTime") val recycledTime: Long?,

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(originalPath)
        parcel.writeValue(size)
        parcel.writeString(mimeType)
        parcel.writeValue(recycledTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecycledBean> {
        override fun createFromParcel(parcel: Parcel): RecycledBean {
            return RecycledBean(parcel)
        }

        override fun newArray(size: Int): Array<RecycledBean?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is RecycledBean) {
            return false
        }
        return other.id == id
    }
    fun isVideo(): Boolean {
        return mimeType?.startsWith("video") == true
    }

    fun isImage(): Boolean {
        return mimeType?.startsWith("image") == true
    }
}
