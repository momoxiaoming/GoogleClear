package com.dn.vi.app.base.image.loader

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey

/**
 * 通过glide 加载安装或apk的icon
 * Created by holmes on 2020/6/17.
 **/
class AppIconLoader(context: Context) : ModelLoader<AppIconReq, Drawable> {

    private val appContext: Context = context.applicationContext

    override fun buildLoadData(
        model: AppIconReq,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<Drawable>? {

        if (model.appInfo != null) {
            return ModelLoader.LoadData(
                ObjectKey(model),
                AppIconFetcher.InstalledFromAppInfo(appContext, model)
            )
        }

        if (model.apkFilepath.isNotEmpty()) {
            return ModelLoader.LoadData(
                ObjectKey(model),
                AppIconFetcher.StandaloneApkFile(appContext, model)
            )
        }

        // default
        return ModelLoader.LoadData(
            ObjectKey(model),
            AppIconFetcher.Installed(appContext, model)
        )
    }

    override fun handles(model: AppIconReq): Boolean {
        return true
    }

    private sealed class AppIconFetcher(
        protected val context: Context,
        protected val model: AppIconReq
    ) :
        DataFetcher<Drawable> {

        override fun getDataClass(): Class<Drawable> {
            return Drawable::class.java
        }

        override fun cleanup() {
        }

        override fun getDataSource(): DataSource {
            return DataSource.LOCAL
        }

        override fun cancel() {
        }

        override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in Drawable>) {
            if (model.packageName.isNullOrEmpty()) {
                callback.onLoadFailed(Resources.NotFoundException("no app found with empty packageName"))
                return
            }
            val pm = context.packageManager
            val appInfo = try {
                pm.getApplicationInfo(model.packageName, 0)
            } catch (e: Exception) {
                callback.onLoadFailed(e)
                return
            }

            val icon = try {
                pm.getApplicationIcon(appInfo)
            } catch (e: Exception) {
                callback.onLoadFailed(e)
                return
            }
            if (icon != null) {
                callback.onDataReady(icon)
            } else {
                callback.onLoadFailed(RuntimeException("get a null drawable icon"))
            }
        }


        /**
         * 安装的应用
         */
        class Installed(context: Context, model: AppIconReq) :
            AppIconFetcher(context, model) {

        }

        /**
         * 安装的应用
         */
        class InstalledFromAppInfo(context: Context, model: AppIconReq) :
            AppIconFetcher(context, model) {

            override fun loadData(
                priority: Priority,
                callback: DataFetcher.DataCallback<in Drawable>
            ) {
                val pm = context.packageManager
                val appInfo = model.appInfo

                val icon = try {
                    pm.getApplicationIcon(appInfo!!)
                } catch (e: Exception) {
                    callback.onLoadFailed(e)
                    return
                }
                if (icon != null) {
                    callback.onDataReady(icon)
                } else {
                    callback.onLoadFailed(RuntimeException("get a null drawable icon"))
                }
            }
        }

        /**
         * apk安装包
         */
        class StandaloneApkFile(context: Context, model: AppIconReq) :
            AppIconFetcher(context, model) {

            override fun loadData(
                priority: Priority,
                callback: DataFetcher.DataCallback<in Drawable>
            ) {
                val filepath = model.apkFilepath
                val pm = context.packageManager
                val archivePackage = try {
                    pm.getPackageArchiveInfo(filepath, 0)
                } catch (e: Exception) {
                    callback.onLoadFailed(e)
                    return
                }

                val appInfo = archivePackage!!.applicationInfo
                if (appInfo == null) {
                    callback.onLoadFailed(RuntimeException("Can not parser app info for: $filepath"))
                    return
                }
                appInfo.sourceDir = filepath
                appInfo.publicSourceDir = filepath

                val icon = try {
                    pm.getApplicationIcon(appInfo)
                } catch (e: Exception) {
                    callback.onLoadFailed(e)
                    return
                }
                if (icon != null) {
                    callback.onDataReady(icon)
                } else {
                    callback.onLoadFailed(RuntimeException("get a null drawable icon"))
                }
            }

        }

    }


    class Factory(private val appContext: Context) : ModelLoaderFactory<AppIconReq, Drawable> {

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<AppIconReq, Drawable> {
            return AppIconLoader(appContext)
        }

        override fun teardown() {
        }
    }

}