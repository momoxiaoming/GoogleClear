package com.dn.vi.app.base.image

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.dn.vi.app.base.BuildConfig
import com.dn.vi.app.base.image.loader.AppIconLoader
import com.dn.vi.app.base.image.loader.AppIconReq
import com.dn.vi.app.base.image.loader.ZipEntryLoader
import com.dn.vi.app.base.image.loader.ZipPic
import java.io.InputStream

/**
 * 自定义的加载Glide。
 *
 *
 * Created by holmes on 17-6-20.
 */
@GlideModule
class ImageLoaderGlideModule : AppGlideModule() {

    companion object {
        /**
         * 外部可以传入的一个glide module 代理
         */
        @JvmStatic
        var glideModuleInject: AppGlideModule? = null
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // 注册 全局加载器
        val appContext = context.applicationContext
        registry.append(ZipPic::class.java, InputStream::class.java, ZipEntryLoader.Factory())
        registry.append(
            AppIconReq::class.java,
            Drawable::class.java,
            AppIconLoader.Factory(appContext)
        )
        //如果版本小于或等于6.0时,添加HTTPS证书信任
        //否则在小于6.0以下版本加载证书图片时会加载失败
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            registry.replace(
                GlideUrl::class.java, InputStream::class.java,
                OkHttpUrlLoader.Factory(HttpUtils.getSSLOkHttpClient())
            )
        }

        glideModuleInject?.registerComponents(context, glide, registry)
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        glideModuleInject?.applyOptions(context, builder)
    }

}