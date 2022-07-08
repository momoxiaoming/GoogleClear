package com.dn.gc

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.gc.databinding.ActivityMainBinding
import com.org.openlib.utils.SystemUiUtil
import com.dn.vi.app.base.app.ViActivity
import com.mckj.datalib.entity.ARouterPath
import com.org.openlib.utils.FragmentUtil
@Route(path = "/app/main")
class MainActivity : ViActivity() {

    private lateinit var mbinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUiUtil.hideSystemUI(window)
    }
    override fun initLayout() {
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        FragmentUtil.show(supportFragmentManager, ARouterPath.Cleanup.FRAGMENT_HOME,android.R.id.content)

    }

}