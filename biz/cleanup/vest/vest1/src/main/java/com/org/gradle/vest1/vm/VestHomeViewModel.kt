package com.org.gradle.vest1.vm

import android.app.ActivityManager
import android.content.Context
import android.text.format.Formatter
import androidx.lifecycle.MutableLiveData
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.log.VLog
import com.mckj.template.BaseHomeViewModel
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * <pre>
 *     author:
 *     time  : 2022/7/21
 *     desc  : new class
 * </pre>
 */
class VestHomeViewModel :BaseHomeViewModel() {

    val ramLiveData=MutableLiveData<Pair<Float,Float>>()

    fun getRamInfo(){
        //获得ActivityManager服务的对象
        val mActivityManager =
            AppMod.app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //获得MemoryInfo对象
        val memoryInfo =
            ActivityManager.MemoryInfo()
        //获得系统可用内存，保存在MemoryInfo对象上
        mActivityManager.getMemoryInfo(memoryInfo)
        val memSize = memoryInfo.totalMem
        val avSize=memoryInfo.availMem  //可用内存大小
        val ramSize=memSize-avSize

        VLog.i("已用内存：$ramSize---总共：$ramSize")
//        ramLiveData.value= Pair(ramSize,memSize)
    }

}