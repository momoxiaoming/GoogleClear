package com.dn.gc.ui.splash

import android.content.Intent
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dn.gc.MainActivity
import com.dn.gc.R
import com.dn.gc.databinding.ActivitySplashBinding
import com.dn.openlib.ui.splash.SimpleStartupActivity
import com.dn.openlib.utils.SystemUiUtil
import com.dn.vi.app.cm.log.VLog

/**
 * SplashActivity
 *
 * @author mmxm
 * @date 2022/1/5 14:56
 */
class SplashActivity:SimpleStartupActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val mSplashModel by lazy {
        ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    override fun initLayout() {
        super.initLayout()
        binding =
            DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        SystemUiUtil.hideSystemUI(window)
        mSplashModel.mLoadTimeLiveData.observe(this) {
            binding.splashProgress.progress = it.toInt()
            Log.i("GC","---->${it.toInt()}")
            if(it.toInt()>=90){
                startMain()
            }
        }

    }

    override fun redirectToMain() {
        mSplashModel.loadTimeout(3000)
    }

    fun startMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onFinishAction() {

    }
}