package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.notify

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dn.vi.app.base.app.AppMod
import com.google.gson.reflect.TypeToken
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.AppInfo
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.GsonHelper
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.KvSpHelper
import kotlinx.coroutines.*

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.wjm.util.main.notify.bean
 * @data  2022/3/1 15:54
 */
object AppStateProvider {

    //com.tencent.mm  com.tencent.mobileqq
    private const val SP_APP_STATE_INFO = "SP_APP_STATE_INFO"
    private val defaultAppList = arrayListOf("com.tencent.mm1", "com.tencent.mobileqq1")
    val appStateLiveData by lazy { MutableLiveData<MutableList<AppInfo>>() }
    private val iconAppMap = hashMapOf<String,Drawable>()
    private var protectedAppMap= hashMapOf<String, String>()
    private val appStateList = arrayListOf<AppInfo>()
    private val filterAppMap = hashMapOf<String,String>()

    private val  job by lazy {  Job() }
    private val scope by lazy { CoroutineScope(job) }



    fun initSourceData(){
        scope.launch (Dispatchers.Main){
            withContext(Dispatchers.Default){
                appStateList.clear()
                appStateList.addAll( getInstalledAppInfoList(AppMod.app))
            }
            val appStateStr = KvSpHelper.getString(SP_APP_STATE_INFO)
            if (appStateStr != null) {
                val type = object : TypeToken<HashMap<String, String>>() {}.type
                GsonHelper.jsonToObj<HashMap<String, String>>(appStateStr, type)?.let { protectedMap ->
                    protectedAppMap =protectedMap
                    for (appInfo in appStateList) {
                        val packageName = appInfo.packageName
                        val protectedApp = protectedMap[packageName]
                        if (!protectedApp.isNullOrEmpty()) {
                            appInfo.isFilter=false
                            continue
                        }
                        if (appInfo.isFilter){
                            filterAppMap[packageName] = packageName
                        }
                    }
                }
            } else {
                for (appInfo in appStateList) {
                    val packageName = appInfo.packageName
                    if (packageName == defaultAppList[0] || packageName == defaultAppList[1]) {
                        appInfo.isFilter = false
                        protectedAppMap[packageName] = packageName
                        continue
                    }
                    if (appInfo.isFilter){
                        filterAppMap[packageName] = packageName
                    }
                }
            }
            appStateList.sortWith(compareBy({ it.isFilter},{it.appName})  )
            appStateLiveData.postValue(appStateList)
        }
    }

    fun getProtectedAppMap() = protectedAppMap

    fun getIconAppMap() = iconAppMap

    fun getFilterAppMap() = filterAppMap

    fun saveAppState(appInfo: AppInfo) {
        val packageName = appInfo.packageName
        if (appInfo.isFilter) {
            filterAppMap[packageName] = packageName
            protectedAppMap.remove(packageName)
        } else {
            filterAppMap.remove(packageName)
            protectedAppMap[packageName] = packageName
        }
        KvSpHelper.putString(SP_APP_STATE_INFO, GsonHelper.toJson(protectedAppMap))
    }

    /**
     * 获取手机已安装应用信息
     *
     * @param context 上下文
     * @param containSelf 是否包含自己应用
     * @param containSystemApp 是否包含系统应用
     */
    private fun getInstalledAppInfoList(
        context: Context,
        containSelf: Boolean = false,
        containSystemApp: Boolean = false,
    ): MutableList<AppInfo> {
        val infoList = arrayListOf<AppInfo>()
        val pm = context.packageManager
        val installedPackages = pm.getInstalledPackages(0)
        for (pi in installedPackages) {
            val applicationInfo = pi?.applicationInfo ?: continue
            if (!containSelf && pi.packageName == context.packageName) {
                continue
            }
            if (((ApplicationInfo.FLAG_SYSTEM and applicationInfo.flags!= 0) and !containSystemApp )) {
                continue
            }
            val appName = pi.applicationInfo.loadLabel(pm).toString()
            val packageName = pi.applicationInfo.packageName
            val loadIcon = pi.applicationInfo.loadIcon(pm)
            infoList.add(AppInfo(packageName,appName,loadIcon))
            Log.i("888","installedAppInfoList----${appName}-------${packageName}----${loadIcon}")
            iconAppMap[packageName] = loadIcon
        }
        return   infoList
    }

}