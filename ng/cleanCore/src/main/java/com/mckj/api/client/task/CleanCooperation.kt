package com.mckj.api.client.task

import com.mckj.api.client.JunkConstants
import com.mckj.api.client.JunkExecutor
import com.mckj.api.client.task.base.BaseTask
import com.mckj.api.client.task.mediaTask.QQFileTask
import com.mckj.api.client.task.mediaTask.QQImgTask
import com.mckj.api.client.task.mediaTask.QQVideoTask
import com.mckj.api.client.task.mediaTask.WechatFileTask
import com.mckj.api.client.task.mediaTask.WechatImgTask
import com.mckj.api.client.task.mediaTask.WechatVideoTask
import com.mckj.api.client.task.shallow.ApkFileTask
import com.mckj.api.client.task.shallow.AppCacheTask
import com.mckj.api.client.task.shallow.CommonCacheTask
import com.mckj.api.client.task.shallow.RamCacheTask
import com.mckj.api.client.task.shallow.UninstallResidueTask

/**
 * @author xx
 * @version 1
 * @createTime 2021/10/28 16:12
 * @desc
 */
object CleanCooperation {

    //QQ浅层扫描
    fun getShallowCleanQQTask(): BaseTask {
        return AppCacheTask(pkgName = "com.tencent.mobileqq", appName = "QQ清理")
    }

    //微信浅层扫描
    fun getShallowCleanWxTask(): BaseTask {
        return AppCacheTask(pkgName = "com.tencent.mm", appName = "微信清理")
    }

    //一键扫描
    fun getCommonCacheTask(): BaseTask {
        return CommonCacheTask()
    }

    //无用安装包
    fun getApkCacheTask(): BaseTask {
        return ApkFileTask()
    }

    //内存清理
    fun getRamCacheTask(): BaseTask {
        return RamCacheTask()
    }

    //残留清理
    fun getUninstallResidueTask(): BaseTask {
        return UninstallResidueTask()
    }

    //微信深层扫描任务
    private fun getWxDeepTask(): List<BaseTask> {
        val list = mutableListOf<BaseTask>()
        getShallowCleanWxTask()
        list.add(getShallowCleanWxTask())
        list.add(WechatImgTask())
        list.add(WechatVideoTask())
        list.add(WechatFileTask())
        return list
    }

    //QQ深层扫描任务
    private fun getQQDeepTask(): List<BaseTask> {
        val list = mutableListOf<BaseTask>()
        getShallowCleanWxTask()
        list.add(getShallowCleanQQTask())
        list.add(QQImgTask())
        list.add(QQVideoTask())
        list.add(QQFileTask())
        return list
    }


    /**
     *  Executor
     */
    //应用缓存
    fun getCacheExecutor(monitor: JunkExecutor.CleanMonitor? = null): JunkExecutor {
        val tasks = mutableListOf<BaseTask>()
        tasks.add(getCommonCacheTask())
        return JunkExecutor.Builder()
            .type(JunkConstants.Session.APP_CACHE)
            .task(tasks)
            .cleanMonitor(monitor).build()
    }

    //apk清理
    fun getApkCleanExecutor(monitor: JunkExecutor.CleanMonitor? = null): JunkExecutor {
        val tasks = mutableListOf<BaseTask>()
        tasks.add(getApkCacheTask())
        return JunkExecutor.Builder()
            .type(JunkConstants.Session.APK)
            .task(tasks)
            .cleanMonitor(monitor).build()
    }

    //残留清理
    fun getResidualCleanExecutor(monitor: JunkExecutor.CleanMonitor? = null): JunkExecutor {
        val tasks = mutableListOf<BaseTask>()
        tasks.add(getUninstallResidueTask())
        return JunkExecutor.Builder()
            .type(JunkConstants.Session.UNINSTALL_RESIDUE)
            .task(tasks)
            .cleanMonitor(monitor).build()
    }

    //微信深度清理
    fun getWxDeepCleanExecutor(
        monitor: JunkExecutor.CleanMonitor? = null,
        silent: Boolean = false
    ): JunkExecutor {
        val tasks = mutableListOf<BaseTask>()
        val type = if (silent) {
            JunkConstants.Session.WECHAT_SILENT_CACHE
        } else {
            JunkConstants.Session.WECHAT_CACHE
        }
        tasks.addAll(getWxDeepTask())
        return JunkExecutor.Builder()
            .type(type)
            .task(tasks)
            .cleanMonitor(monitor).build()
    }

    //QQ深度清理
    fun getQQDeepCleanExecutor(
        monitor: JunkExecutor.CleanMonitor? = null,
        silent: Boolean = false
    ): JunkExecutor {
        val tasks = mutableListOf<BaseTask>()
        val type = if (silent) {
            JunkConstants.Session.QQ_SILENT_CACHE
        } else {
            JunkConstants.Session.QQ_CACHE
        }
        tasks.addAll(getQQDeepTask())
        return JunkExecutor.Builder()
            .type(type)
            .task(tasks)
            .cleanMonitor(monitor).build()
    }
}