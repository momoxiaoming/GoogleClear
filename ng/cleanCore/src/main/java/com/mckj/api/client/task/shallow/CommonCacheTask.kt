package com.mckj.api.client.task.shallow

import android.content.pm.PackageInfo
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
 * @author leix
 * @version 1
 * @createTime 2021/10/28 15:50
 * @desc 应用缓存垃圾扫描/清理
 */
class CommonCacheTask : BaseTask() {

    companion object {
        const val TAG = "CommonCacheTask"
    }

    private var mParser: IParser? = null
    override fun getName(): String {
        return "应用缓存"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun scan(consumer: Consumer<AppJunk>): Boolean {
        //获取已安装app列表
        val packageInfoList: List<PackageInfo> = AppUtil.getInstalledAppInfoList(AppMod.app)
        //包名集合
        val names: List<String> = packageInfoList.map { it.packageName }
        //所有安装app数据缓存路径
        val allAppJunkList: List<JunkDbEntity>? =
            JunkDbImpl.instance.getListByPackageNames(names)
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
        mParser?.parseJunk(appMap, consumer = consumer,JunkConstants.Session.APP_CACHE)
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