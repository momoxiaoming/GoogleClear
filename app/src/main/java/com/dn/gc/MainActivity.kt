package com.dn.gc

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dn.datalib.ARouterPath
import com.dn.gc.databinding.ActivityMainBinding
import com.dn.openlib.OpenLibARouterPath
import com.dn.openlib.utils.FragmentUtil
import com.dn.openlib.utils.SystemUiUtil
import com.dn.vi.app.base.app.ViActivity
import com.dn.vi.app.base.app.kt.arouter

class MainActivity : ViActivity() {

    private lateinit var mbinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUiUtil.hideSystemUI(window)
    }
    override fun initLayout() {
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        FragmentUtil.show(supportFragmentManager,ARouterPath.Cleanup.FRAGMENT_HOME,android.R.id.content)
    }

}