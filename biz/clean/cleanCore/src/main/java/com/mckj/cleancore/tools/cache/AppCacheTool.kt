package com.mckj.cleancore.tools.cache

import android.content.pm.PackageInfo
import com.dn.vi.app.base.app.AppMod
import com.mckj.cleancore.entity.IJunkEntity
import com.dn.vi.app.cm.utils.AppUtil
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.cleancore.db.entity.JunkDbEntity
import com.mckj.cleancore.impl.junk.JunkDbImpl
import com.mckj.cleancore.manager.junk.AppJunkHelper
import com.mckj.cleancore.tools.AbsJunkTool
import com.mckj.cleancore.tools.JunkType
import com.mckj.cleancore.util.Log
import io.reactivex.rxjava3.functions.Consumer

/**
 * Describe:
 *
 * Created By yangb on 2021/5/17
 */
class AppCacheTool : AbsJunkTool() {

    companion object {
        const val TAG = "AppCacheTool"
    }

    override fun getName(): String {
        return "应用缓存"
    }

    override fun scan(consumer: Consumer<IJunkEntity>): Boolean {
        if (!isOptEnable()) {
            Log.e(TAG, "scan error: isOptEnable is false")
            return false
        }
        //获取已安装app列表
        val packageInfoList: List<PackageInfo> = AppUtil.getInstalledAppInfoList(AppMod.app)
        Log.i(TAG, "scan: packageInfoList.size:${packageInfoList.size}")
        //包名集合
        val names: List<String> = packageInfoList.map { it.packageName }
        //所有安装app数据缓存路径
        val allAppJunkList: List<JunkDbEntity>? =
            JunkDbImpl.instance.getListByPackageNames(names)
        Log.i(TAG, "scan: allAppList.size:${allAppJunkList?.size}")
        if (allAppJunkList.isNullOrEmpty()) {
            Log.e(TAG, "scan error: allAppJunkList is null")
            return false
        }
        AppJunkHelper.parseAppJunk(
            JunkType.APP_CACHE,
            AppJunkHelper.getAppMap(names, allAppJunkList),
            consumer
        )
        return true
    }

    override fun clean(entity: IJunkEntity): Boolean {
        val details = entity.getJunkDetails()
        Log.i(TAG, "clean: details.size:${details?.size ?: 0}")
        details?.forEach {
            FileUtil.delete(it.path)
        }
        return true
    }

}