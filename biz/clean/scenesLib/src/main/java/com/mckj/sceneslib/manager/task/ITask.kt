package com.mckj.sceneslib.manager.task

import android.content.pm.PackageInfo
import com.mckj.cleancore.entity.IJunkEntity

/**
 * ITask
 *
 * @author mmxm
 * @date 2021/5/11 10:45
 */
interface ITask {


    /**
     * 扫描正在运行的app
     */
    suspend fun scanRunningApp(): List<PackageInfo>

    /**
     * 扫描垃圾
     */
    suspend fun scanJunk(): List<IJunkEntity>

    /**
     * 清理手机内存
     */
    suspend fun cleanPhoneRAM(): Boolean

    /**
     * 清理手机空间
     */
    suspend fun cleanPhoneSpace(): Boolean

    /**
     * 清理安装包
     */
    suspend fun cleanApk(): Boolean


    /**
     * 清理卸载残留
     */
    suspend fun cleanUninstallJunk(): Boolean

}