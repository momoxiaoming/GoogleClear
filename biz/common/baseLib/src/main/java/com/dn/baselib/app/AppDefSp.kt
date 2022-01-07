package com.dn.baselib.app

import com.tencent.mmkv.MMKV

/**
 * App
 * Created by [Spgen-go] on 18:09:28 2020-11-04.
 */
class AppDefSp {

    companion object {
        val instance: AppDefSp by lazy { AppDefSp() }
    }

    private val mmkv: MMKV = MMKV.defaultMMKV()!!

    private constructor()

	// === Total 2 ===
	
	/**
	 * 应用进程第一次启动时间
	 */
    var appBootAt: Long
        get() {
            return mmkv.decodeLong("app:boot:at", 0L)
        }
        set(value) {
            mmkv.encode("app:boot:at", value)
		}


	
}
