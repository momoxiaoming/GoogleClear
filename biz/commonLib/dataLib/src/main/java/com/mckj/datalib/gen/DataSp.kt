package com.mckj.datalib.gen

import com.tencent.mmkv.MMKV

/**
 * Data
 * Created by [Spgen-go] on 15:27:55 2021-05-18.
 */
class DataSp {

    companion object {
        val instance: DataSp by lazy { DataSp() }
    }

    private val mmkv: MMKV = MMKV.defaultMMKV()

    private constructor()

	// === Total 1 ===
	
	/**
	 * 浮动控件数据集
	 */
    var dataFloatData: String
        get() {
            return mmkv.decodeString("data:float:data", "")
        }
        set(value) {
            mmkv.encode("data:float:data", value)
		}
	

	
}
