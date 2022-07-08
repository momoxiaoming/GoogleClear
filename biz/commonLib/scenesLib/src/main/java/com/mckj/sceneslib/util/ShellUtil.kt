package com.mckj.sceneslib.util

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Describe:
 *
 * Created By yangb on 2021/5/18
 */
object ShellUtil {

    data class CommandResult(
        val result: Int,
        val successMsg: String? = null,
        val errorMsg: String? = null
    )

    /**
     * 执行shell命令
     * 这是一个耗时任务，请在非UI线程执行
     */
    fun execCmd(cmd: String): CommandResult {
        if (cmd.isEmpty()) {
            return CommandResult(-1)
        }
        var result = -1
        var process: Process? = null
        var successReader: BufferedReader? = null
        var errorReader: BufferedReader? = null
        var successBuilder: StringBuilder? = null
        var errorBuilder: StringBuilder? = null
        try {
            process = Runtime.getRuntime().exec(cmd)
            result = process.waitFor()
            successBuilder = StringBuilder()
            errorBuilder = StringBuilder()
            successReader = BufferedReader(InputStreamReader(process.inputStream, "UTF-8"))
            errorReader = BufferedReader(InputStreamReader(process.errorStream, "UTF-8"))
            var line: String? = null
            while (successReader.readLine().also { line = it } != null) {
                successBuilder.append("${line}\n")
            }
            while (errorReader.readLine().also { line = it } != null) {
                errorBuilder.append("${line}\n")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                successReader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                errorReader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            process?.destroy()
        }
        Log.i("ShellUtil", "execCmd: ${successBuilder.toString()}")
        Log.i("ShellUtil", "execCmd: ${errorBuilder.toString()}")
        return CommandResult(result, successBuilder?.toString(), errorBuilder?.toString())
    }

}