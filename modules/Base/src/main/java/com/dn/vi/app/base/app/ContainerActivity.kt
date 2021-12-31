package com.dn.vi.app.base.app

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.R
import com.dn.vi.app.base.app.kt.getAs
import com.dn.vi.app.base.app.kt.transportData
import com.dn.vi.app.cm.kt.printTimeMillis

/**
 * 一个BaseFragment容器
 *
 * Created by holmes on 2020/5/21.
 **/
@Route(path = "/base/page/container")
open class ContainerActivity : ViActivity() {

    companion object {
        /**
         * 外部传入。
         * 或者 在 Meta-Data里面指定.
         *
         * 是个[Fragment]实例
         */
        const val EXTRA_FRAGMENT = "Contain:Fragment"

        /**
         * Fragment的class Name
         * 可用反射，getInstance()
         */
        const val EXTRA_FRAGMENT_CLASS = "Contain:Fragment:Cls"

    }

    var placeHolderId: Int = android.R.id.content
        protected set

    protected var currentFragment: Fragment? = null
        private set

    override fun initLayout() {
    }

    override fun beforeSetLayout(): Boolean {
        ensureFragment()
        return currentFragment != null
    }

    private fun ensureFragment() {
        // dump from data share
        transportData {
            currentFragment = getAs<Fragment>(EXTRA_FRAGMENT)
        }

        // dump from MetaData declaration
        if (currentFragment == null) {
            // get from meta-data
            printTimeMillis("CreateFragment") {
                val pm = packageManager
                val activityInfo = try {
                    pm.getActivityInfo(
                        ComponentName(this, this::class.java),
                        PackageManager.GET_META_DATA
                    )
                } catch (e: Exception) {
                    null
                }

                val fragmentClassName = activityInfo?.metaData?.getString(EXTRA_FRAGMENT)
                if (!fragmentClassName.isNullOrEmpty()) {
                    // create a instance
                    val createdFragment = try {
                        Class.forName(fragmentClassName)?.getConstructor()
                            ?.newInstance() as? Fragment
                    } catch (e: Exception) {
                        null
                    }
                    currentFragment = createdFragment
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val toolbar: Toolbar? = findViewById<Toolbar>(R.id.action_bar)
        toolbar?.setNavigationOnClickListener {
            popBack()
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                popBack()
            }
        })

        if (!willSetLayout) {
            goBack()
        } else {
            currentFragment?.also { fragment ->
                supportFragmentManager.commit {
                    replace(placeHolderId, fragment, fragment::class.java.name)
                }
            }
        }
    }

    private fun popBack(){
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

}