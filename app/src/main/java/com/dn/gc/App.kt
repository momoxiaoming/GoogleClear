package com.dn.gc

import android.app.Application
import com.dn.vi.app.base.app.BaseAppDelegate
import com.dn.vi.app.base.app.BaseApplication
import com.org.openlib.kits.DefaultAppDelegate

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

    }



}