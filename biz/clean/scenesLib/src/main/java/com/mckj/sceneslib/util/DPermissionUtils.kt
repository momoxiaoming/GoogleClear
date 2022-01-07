package com.mckj.sceneslib.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.dn.baselib.util.LocationUtil
import com.tbruyelle.rxpermissions3.RxPermissions

/**
 * @author leix
 * @version 1
 * @createTime 2021/11/29 16:50
 * @desc
 */
object DPermissionUtils {

    /**
     * 是否有sd卡的读写权限
     */
    private fun hasStoragePermission(activity: FragmentActivity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    }

    /**
     * 申请sd卡权限
     */
    fun requestStoragePermission(activity: FragmentActivity, block: (accept: Boolean) -> Unit) {
        val rxPermissions = RxPermissions(activity)
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe { result ->
                if (result) {
                    //申请成功,刷新数据
                    block.invoke(true)
                } else {
                    block.invoke(false)
                    //权限被拒绝 且禁止询问
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    ) {
                        //不能否再次请求
                        LocationUtil.openSelfSetting()
                    }
                }
            }
    }

    fun checkStoragePermission(activity: FragmentActivity, block: (accept: Boolean) -> Unit) {
        if (hasStoragePermission(activity)) {
            block.invoke(true)
            return
        }
        requestStoragePermission(activity, block)
    }

}