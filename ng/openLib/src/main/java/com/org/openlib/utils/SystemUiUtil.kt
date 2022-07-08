package com.org.openlib.utils

import android.app.KeyguardManager
import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

/**
 * Describe: 系统ui工具
 *
 * Created By yangb on 2020/12/31
 */
object SystemUiUtil {

    /**
     * 沉浸式效果
     */
    fun immersiveSystemUi(window: Window): SystemUiUtil {
        setTransparentForWindow(window)
        supportDisplayCutoutMode(window)
        return this
    }

    /**
     * 隐藏系统ui
     */
    fun hideSystemUI(window: Window): SystemUiUtil {
        window.decorView.systemUiVisibility = (
                //粘性沉浸模式，用户从隐藏系统栏位置调出后，无操作后会自动隐藏
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        //隐藏状态栏
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        //隐藏导航栏
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
        supportDisplayCutoutMode(window)
        return this
    }

    /**
     * 显示系统ui
     */
    fun showSystemUI(window: Window): SystemUiUtil {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        supportDisplayCutoutMode(window)
        return this
    }

    /**
     * 设置状态栏颜色
     */
    fun setStatusBarColor(window: Window, color: Int): SystemUiUtil {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = color
        }
        return this
    }

    /**
     * 设置导航栏颜色
     */
    fun setNavigationBarColor(window: Window, color: Int): SystemUiUtil {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = color
        }
        return this
    }

    /**
     * 设置状态栏，导航栏颜色
     */
    fun setBarColor(window: Window, color: Int): SystemUiUtil {
        setStatusBarColor(window, color)
        setNavigationBarColor(window, color)
        return this
    }

    /**
     * 设置透明
     */
    private fun setTransparentForWindow(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }
    }

    /**
     * 支持刘海屏模式
     */
    private fun supportDisplayCutoutMode(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val attributes = window.attributes
            attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = attributes
        }
    }

    /**
     * 设置标题栏控件
     */
    fun setTitleBar(context: Context, view: View): SystemUiUtil {
        val statusBarHeight = getStatusBarHeight(context)
        val layoutParams = view.layoutParams
        if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT || layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            view.post {
                layoutParams.height = view.height + statusBarHeight
                view.setPadding(
                    view.paddingLeft,
                    view.paddingTop + statusBarHeight,
                    view.paddingRight,
                    view.paddingBottom
                )
                view.layoutParams = layoutParams
            }
        } else {
            layoutParams.height = view.height + statusBarHeight
            view.setPadding(
                view.paddingLeft,
                view.paddingTop + statusBarHeight,
                view.paddingRight,
                view.paddingBottom
            )
            view.layoutParams = layoutParams
        }
        return this
    }

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        val result = 0
        try {
            val resourceId =
                Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                val sizeOne = context.resources.getDimensionPixelSize(resourceId)
                val sizeTwo = Resources.getSystem().getDimensionPixelSize(resourceId)
                return if (sizeTwo >= sizeOne) {
                    sizeTwo
                } else {
                    val densityOne = context.resources.displayMetrics.density
                    val densityTwo = Resources.getSystem().displayMetrics.density
                    (sizeOne * densityTwo / densityOne).roundToInt()
                }
            }
        } catch (ignored: NotFoundException) {
            return 0
        }
        return result
    }

    /**
     * 是否处于锁屏状态
     */
    fun isLock(context: Context): Boolean {
        val keyguardManager =
            context.getSystemService(AppCompatActivity.KEYGUARD_SERVICE) as? KeyguardManager
        return keyguardManager?.isKeyguardLocked ?: false
    }

    /**
     * 将信号栏文字设置成黑色
     * @param isDark 是否设置成黑色文字
     * false：白色文字
     */
    fun darkSystemUiText(window: Window, isDark: Boolean = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isDark) {
                setSystemUiFlag(window, View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            } else {
                unsetSystemUiFlag(window, View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            }
        }
    }
    /**
     * 向系统UI插入一个标志
     * @param window 窗口
     * @param systemUiFlag 系统UI标志
     *
     * 例如亮色状态栏黑色文字 flag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
     */
    fun setSystemUiFlag(window: Window, systemUiFlag: Int) {
        val decorView: View = window.decorView
        decorView.systemUiVisibility = (
                decorView.systemUiVisibility
                        or systemUiFlag)
    }

    /**
     * 向系统UI删除一个标志
     * @param window 窗口
     * @param systemUiFlag 系统UI标志
     *
     * 例如取消亮色状态栏黑色文字 flag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
     */
    fun unsetSystemUiFlag(window: Window, systemUiFlag: Int) {
        val decorView: View = window.decorView
        decorView.systemUiVisibility = (
                decorView.systemUiVisibility
                        and systemUiFlag.inv())
    }
}