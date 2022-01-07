package com.mckj.sceneslib.manager.task

import android.content.pm.PackageInfo
import com.dn.baselib.util.ProcessUtil
import com.mckj.cleancore.entity.IJunkEntity
import com.mckj.cleancore.manager.junk.JunkManager
import com.mckj.cleancore.tools.JunkType
import com.mckj.cleancore.tools.apk.ApkFileTool
import com.mckj.cleancore.tools.uninstall.UninstallResidueTool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * 任务池
 *
 * @author mmxm
 * @date 2021/5/11 11:06
 */
class TaskPool : ITask {

    private val apkTool by lazy {
        ApkFileTool()
    }
    private val uninstallTool by lazy {
        UninstallResidueTool()
    }

    private val junkManager by lazy {
        JunkManager.getInstance()
    }

    override suspend fun scanRunningApp(): List<PackageInfo> {
        return ProcessUtil.getRunningApps()!!
    }

    override suspend fun scanJunk(): List<IJunkEntity> {
        return withContext(Dispatchers.IO) {
            val list = mutableListOf<IJunkEntity>()
            junkManager.scan(JunkType.AUTO_SCAN) { entity ->
                list.add(entity)
            }
            list
        }
    }

    /**
     * 清理手机内存
     */
    override suspend fun cleanPhoneRAM(): Boolean {
        return withContext(Dispatchers.IO) {
            ProcessUtil.killBackgroundProcesses()
            delay(1000)
            true
        }
    }

    /**
     * 清理手机空间
     */
    override suspend fun cleanPhoneSpace(): Boolean {
        return withContext(Dispatchers.IO) {
            val junkList = scanJunk()
            junkManager.clean(junkList) {
            }
            delay(1000)
            true
        }
    }

    override suspend fun cleanApk(): Boolean {
        return withContext(Dispatchers.IO) {
            val list = mutableListOf<IJunkEntity>()
            apkTool.scan { entity ->
                list.add(entity)
            }
            list.forEach {
                apkTool.clean(it)
            }
            true
        }
    }

    override suspend fun cleanUninstallJunk(): Boolean {
        return withContext(Dispatchers.IO) {
            val list = mutableListOf<IJunkEntity>()
            uninstallTool.scan { entity ->
                list.add(entity)
            }
            list.forEach {
                uninstallTool.clean(it)
            }
            true
        }
    }


}