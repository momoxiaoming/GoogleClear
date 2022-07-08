package com.mckj.gallery.bean

import android.os.Parcel
import android.os.Parcelable


/**
 *@author leix
 *@version 1
 *@createTime 2021/7/20 11:22
 *@desc
 */
data class MediaBean(
    val id: Long? = 0,
    val title: String? = "",
    val originalPath: String? = "",
    val createData: Long? = 0,
    val mimeType: String? = "",
    val length: Long = 0,
    val bucketId: String? = "",
    val bucketDisplayName: String? = "",
    val thumbnailBigPath: String? = "",
    val thumbnailSmallPath: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    fun isVideo(): Boolean {
        return mimeType?.startsWith("video") == true
    }

    fun isImage(): Boolean {
        return mimeType?.startsWith("image") == true
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(originalPath)
        parcel.writeValue(createData)
        parcel.writeString(mimeType)
        parcel.writeLong(length)
        parcel.writeString(bucketId)
        parcel.writeString(bucketDisplayName)
        parcel.writeString(thumbnailBigPath)
        parcel.writeString(thumbnailSmallPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MediaBean> {
        override fun createFromParcel(parcel: Parcel): MediaBean {
            return MediaBean(parcel)
        }

        override fun newArray(size: Int): Array<MediaBean?> {
            return arrayOfNulls(size)
        }
    }
}