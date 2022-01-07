package com.mckj.cleancore.manager.junk

import com.mckj.cleancore.entity.IJunkEntity
import com.mckj.cleancore.tools.AbsJunkTool
import com.mckj.cleancore.util.Log
import io.reactivex.rxjava3.functions.Consumer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Describe:
 *
 * Created By yangb on 2021/5/17
 */
class JunkManager {

    companion object {

        const val TAG = "JunkManager"

        private val INSTANCE by lazy { JunkManager() }

        fun getInstance(): JunkManager = INSTANCE

    }

    /**
     * 是否允许操作
     */
    private val mOptEnable = AtomicBoolean(true)

    /**
     * 是否执行中
     */
    private val mRunning = AtomicBoolean(false)

    /**
     * 垃圾扫描
     */
    suspend fun scan(type: Int, consumer: Consumer<IJunkEntity>): Boolean {
        Log.i(TAG, "scan: type:$type")
        mOptEnable.set(true)
        if (mRunning.get()) {
            Log.i(TAG, "scan error: mRunning is true")
            return false
        }
        val tools = JunkToolManager.getInstance().getToolsByType(type)
        if (tools.isEmpty()) {
            Log.i(TAG, "scan error: tools is empty")
            return false
        }
        return withContext(Dispatchers.IO) {
            mRunning.set(true)
            tools.forEach { tool ->
                if (!mOptEnable.get()) {
                    return@forEach
                }
                tool.setOptEnable(true)
                tool.scan(consumer)
            }
            mRunning.set(false)
            true
        }
    }

    /**
     * 垃圾清理
     */
    suspend fun clean(list: List<IJunkEntity>, consumer: Consumer<IJunkEntity>): Boolean {
        Log.i(TAG, "clean: list.size:${list.size}")
        mOptEnable.set(true)
        if (mRunning.get()) {
            Log.i(TAG, "clean error: mRunning is true")
            return false
        }
        if (list.isEmpty()) {
            Log.i(TAG, "clean error: list is empty")
            return false
        }
        return withContext(Dispatchers.IO) {
            mRunning.set(true)
            list.forEach { entity ->
                if (!mOptEnable.get()) {
                    return@forEach
                }
                val tool: AbsJunkTool? = JunkToolManager.getInstance().getTool(entity.getJunkType())
                tool?.setOptEnable(true)
                val result: Boolean = tool?.clean(entity) ?: false
                if (result) {
                    consumer.accept(entity)
                }
            }
            mRunning.set(false)
            true
        }
    }

    fun stop() {
        val tools = JunkToolManager.getInstance().getAllTools()
        tools.forEach {
            it.setOptEnable(false)
        }
    }

    /**
     * 扫描清理
     */
    suspend fun scanAndClean(type: Int): Boolean {
        Log.i(TAG, "scanAndClean: type:$type")
        mOptEnable.set(true)
        if (mRunning.get()) {
            Log.i(TAG, "scanAndClean error: mRunning is true")
            return false
        }
        val tools = JunkToolManager.getInstance().getToolsByType(type)
        if (tools.isEmpty()) {
            Log.i(TAG, "scanAndClean error: tools is empty")
            return false
        }
        return withContext(Dispatchers.IO) {
            mRunning.set(true)
            tools.forEach { tool ->
                if (!mOptEnable.get()) {
                    return@forEach
                }
                tool.setOptEnable(true)
                tool.scan { entity ->
                    tool.clean(entity)
                }
            }
            mRunning.set(false)
            true
        }
    }


}