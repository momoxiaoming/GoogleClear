package com.dn.vi.app.base.app.callback

import android.app.Application
import android.content.Context

/**
 * Describe:用于监听Application生命周期，一般用于库的初始化,切勿执行耗时操作
 *
 * 使用步骤
 * 1.在己方库中实现IAppCallback接口
 * 2.在清单文件注册事件MetaData(value=@string/AppCallback name=实现类路径)
 *
 * Created By yangb on 2020/12/18
 */
interface IAppCallback {
    /**
     * 优先级，数字越大,优先级越高
     */
    fun getPriority() : Int

    fun attachBaseContext(application: Application, context: Context)

    fun onCreate(application: Application)

    fun onTerminate(application: Application)

    fun onLowMemory(application: Application)

    fun onTrimMemory(application: Application, level: Int)

}