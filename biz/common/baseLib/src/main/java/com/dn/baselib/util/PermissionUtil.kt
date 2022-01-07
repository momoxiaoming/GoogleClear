package com.dn.baselib.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tbruyelle.rxpermissions3.RxPermissions

/**
 * Describe:
 *
 * Created By yangb on 2020/12/15
 */
object PermissionUtil {

    /**
     * 申请权限
     */
    fun requestPermission(
        activity: FragmentActivity,
        vararg permissions: String,
        block: (Boolean) -> Unit
    ) {
        RxPermissions(activity)
            .request(*permissions)
            .subscribe {
                block(it)
            }
    }

    /**
     * 申请权限
     */
    fun requestPermission(
        fragment: Fragment,
        vararg permissions: String,
        block: (Boolean) -> Unit
    ) {
        RxPermissions(fragment)
            .request(*permissions)
            .subscribe {
                block(it)
            }
    }

    /**
     * 能否显示权限申请
     */
    fun shouldShowRequestPermissionRationale(
        activity: FragmentActivity,
        vararg permissions: String,
        block: (Boolean) -> Unit
    ) {
        RxPermissions(activity)
            .shouldShowRequestPermissionRationale(activity, *permissions)
            .subscribe {
                block(it)
            }
    }

}