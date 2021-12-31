package com.dn.vi.app.base.app

import android.os.Bundle
import androidx.fragment.app.ExposeXApp
import com.dn.vi.app.base.arch.gmvp.LifecycleView
import com.dn.vi.app.base.lifecycle.RxLifecycleDelegate

/**
 * 对base的进一步扩展
 * Created by holmes on 2020/5/21.
 **/
abstract class ViActivity : BaseActivity(), LifecycleView {

    private val rxLifecycle: RxLifecycleDelegate by lazy { RxLifecycleDelegate(this) }

    /**
     * 直接只关联 Destroy
     *
     * [RxLifecycleDelegate]
     */
    override fun <T> bindUntilDestroy(): RxLifecycleDelegate.LifecycleTransformer<T> {
        return rxLifecycle.bindUntilDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // remove saved fragment!
        // preventing the state restored for fragments
        ExposeXApp.removeSaveInstance(outState)
    }

}