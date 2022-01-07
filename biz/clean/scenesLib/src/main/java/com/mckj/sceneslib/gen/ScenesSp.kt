package com.mckj.sceneslib.gen

import com.tencent.mmkv.MMKV

/**
 * Scenes
 * Created by [Spgen-go] on 17:34:23 2021-06-05.
 */
class ScenesSp {

    companion object {
        val instance: ScenesSp by lazy { ScenesSp() }
    }

    private val mmkv: MMKV = MMKV.defaultMMKV()

    private constructor()

	// === Total 1 ===
	
	/**
	 * 最大网速
	 */
    var networkSpeedMax: Long
        get() {
            return mmkv.decodeLong("networkSpeed:max", 0L)
        }
        set(value) {
            mmkv.encode("networkSpeed:max", value)
		}
	

	
}
