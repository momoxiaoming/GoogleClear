package com.mckj.sceneslib.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Describe: wifi设备
 *
 * Created By yangb on 2021/4/23
 */
@Parcelize
data class WifiDevice(val name: String, val ip: String) : Parcelable {
}