package com.org.openlib.utils

import android.app.ActivityManager
import com.dn.vi.app.base.app.AppMod
import com.org.openlib.utils.ProcessUtil.getActivityManager

/**
 * AppUtils
 *
 * @author mmxm
 * @date 2022/7/21 11:02
 */
object AppUtils {

    /**
     * 判断本应用是否已经位于最前端
     *
     * @return 本应用已经位于最前端时，返回 true；否则返回 false
     */
    fun isRunningForeground(): Boolean {
        val context = AppMod.app
        val activityManager = getActivityManager()
        val appProcessInfoList = activityManager.runningAppProcesses ?: return false
        for (appProcessInfo in appProcessInfoList) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName == context.applicationInfo.processName) {
                    return true
                }
            }
        }
        return false
    }
}