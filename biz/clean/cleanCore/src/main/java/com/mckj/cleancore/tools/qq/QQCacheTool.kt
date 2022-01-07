package com.mckj.cleancore.tools.qq

import com.mckj.cleancore.entity.IJunkEntity
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
class QQCacheTool : AbsJunkTool() {

    companion object {
        const val TAG = "QQCacheTool"
    }

    override fun getName(): String {
        return "qq清理"
    }

    override fun scan(consumer: Consumer<IJunkEntity>): Boolean {
        if (!isOptEnable()) {
            Log.e(TAG, "scan error: isOptEnable is false")
            return false
        }
        val appList = mutableListOf<String>("com.tencent.mobileqq")
        val allAppJunkList: List<JunkDbEntity>? =
            JunkDbImpl.instance.getListByPackageNames(appList)
        Log.i(TAG, "scan: allAppList.size:${allAppJunkList?.size}")
        if (allAppJunkList.isNullOrEmpty()) {
            Log.e(TAG, "scan error: allAppJunkList is null")
            return false
        }
        AppJunkHelper.parseAppJunk(
            JunkType.QQ,
            AppJunkHelper.getAppMap(appList, allAppJunkList),
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