package com.org.openlib.kits

import android.content.Context
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.log.VLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.atomic.AtomicBoolean

/**
 * debug 状态标识
 * Created by holmes on 2020/12/3.
 **/
object Debuger {

    class DefaultContextProvider : Callable<Context> {
        override fun call(): Context {
            return AppMod.app
        }
    }

    val DEBUG_FILE = ".debug.on"

    private val scope = CoroutineScope(Job() + Dispatchers.Default)

    private val changed = AtomicBoolean(true)

    /**
     * 强制debug开关
     * 一般只用在传入 application的 [BuildConfig.DEBUG]
     */
    var forceDebug = false

    /**
     * debug flag是否打开
     */
    var debugOn: Boolean = false
        private set
        get() {
            if (changed.get() && changed.compareAndSet(changed.get(), false)) {
                field = checkDebuging()
            }
            return field || forceDebug
        }

    var restartNeed: Boolean = false
        private set

    private var contextProvider: Callable<Context> = DefaultContextProvider()

    /**
     * context provider
     * 如果不设置，则使用 [DefaultContextProvider]从[AppMod]里面拿。
     *
     * 如果涉及初始化顺序问题的话，就自己设置
     */
    fun setContextProvider(ctxProvider: Callable<Context>) {
        contextProvider = ctxProvider
    }

    private val context: Context
        get() = contextProvider.call()

    /**
     * 设置debug开关
     *
     * 开的时候，在`/sdcard/Android/data/[pkg]/cache/.debug.on`生成这个文件。
     * 关闭则相反
     *
     * 并重启进程。
     */
    fun setDebugSwitch(debug: Boolean) {
        val cacheDir = context.externalCacheDir
        if (cacheDir == null) {
            VLog.w("can not get cache dir")
            return
        }

        scope.launch {
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }

            val f = File(cacheDir, DEBUG_FILE)
            if (debug) {
                if (!f.exists()) {
                    f.createNewFile()
                }
            } else {
                if (f.exists()) {
                    f.delete()
                }
            }

            if (changed.compareAndSet(false, true)) {
                restartNeed = true
            }
        }

    }

    private fun checkDebuging(): Boolean {
        val cacheDir = context.externalCacheDir
        if (cacheDir == null) {
            VLog.w("can not get cache dir")
            return false
        }
        if (!cacheDir.exists()) {
            return false
        }

        val f = File(cacheDir, DEBUG_FILE)
        return f.exists()
    }

    /**
     * 在debug的时候执行[block]
     */
    inline fun doIfDebug(block: () -> Unit) {
        if (debugOn) {
            block()
        }
    }

}