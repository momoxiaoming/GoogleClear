package com.dn.vi.app.cm.utils

import android.os.Process
import okio.buffer
import okio.source
import java.io.File


/**
 * 进程相关的工具
 * @author holmes
 */
object ProcessUtils {

    /**
     * 获取当前的进程名
     * @return
     */
    val currentProcessName: String
        get() {
            val pid = Process.myPid()
            return getProcessNameByPid(pid)
        }

    /**
     * 通过进程ID, 获取进程名
     * @param pid
     * @return
     */
    fun getProcessNameByPid(pid: Int): String {
        // 读取文件 /proc/{pid}/cmdline
        val procCmd = "/proc/$pid/cmdline"
        val f = File(procCmd)
        var procName: String? = null
        try {
            val src = f.source()
            src.use { src ->
                src.buffer().use { buff ->
                    procName = buff.readUtf8()
                }
            }
        } catch (e: Exception) {
            // This is Auto-generated catch block
            e.printStackTrace()
        }

        procName.also {
            procName = it?.trim { c ->
                c.isIdentifierIgnorable()   // or c <= 20; ' '
            }
        }
        return procName ?: ""
    }

    /**
     * 是否是远程进程
     */
    fun isRemoteProcess(procName: String): Boolean {
        return procName.indexOf(':') != -1
    }

    /**
     * 截取进程的 scope.
     *
     * packageName:scope
     *
     * @param processName
     * @return 如果为empty, 则是main
     */
    fun getProcessScope(processName: String): String? {
        if (processName.isEmpty()) {
            return ""
        }
        val i = processName.indexOf(':')
        return if (i > 0) {
            processName.substring(i + 1)
        } else {
            ""
        }
    }

}
