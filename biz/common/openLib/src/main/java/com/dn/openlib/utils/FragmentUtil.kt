package com.dn.openlib.utils

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.dn.vi.app.base.app.kt.arouter

/**
 * Describe:
 *
 * Created By yangb on 2020/11/18
 */
object FragmentUtil {

    const val TAG = "FragmentUtil"

    fun show(fragmentManager: FragmentManager, path: String, resId: Int) {
        val fragment =
            arouter().build(path).navigation() as? Fragment
        if (fragment != null) {
            show(fragmentManager, fragment, resId)
        }
    }

    fun show(fragmentManager: FragmentManager, fragment: Fragment, resId: Int, tag: String = "") {
        Log.i(TAG, "show: fragment:$fragment resId:$resId")
        show(fragmentManager, fragment, resId, tag, 0 , 0)
    }

    fun show(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        resId: Int,
        tag: String = "",
        animEnterResId: Int = 0,
        animExitResId: Int = 0
    ) {
        Log.i(TAG, "show: fragment:$fragment resId:$resId")
        val ft = fragmentManager.beginTransaction()
        if (fragment.isAdded) {
            ft.show(fragment)
        } else {
            if (tag.isEmpty()) {
                ft.replace(resId, fragment)
            } else {
                ft.replace(resId, fragment, tag)
            }
        }
        if (animEnterResId != 0 && animExitResId != 0) {
            ft.setCustomAnimations(animEnterResId, animExitResId)
        }
        ft.commitAllowingStateLoss()
    }

    fun remove(fragmentManager: FragmentManager, fragment: Fragment) {
        Log.i(TAG, "remove: fragment:$fragment")
        val ft = fragmentManager.beginTransaction()
        if (fragment.isAdded) {
            ft.remove(fragment)
            ft.commitAllowingStateLoss()
        }
    }

}