package com.dn.gc

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.gc.databinding.ActivityMainBinding
import com.org.openlib.utils.SystemUiUtil
import com.dn.vi.app.base.app.ViActivity
import com.dn.vi.app.cm.log.VLog
import com.dn.vi.app.scaffold.MainPager
import com.mckj.datalib.entity.ARouterPath
import com.org.admodule.type.splash.SplashManager
import com.org.openlib.utils.FragmentUtil

@Route(path = com.org.openlib.path.ARouterPath.EXTRA_APP_MAIN)
class MainActivity : ViActivity(),MainPager {

    private lateinit var mbinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUiUtil.hideSystemUI(window)

    }
    override fun initLayout() {
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        FragmentUtil.show(supportFragmentManager, ARouterPath.Cleanup.FRAGMENT_HOME,android.R.id.content)
        SplashManager.show(this,mbinding.splashAdContainer)
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        VLog.i("onNewIntent----------")
        SplashManager.show(this,mbinding.splashAdContainer)
    }
    override fun onResume() {
        super.onResume()
    }
}