package com.dn.gc

import android.app.Application
import com.dn.baselib.kit.DefaultAppDelegate
import com.dn.vi.app.base.app.BaseAppDelegate
import com.dn.vi.app.base.app.BaseApplication

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

        override val logPrefix: String
            get() = "GC"


        override fun onCreateEnv(processName: String, remoteProc: Boolean) {
            super.onCreateEnv(processName, remoteProc)

        }
    }



}