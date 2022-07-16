package com.mckj.api.client

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mckj.api.client.task.base.BaseTask
import com.mckj.api.entity.*
import com.mckj.api.util.FileUtils
import com.mckj.api.util.RFileUtils
import com.mckj.api.util.ScopeHelper
import com.org.openlib.help.Consumer2
import com.org.openlib.help.Consumer3
import com.org.proxy.EvAgent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author xx
 * @version 1
 * @createTime 2021/10/28 18:16
 * @desc 扫描任务执行者
 */
class JunkExecutor internal constructor(builder: Builder) {
    companion object {
        const val TAG = "ScanExecutor"
        const val SCAN_TIME_OUT = 40L
    }

    val mType: Int = builder.mType ?: 0

    private val mScanTask: List<BaseTask>? = builder.scanTasks

    //清理监听：需要监听清理结果的可以注册
    private val mCleanMonitor: CleanMonitor? = builder.mCleanMonitor

    private val mScanData = MutableLiveData<ScanBean>()

    /**
     * 是否允许操作
     */
    private val mOptEnable = AtomicBoolean(true)

    /**
     * 是否执行中
     */
    private val mRunning = AtomicBoolean(false)


    fun scan() {
        val scanStartTime = System.currentTimeMillis()
        ScopeHelper.launch {
            withContext(Dispatchers.IO) {
                mOptEnable.set(true)
                val scanBean = ScanBean()
                start(scanBean)
                val cacheJunk = CacheJunk(junkSize = 0L, appJunks = mutableListOf())
                scanBean.junk = cacheJunk
                if (mScanTask.isNullOrEmpty()) {
                    Log.d(TAG, "scanTask  must not be empty")
                    error(scanBean)
                    return@withContext
                }
                if (mRunning.get()) {
                    error(scanBean)
                    Log.d(TAG, "scanTask  has bean started")
                    return@withContext
                }
                mScanTask.forEach {
                    //正常扫描逻辑
                    if (mOptEnable.get()) {
                        mRunning.set(true)
                        val isSuccess = it.scan { appJunk ->
                            cacheJunk.appJunks?.add(appJunk)
                            cacheJunk.junkSize += appJunk.junkSize
                            val useTime = System.currentTimeMillis() - scanStartTime
                            if (useTime > TimeUnit.SECONDS.toMillis(SCAN_TIME_OUT)) {
                                it.stop()
                                complete(scanBean)
                                return@scan
                            }
                            if (!mOptEnable.get()) {
                                it.stop()
                                complete(scanBean)
                                return@scan
                            } else {
                                startIdle(scanBean)
                            }
                        }
                        if (!mOptEnable.get()) {
                            return@forEach
                        }
                        if (isSuccess) {
                            startIdle(scanBean)
                            delay(100)
                        }
                    }
                }
                complete(scanBean)
                mRunning.set(false)
            }
        }
    }

    fun loadCache(cacheJunk: CacheJunk?) {
        ScopeHelper.launch {
            withContext(Dispatchers.IO) {
                val scanBean = ScanBean(status = JunkConstants.ScanStatus.CACHE)
                scanBean.junk = cacheJunk
                mScanData.postValue(scanBean)
                Log.d(TAG, "scan cache")
                EvAgent.sendEvent("home_scan_stop")
            }
        }
    }

