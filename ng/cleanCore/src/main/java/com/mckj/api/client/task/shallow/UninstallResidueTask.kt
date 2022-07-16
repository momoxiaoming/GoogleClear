package com.mckj.api.client.task.shallow

import android.content.pm.PackageInfo
import android.util.Log
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.utils.AppUtil
import com.mckj.api.client.JunkConstants
import com.mckj.api.client.JunkParser
import com.mckj.api.client.task.base.BaseTask
import com.mckj.api.db.entity.JunkDbEntity
import com.mckj.api.entity.AppJunk
import com.mckj.api.impl.junk.JunkDbImpl
import com.mckj.api.impl.parser.CommonParser
import com.mckj.api.impl.parser.IParser
import com.mckj.api.impl.parser.QParser
import com.mckj.api.util.FileUtils
import com.mckj.api.util.RFileUtils
import io.reactivex.rxjava3.functions.Consumer
import java.lang.Exception

/**
 * @author xx
 * @version 1
 * @createTime 2021/10/28 16:32
 * @desc 卸载残留清理
 */
class UninstallResidueTask : BaseTask() {
    companion object {
        const val TAG = "UninstallResidueTask"
    }

    private var mParser: IParser? = null
    override fun getName(): String {
        return "残留清理"
    }

    override fun scan(consumer: Consumer<AppJunk>): Boolean {
        //获取已安装app列表
        val packageInfoList: List<PackageInfo> = AppUtil.getInstalledAppInfoList(AppMod.app)
        Log.i(TAG, "scan: packageInfoList.size:${packageInfoList.size}")
        //包名集合
        val names: List<String> = packageInfoList.map { it.packageName }
        //所有未安装app数据缓存路径
        val allAppJunkList: List<JunkDbEntity>? =
            JunkDbImpl.instance.getListByExcludePackageNames(names)
        Log.i(TAG, "scan: allAppList.size:${allAppJunkList?.size}")
        if (allAppJunkList.isNullOrEmpty()) {
            Log.e(TAG, "scan error: allAppJunkList is null")
            return false
        }
        val appMap = JunkParser.getAppMap(names, allAppJunkList)
        mParser = if (RFileUtils.isAbove30()) {
            QParser()
        } else {
            CommonParser()
        }
        mParser?.parseJunk(
            appMap,
            consumer = consumer,
            JunkConstants.Session.UNINSTALL_RESIDUE
        )
        return true
    }

    override fun clean(appJunk: AppJunk): Boolean {
        return try {
            appJunk.junks?.forEach {
                FileUtils.delete(it.path)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun stop() {
        mParser?.stop()
    }
}