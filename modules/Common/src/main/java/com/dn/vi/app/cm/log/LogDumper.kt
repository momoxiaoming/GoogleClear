package com.dn.vi.app.cm.log

import android.content.Context
import com.tencent.mars.xlog.Log
import okio.*
import java.io.File
import java.io.IOException
import kotlin.concurrent.withLock

/**
 * 把内部的日志，dump到外存里面
 * xlog经常，不直接写外存文件
 * Created by holmes on 2020/6/4.
 */
class LogDumper {

    companion object {}

    /**
     * 将内部存储的xlog日志，dump到[dumpToDir]中，
     * 如果[dumpToDir]为null，则默认dump到 "externalCacheDir/logs/d"
     * @return 到处的文件数。-1: 失败。
     */
    fun dump(context: Context, dumpToDir: File?): Int {

        // from cachePath to logPath
        val cacheDir = File(context.filesDir, "/xlog")

        val dumpedDir = if (dumpToDir == null || !dumpToDir.isDirectory) {
            val logDir = context.externalCacheDir
            val logPath = File(logDir, "logs")

            File(logPath, "d")
        } else {
            dumpToDir
        }

        if (!dumpedDir.exists()) {
            dumpedDir.mkdirs()
        }
        if (!dumpedDir.exists() || !dumpedDir.isDirectory) {
            println("Dump xlog error. 'd' dir not found")
            return -1
        }

        Log.appenderFlushSync(true)

        if (cacheDir == null) {
            return -1
        }

        val cachedLog =
            cacheDir.listFiles { pathname -> pathname.name.endsWith(".xlog") }

        if (cachedLog == null || cachedLog.size == 0) {
            return -1
        }

        var dumpedCount = 0
        VLog.IMPL_LOCK.writeLock().withLock {
            Log.appenderClose()
            try {
                for (i in cachedLog.indices) {
                    val src = cachedLog[i]
                    val dst = File(dumpedDir, src.name)
                    if (dst.exists()) {
                        if (dst.lastModified() >= src.lastModified()) {
                            continue
                        }
                    }
                    try {
                        copy(src, dst)
                        dumpedCount++
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } finally {
                VLog.reInitialize()
            }
        }

        return dumpedCount
    }

    @Throws(IOException::class)
    fun copy(from: File, to: File) {
        var src: BufferedSource? = null
        var dst: BufferedSink? = null
        try {
            src = from.source().buffer()
            dst = to.sink().buffer()
            dst.writeAll(src)
            dst.flush()
        } finally {
            src?.close()
            dst?.close()
        }
    }
}