    /**
     * 静默扫描 不关注扫描状态
     */
    suspend fun silentScan(
        consumer: Consumer3<Int, AppJunk?, CacheJunk?>? = null,
        block: (cacheJunk: CacheJunk) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            mOptEnable.set(true)
            val cacheJunk = CacheJunk(junkSize = 0L, appJunks = mutableListOf())
            val scanBean = ScanBean(junk = cacheJunk, status = JunkConstants.ScanStatus.SILEN)
            if (mScanTask.isNullOrEmpty()) {
                Log.d(TAG, "scanTask  must not be empty")
                error(scanBean)
                consumer?.accept(JunkConstants.ScanStatus.COMPLETE, null, cacheJunk)
                return@withContext
            }
            if (mRunning.get()) {
                Log.d(TAG, "scanTask  has bean started")
                error(scanBean)
                consumer?.accept(JunkConstants.ScanStatus.COMPLETE, null, cacheJunk)
                return@withContext
            }
            val scanStartTime = System.currentTimeMillis()
            mScanTask.forEach {
                if (mOptEnable.get()) {
                    mRunning.set(true)
                    it.scan { appJunk ->
                        val useTime = System.currentTimeMillis() - scanStartTime
                        if (useTime > TimeUnit.SECONDS.toMillis(SCAN_TIME_OUT)) {
                            it.stop()
                            complete(scanBean)
                            return@scan
                        }
                        if (!mOptEnable.get()) {
                            it.stop()
                            if (mScanData.value == null) {
                                mScanData.postValue(scanBean)
                            }
                            return@scan
                        }
                        cacheJunk.appJunks?.add(appJunk)
                        cacheJunk.junkSize += appJunk.junkSize
                        consumer?.accept(JunkConstants.ScanStatus.SCAN_IDLE, appJunk, cacheJunk)
                    }
                }
            }
            mRunning.set(false)
            consumer?.accept(JunkConstants.ScanStatus.COMPLETE, null, cacheJunk)
            if (mScanData.value == null) {
                mScanData.postValue(scanBean)
            }
            return@withContext
        }
    }

    /**
     * 自清理
     */
    fun autoClean(block: (junkInfo: JunkInfo) -> Unit) {
        ScopeHelper.launch {
            withContext(Dispatchers.IO) {
                if (mScanTask.isNullOrEmpty()) {
                    Log.d(TAG, "scanTask  must not be empty")
                    return@withContext
                }
                if (mRunning.get()) {
                    Log.d(TAG, "scanTask  has bean started")
                    return@withContext
                }
                mScanTask.forEach {
                    if (mOptEnable.get()) {
                        mRunning.set(true)
                        it.scan { appJunk ->
                            appJunk?.junks?.iterator()?.apply {
                                while (hasNext()) {
                                    val next = next()
                                    if (delete(next)) {
                                        block.invoke(next)
                                    }
                                }
                            }
                        }
                    }
                }
                mRunning.set(false)
                return@withContext
            }
        }
    }

    /**
     * 自清理
     */
    suspend fun autoCleanBySuspend(block: (junkInfo: JunkInfo) -> Unit) {
        withContext(Dispatchers.IO) {
            mOptEnable.set(true)
            if (mScanTask.isNullOrEmpty()) {
                Log.d(TAG, "scanTask  must not be empty")
                return@withContext
            }
            if (mRunning.get()) {
                Log.d(TAG, "scanTask  has bean started")
                return@withContext
            }
            mScanTask.forEach {
                if (mOptEnable.get()) {
                    mRunning.set(true)
                    it.scan { appJunk ->
                        appJunk?.junks?.iterator()?.apply {
                            while (hasNext()) {
                                val next = next()
                                if (delete(next)) {
                                    block.invoke(next)
                                }
                            }
                        }
                    }
                }
            }
            mRunning.set(false)
            return@withContext
        }
    }

    private suspend fun start(scanBean: ScanBean) {
        scanBean.status = JunkConstants.ScanStatus.START
        mScanData.postValue(scanBean)
        Log.d(TAG, "scan start")
        EvAgent.sendEvent("home_scan_start")
    }

    private fun startIdle(scanBean: ScanBean) {
        scanBean.status = JunkConstants.ScanStatus.SCAN_IDLE
        mScanData.postValue(scanBean)
//        Log.d(TAG, "scan startIdle")
    }

    private fun error(scanBean: ScanBean) {
        scanBean.status = JunkConstants.ScanStatus.ERROR
        mScanData.postValue(scanBean)
        Log.d(TAG, "scan error")
        EvAgent.sendEvent("home_scan_stop")
    }

    private fun complete(scanBean: ScanBean) {
        scanBean.status = JunkConstants.ScanStatus.COMPLETE
        mScanData.postValue(scanBean)
        Log.d(TAG, "scan complete")
        EvAgent.sendEvent("home_scan_stop")
    }

    fun stop() {
        mOptEnable.set(false)
        mRunning.set(false)
    }

    //清理，以最小颗粒执行
    fun clean(
        junks: MutableList<JunkInfo>,
        consumer2: Consumer2<Int, JunkInfo?>? = null,
        block: ((junkInfo: JunkInfo) -> Unit)? = null
    ) {
        ScopeHelper.launch {
            cleanInSuspend(junks, consumer2, block)
        }
    }

    //清理，以最小颗粒执行
    suspend fun cleanBySuspend(
        junks: MutableList<JunkInfo>,
        consumer2: Consumer2<Int, JunkInfo?>? = null,
        block: ((junkInfo: JunkInfo) -> Unit)? = null,
        delay: Long?
    ) {
        try {
            cleanInSuspend(junks, consumer2, block, delay)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun cleanInSuspend(
        junks: MutableList<JunkInfo>,
        consumer2: Consumer2<Int, JunkInfo?>? = null,
        block: ((junkInfo: JunkInfo) -> Unit)?,
        delay: Long? = 0
    ) {
        var cleanSize = 0L
        val cleanList = mutableListOf<JunkInfo>()
        val scanData = mScanData.value
        val cacheJunk = scanData?.junk
        var scanSize = scanData?.junk?.junkSize ?: 0
        withContext(Dispatchers.IO) {
            cacheJunk?.let {
                junks.iterator().run {
                    while (hasNext()) {
                        delay?.let {
                            delay(it)
                        }
                        val next = next()
                        val isSuccess = delete(next)
                        if (isSuccess) {
                            removeFromOrigin(it, next) {
                                //统计清理大小
                                cleanSize += next.junkSize
                                cleanList.add(next)
                                //刷新剩余大小
                                scanSize -= next.junkSize
                                scanData.junk?.junkSize = scanSize
                                block?.invoke(next)
                                consumer2?.accept(1, next)
                            }
                        }
                    }
                }
            }
        }
        withContext(Dispatchers.Main) {
            Log.d(TAG, "clean complete")
            scanData?.status = JunkConstants.ScanStatus.CLEAN
            mScanData.postValue(scanData!!)
            consumer2?.accept(JunkConstants.ScanStatus.CLEAN, null)
            mCleanMonitor?.clean(cleanSize, cleanList)
        }
    }

    private fun removeFromOrigin(cacheJunk: CacheJunk, junkInfo: JunkInfo, block: () -> Unit) {
        cacheJunk.appJunks?.iterator()?.apply {
            while (hasNext()) {
                val appJunk = next()
                appJunk.junks?.iterator()?.let {
                    while (it.hasNext()) {
                        val next = it.next()
                        if (next.path == junkInfo.path) {
                            Log.d(TAG, "hit target：path:${next.path}")
                            block.invoke()
                            appJunk.junkSize -= next.junkSize
                            it.remove()
                        }
                    }
                }
            }
        }
    }

    private fun delete(junkInfo: JunkInfo): Boolean {
        junkInfo.uri?.let {
            return RFileUtils.deleteFile(it)
        } ?: let {
            return FileUtils.delete(junkInfo.path)
        }
    }


    fun getScanData(): MutableLiveData<ScanBean> {
        return mScanData
    }


    class Builder {
        internal var scanTasks: List<BaseTask>? = null
        internal var mCleanMonitor: CleanMonitor? = null
        internal var mType: Int? = null

        fun type(type: Int) = apply {
            this.mType = type
        }

        fun task(tasks: List<BaseTask>) = apply {
            this.scanTasks = tasks
        }

        fun cleanMonitor(cleanMonitor: CleanMonitor?) = apply {
            this.mCleanMonitor = cleanMonitor
        }

        fun build(): JunkExecutor = JunkExecutor(this)
    }

    interface CleanMonitor {
        fun clean(cleanSize: Long, cleanList: MutableList<JunkInfo>)
    }
}