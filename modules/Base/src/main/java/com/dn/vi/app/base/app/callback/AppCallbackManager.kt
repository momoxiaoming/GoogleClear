package com.dn.vi.app.base.app.callback

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.dn.vi.app.base.R
import com.dn.vi.app.cm.kt.printTimeMillisSysLog

/**
 * Describe:Application回调管理类
 *
 * Created By yangb on 2020/12/18
 */
class AppCallbackManager {

    companion object {
        const val TAG = "AppCb"

        private val INSTANCE by lazy { AppCallbackManager() }

        fun getInstance(): AppCallbackManager = INSTANCE

    }

    /**
     * IAppCallback 实现类集合
     */
    private var list: List<IAppCallback>? = null

    fun attachBaseContext(application: Application, context: Context) {
        printTimeMillisSysLog("AppCb, list build") {
            list = getInitializerList(context)
        }
        list?.forEach {
            printTimeMillisSysLog("AppCb, it attached (${it})") {
                it.attachBaseContext(application, context)
            }
        }
    }

    fun onCreate(application: Application) {
        list?.forEach {
            printTimeMillisSysLog("AppCb, it created (${it})") {
                it.onCreate(application)
            }
        }
    }

    fun onTerminate(application: Application) {
        list?.forEach {
            printTimeMillisSysLog("AppCb, it terminated (${it})") {
                it.onTerminate(application)
            }
        }
    }

    fun onLowMemory(application: Application) {
        list?.forEach {
            printTimeMillisSysLog("AppCb, it lowMemory (${it})") {
                it.onLowMemory(application)
            }
        }
    }

    fun onTrimMemory(application: Application, level: Int) {
        list?.forEach {
            printTimeMillisSysLog("AppCb, it trimMemory (${it})") {
                it.onTrimMemory(application, level)
            }
        }
    }

    private fun getInitializerList(context: Context): List<IAppCallback>? {
        var list: List<IAppCallback>? = null
        do {
            try {
                val keyList = getInitializerKeyList(context)
                if (keyList == null) {
                    Log.i(TAG, "getInitializerList error: keyList is null")
                    break
                }
                list = mutableListOf()
                for (item in keyList) {
                    val initializer = getInitializer(item) ?: continue
                    list.add(initializer)
                }
                if (list.isNotEmpty()) {
                    list = list.sortedByDescending {
                        it.getPriority()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } while (false)
        return list
    }

    /**
     * 获取初始化实现类
     *
     * @param name 实现类的路径
     */
    private fun getInitializer(name: String): IAppCallback? {
        var iAppCallback: IAppCallback? = null
        try {
            do {
                val clazz = Class.forName(name)
                if (IAppCallback::class.java.isAssignableFrom(clazz)) {
                    iAppCallback = clazz.getDeclaredConstructor().newInstance() as? IAppCallback
                }
            } while (false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return iAppCallback
    }

    /**
     * 获取初始化key列表
     */
    private fun getInitializerKeyList(context: Context): List<String>? {
        var list: List<String>? = null
        try {
            do {
                val applicationInfo = context.packageManager.getApplicationInfo(
                    context.packageName,
                    PackageManager.GET_META_DATA
                )
                val bundle = applicationInfo.metaData
                if (bundle == null) {
                    Log.i(TAG, "getInitList error: bundle is null")
                    break
                }
                val key = context.getString(R.string.AppCallback)
                list = mutableListOf<String>()
                val keys: Set<String> = bundle.keySet()
                for (item in keys) {
                    val value: String = bundle.get(item)?.toString() ?: ""
                    if (value == key) {
                        list.add(item)
                    }
                }
            } while (false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

}