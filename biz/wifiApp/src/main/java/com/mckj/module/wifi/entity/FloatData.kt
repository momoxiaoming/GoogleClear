package com.mckj.module.wifi.entity

import android.util.Log
import com.mckj.baselib.util.JsonUtil
import com.mckj.datalib.gen.DataSp

/**
 * Describe:浮动控件数据集
 *
 * Created By yangb on 2021/1/25
 */
data class FloatData(
    var goldList: MutableList<FloatEntity>,
    var lotteryList: MutableList<FloatEntity>,
    var wheelList: MutableList<FloatEntity>
) {

    companion object {

        const val TAG = "FloatData"

        fun getObject(): FloatData? {
            val json = DataSp.instance.dataFloatData
            Log.i(TAG, "getObject: json:$json")
            return JsonUtil.fromJson<FloatData>(json, FloatData::class.java)
        }

        fun saveObject(floatData: FloatData) {
            val json = JsonUtil.toJson(floatData) ?: ""
            Log.i(TAG, "saveObject: json:$json")
            DataSp.instance.dataFloatData = json
        }

    }

}