package com.dn.vi.app.base.app

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * ActivityLifecycle 的空实现
 *
 * 免得临时用的时候，重写一堆方法
 *
 * Created by holmes on 2020/12/2.
 **/
open class ActivityLifecycleCallbackAdapter : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}
