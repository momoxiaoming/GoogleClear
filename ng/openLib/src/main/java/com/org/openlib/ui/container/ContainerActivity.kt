package com.org.openlib.ui.container

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.R
import com.dn.vi.app.base.app.ViActivity
import com.dn.vi.app.base.app.kt.arouter
import com.org.openlib.path.ARouterPath
import com.org.openlib.utils.FragmentUtil
import com.org.openlib.utils.Log
import com.org.openlib.utils.SystemUiUtil

/**
 * Describe:
 *
 * Created By yangb on 2020/10/22
 */
@Route(path = ARouterPath.ACTIVITY_CONTAINER)
open class ContainerActivity : ViActivity() {

    companion object {
        const val TAG = "ContainerActivity"
    }

    /**
     * 获取容器id
     */
    open fun getContainerId(): Int = android.R.id.content

    override fun onInternalCreate(savedInstanceState: Bundle?) {
        SystemUiUtil.immersiveSystemUi(window)
        val toolbar: Toolbar? = findViewById<Toolbar>(R.id.action_bar)

    }

    override fun initLayout() {
        var fragment: Fragment? = null
        do {
            val bundle = intent.extras
            if (bundle == null) {
                Log.e(TAG, "initLayout error: bundle is null")
                break
            }
            val fragmentPath = bundle.getString(ARouterPath.EXTRA_FRAGMENT_PATH)
            if (fragmentPath == null) {
                Log.e(TAG, "initData error: fragmentPath is null")
                break
            }
            Log.i(TAG, "initData: fragment Path:$fragmentPath")
            bundle.remove(ARouterPath.EXTRA_FRAGMENT_PATH)
            fragment = arouter().build(fragmentPath).with(bundle).navigation() as? Fragment
        } while (false)
        Log.i(TAG, "initData: fragment$fragment")
        if (fragment != null) {
            FragmentUtil.show(supportFragmentManager, fragment, getContainerId())
        } else {
            popBack()
        }
    }

    internal fun popBack() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

}