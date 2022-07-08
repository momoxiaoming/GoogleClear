package com.mckj.baselib.view.state

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.drakeet.purewriter.ObscureLifecycleEventObserver
import com.drakeet.purewriter.addObserver
import com.drakeet.purewriter.removeObscureObserver
import java.util.concurrent.TimeUnit

/**
 * State 扩展函数
 *
 * @author mmxm
 * @date 2021/6/29 11:11
 */

/**
 * 将通过代码方式注入[StateLayout]
 */
fun View.state(): StateLayout {
    if (parent is StateLayout) {
        return parent as StateLayout
    }
    if (parent is ViewPager || parent is RecyclerView) {
        throw UnsupportedOperationException("You should using StateLayout wrap [ $this ] in layout when parent is ViewPager or RecyclerView")
    }
    val parentView = parent as ViewGroup
    val index = parentView.indexOfChild(this)
    val targetPrams = layoutParams
    val stateLayout = StateLayout(context)
    stateLayout.id = id
    parentView.removeView(this)
    parentView.addView(stateLayout, index, targetPrams)
    stateLayout.addView(
        this,
        ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    )
    stateLayout.setContent(this)
    return stateLayout
}


fun Activity.state(): StateLayout {
    val v = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
    return v.state()
}

fun Fragment.state(): StateLayout {
    val stateLayout = view!!.state()
    //lifecycle.addObserver(object : LifecycleObserver {
      //  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        //fun removeState() {
          //  val parent = stateLayout.parent as ViewGroup
            //parent.removeView(stateLayout)
            //lifecycle.removeObserver(this)
        //}
    //})
    lifecycle.addObserver(object: ObscureLifecycleEventObserver{
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                val parent = stateLayout.parent as ViewGroup
                parent.removeView(stateLayout)
                lifecycle.removeObscureObserver(this)
            }
        }

    })
    return stateLayout
}

fun View.setText(@IdRes id: Int, msg: String) {
    try {
        findViewById<TextView>(id)?.let {
            it.text = msg
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Activity.setText(@IdRes id: Int, msg: String) {
    try {
        findViewById<TextView>(id)?.let {
            it.text = msg
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun <T : View> View.findView(@IdRes id: Int): T? {
    return try {
        findViewById<T>(id)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * 防抖动
 */
fun View.tingleClick(
    interval: Long = 500,
    unit: TimeUnit = TimeUnit.MILLISECONDS,
    block: View.() -> Unit
) {
    setOnClickListener(ThrottleClickListener(interval, unit, block))
}

class ThrottleClickListener(
    private val interval: Long = 500,
    private val unit: TimeUnit = TimeUnit.MILLISECONDS,
    private var block: View.() -> Unit
) : View.OnClickListener {
    private var lastTime: Long = 0
    override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastTime > unit.toMillis(interval)) {
            lastTime = currentTime
            block(v)
        }
    }
}

/**
 * 主线程运行代码块
 * @param block Function0<Unit>
 */
inline fun runMain(crossinline block: () -> Unit) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        block()
    } else {
        Handler(Looper.getMainLooper()).post { block() }
    }
}
/**
 * 判断是否有网络连接
 */
fun Context?.isNetConnected(): Boolean {
    if (this != null) {
        val mConnectivityManager = getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val mNetworkInfo = mConnectivityManager.activeNetworkInfo
        if (mNetworkInfo != null) {
            @Suppress("DEPRECATION")
            return mNetworkInfo.isAvailable
        }
    }
    return false
}
fun Context.inflate(@LayoutRes resource: Int, root: ViewGroup, attachToRoot: Boolean): View {
    return LayoutInflater.from(this).inflate(resource, root, attachToRoot)
}


