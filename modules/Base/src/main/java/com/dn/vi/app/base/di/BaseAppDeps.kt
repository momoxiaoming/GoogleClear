package com.dn.vi.app.base.di

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import com.dn.vi.app.base.app.AppStatus
import com.dn.vi.app.base.app.BaseAppDelegate
import com.dn.vi.app.base.image.GlideApp
import com.dn.vi.app.base.image.GlideRequests
import com.dn.vi.app.cm.utils.ThreadUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Component
import dagger.Module
import dagger.Provides
import z.hol.gq.GsonQuick
import javax.inject.Singleton

@Module
class BaseAppModule(val baseApp: BaseAppDelegate, val appStatus: AppStatus) {

    @Provides
    @Singleton
    fun provideAppDelegate(): BaseAppDelegate = baseApp

    @Provides
    @Singleton
    fun provideApp(): Application = baseApp.app

    @Provides
    @Singleton
    fun provideContext(): Context = baseApp.app

    @Provides
    @Singleton
    fun provideWorkThread(): HandlerThread {
        return ThreadUtils.getWorkHandlerThread()
    }

    @Provides
    @Singleton
    fun provideMainHandler(): Handler {
        return Handler(Looper.getMainLooper())
    }

    @Provides
    @Singleton
    fun provideAppStatus(): AppStatus = appStatus

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gson = GsonBuilder()
            .create()
        GsonQuick.setGson(gson)
        return gson
    }

    @Provides
    @Singleton
    fun provideGlide(app: Application): GlideRequests {
        return GlideApp.with(app)
    }

}

@Component(modules = [BaseAppModule::class])
@Singleton
interface BaseAppComponent {

    fun inject(app: BaseAppDelegate)

    fun getApp(): Application
    fun getAppDelegate(): BaseAppDelegate
    fun getContext(): Context
    fun getWorkThread(): HandlerThread
    fun getMainHandler(): Handler
    fun getAppStatus(): AppStatus
    fun getGson(): Gson

    /**
     * 全局的 Glide。
     * 严格生命周期控制的话，用 [GlideApp.with(xx)]
     **/
    fun getGlide(): GlideRequests

}