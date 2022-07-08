package com.org.proxy

import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

/**
 * EvAgent
 *
 * @author mmxm
 * @date 2022/7/4 20:29
 */
object EvAgent {

    fun sendEvent(event: String) {
        sendEventMap(event, mapOf())
    }

    fun sendEventMap(event: String, map: Map<String, String>) {
        Firebase.analytics.logEvent(event, mapToBundle(map))
    }
    private fun mapToBundle(map: Map<String, String>): Bundle {
        val bundle = Bundle()
        map.keys.forEach {
            bundle.putString(it, map[it])
        }
        return bundle
    }
}