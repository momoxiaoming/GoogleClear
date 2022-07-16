package com.mckj.api.client.task.shallow

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
 * @createTime 2021/10/28 16:03
 * @desc 目标app的缓存信息
 */
class AppCacheTask constructor(var pkgName: String, var appName: String) : BaseTask() {

    companion object {
        const val TAG = "AppCacheTask"
    }

    private var mParser: IParser? = null

    override fun getName(): String {
        return appName
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun scan(consumer: Consumer<AppJunk>): Boolean {
        if (pkgName.isNullOrEmpty()) return false
        val appList = mutableListOf(pkgName)
        val allAppJunkList: List<JunkDbEntity>? =
            JunkDbImpl.instance.getListByPackageNames(appList)
        Log.i(TAG + pkgName, "scan: allAppList.size:${allAppJunkList?.size}")
        if (allAppJunkList.isNullOrEmpty()) {
            Log.e(TAG + pkgName, "scan error: allAppJunkList is null")
            consumer.accept(
                AppJunk(
                    appName = "",
                    packageName = "",
                    icon = null,
                    junkSize = 0,
                    junks = null,
                )
            )
            return false
        }
        mParser = if (RFileUtils.isAbove30()) {
            QParser()
        } else {
            CommonParser()
        }
        mParser?.parseJunk(
            JunkParser.getAppMap(appList, allAppJunkList),
            consumer = consumer,
            JunkConstants.AppCacheType.APP_CACHE
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