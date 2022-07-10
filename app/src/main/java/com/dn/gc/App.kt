package com.dn.gc

import android.app.Application
import com.dn.vi.app.base.app.BaseAppDelegate
import com.dn.vi.app.base.app.BaseApplication
import com.org.openlib.kits.DefaultAppDelegate
import android.webkit.WebView

import android.os.Build




/**
 * App
 *
 * @author mmxm
 * @date 2022/1/4 14:18
 */
class App : BaseApplication() {
    override fun createAppDelegate(): BaseAppDelegate {
        return AppDelegate(this)
    }


    private class AppDelegate(app: Application) : DefaultAppDelegate(app) {
        override fun onCreateEnv(processName: String, remoteProc: Boolean) {
            //Android 9及以上必须设置
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val processName: String = getProcessName()
                if (!(app.getPackageName() == processName)) {
                    WebView.setDataDirectorySuffix(processName)
                }
            }
            super.onCreateEnv(processName, remoteProc)

        }
    }



}