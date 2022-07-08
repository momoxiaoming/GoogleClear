package com.mckj.sceneslib.sp

import com.tencent.mmkv.MMKV

class CommonScenesSp {
    private val mmkv: MMKV = MMKV.defaultMMKV()

    companion object {
        val instance: CommonScenesSp by lazy { CommonScenesSp() }
    }

    /**
     * 插屏广告激励视频占比比例
     */
    var landingBeforeAdRat:Int
    get() {
        return mmkv.decodeInt("scenes:landing_before_ad:reward_rat", 0)
    }
    set(value) {
        mmkv.encode("scenes:landing_before_ad:reward_rat", value)
    }
}