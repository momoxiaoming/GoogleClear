package com.mckj.sceneslib.util

import com.tencent.mmkv.MMKV

object VestUtil {
    private const val vestNumber = "vest_number"

    private val vestNum = MMKV.defaultMMKV().decodeInt(vestNumber, 1)

    //获得马甲的序号
    fun getVestNum(): Int{
        return vestNum
    }
}