package com.mckj.sceneslib.gen

import com.tencent.mmkv.MMKV

/**
 * Scenes
 * Created by [Spgen-go] on 11:50:55 2021-11-12.
 */
class ScenesSp {

    companion object {
        val instance: ScenesSp by lazy { ScenesSp() }
    }

    private val mmkv: MMKV = MMKV.defaultMMKV()

    private constructor()

	// === Total 2 ===
	
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
	
	/**
	 * 自动任务时间
	 */
    var executorTaskTime: Long
        get() {
            return mmkv.decodeLong("executorTaskTime", 0L)
        }
        set(value) {
            mmkv.encode("executorTaskTime", value)
		}
	

	
}
