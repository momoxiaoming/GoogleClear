package com.mckj.sceneslib.entity

import com.dn.vi.app.cm.utils.JsonUtil
import com.google.gson.annotations.SerializedName


/**
 * Describe:
 *
 * Created By yangb on 2021/3/11
 */
data class SpeedTestServer(
    @SerializedName("wifi_url")
    val wifiUrl: String?,

    @SerializedName("3g_url")
    val g3Url: String?,

    @SerializedName("4g_url")
    val g4Url: String?,

    @SerializedName("5g_url")
    val g5Url: String?,
) {

    companion object {

        fun fromJson(json: String): SpeedTestServer? {
            return JsonUtil.fromJson<SpeedTestServer>(json, SpeedTestServer::
            class.java)
        }
    }

}