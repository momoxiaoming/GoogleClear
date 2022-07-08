package com.mckj.sceneslib.permission

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.mckj.api.util.RFileUtils
import com.mckj.baselib.util.ResourceUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.gen.St
import com.tbruyelle.rxpermissions3.RxPermissions
import java.util.function.Consumer

/**
 * @author leix
 * @version 1
 * @createTime 2021/11/29 16:50
 * @desc 权限申请类
 */
object DPermissionUtils {

    /**
     * 请求文件扫描权限
     * >30 获取授权权限   <30 读写权限
     */
    @SuppressLint("NewApi")
    fun requestFileScanPermission(
        context: Context,
        from: String = "",
        consumer: Consumer<Boolean>
    ) {
        St.stPermissionSysDialogShow(what = "手机读写权限", from = "启动页-读写")
        if (RFileUtils.isAbove30()) {
            if (RFileUtils.isGrantAndroidRAndroidDataAccessPermission(context)) {
                consumer.accept(true)

            } else {
                RFileUtils.checkExStoragePermission(context, from) {
                    consumer.accept(it)
                    St.stPermissionSysDialogDismiss(what = "手机读写权限", type = "允许")
                }
            }
        } else {
            if (context !is FragmentActivity) {
                return
            }
            checkStoragePermission(context) {
                consumer.accept(it)
            }
        }
    }


    /**
     * 是否有文件扫描权限
     * * >30 获取授权权限   <30 读写权限
     */
    fun hasFileScanPermission(context: Context): Boolean {
        return if (RFileUtils.isAbove30()) {
            RFileUtils.isGrantAndroidRAndroidDataAccessPermission(context)
        } else {
            hasStoragePermission(context)
        }
    }

    /**
     *读写权限
     */
    fun hasStoragePermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }


    /**
     * 定位权限
     */
    private fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 申请sd卡权限
     */
    private fun requestStoragePermission(
        activity: FragmentActivity,
        block: (accept: Boolean) -> Unit
    ) {
        val rxPermissions = RxPermissions(activity)
        St.stPermissionSysDialogShow(from = "$activity", what = "手机读写权限")
        rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe { result ->
                when {
                    result.granted -> {
                        //申请成功,刷新数据
                        St.stPermissionSysDialogDismiss(what = "手机读写权限", type = "允许")
                        block.invoke(true)
                    }
                    result.shouldShowRequestPermissionRationale -> {
                        St.stPermissionSysDialogDismiss(what = "手机读写权限", type = "拒绝")
                        block.invoke(false)
                    }
                    else -> {
//                        block.invoke(false)
                        St.stPermissionSysDialogDismiss(what = "手机读写权限", type = "拒绝且不在提示")
                        DPermissionDialogFragment.newInstance(
                            ResourceUtil.getString(R.string.scenes_write_phone_permission),
                            ResourceUtil.getString(R.string.scenes_clean_need_permiss),block
                        )
                            .showDialog(
                                activity.supportFragmentManager,
                                what = "手机读写权限",
                                from = "$activity"
                            )
                    }
                }
            }
    }

    /**
     * 申请定位权限
     */
    private fun requestLocationPermission(
        activity: FragmentActivity,
        block: (accept: Boolean) -> Unit
    ) {
        val rxPermissions = RxPermissions(activity)
        St.stPermissionSysDialogShow(from = "$activity", what = "定位权限")
        rxPermissions.requestEach(Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe { result ->
                when {
                    result.granted -> {
                        St.stPermissionSysDialogDismiss(what = "定位权限", type = "允许")
                        block.invoke(true)
                    }
                    result.shouldShowRequestPermissionRationale -> {
                        St.stPermissionSysDialogDismiss(what = "定位权限", type = "拒绝")
                        block.invoke(false)
                    }
                    else -> {
                        St.stPermissionSysDialogDismiss(what = "定位权限", type = "拒绝且不在提示")
                        block.invoke(false)
                        DPermissionDialogFragment.newInstance("定位", "本服务需要\n开启手机定位权限",block)
                            .showDialog(
                                activity.supportFragmentManager,
                                what = "定位权限",
                                from = "$activity"
                            )
                    }
                }
            }
    }

    fun checkLocationPermission(activity: FragmentActivity, block: (accept: Boolean) -> Unit) {
        if (hasLocationPermission(activity)) {
            block.invoke(true)
            return
        }
        requestLocationPermission(activity, block)
    }

    fun checkStoragePermission(activity: FragmentActivity, block: (accept: Boolean) -> Unit) {
        if (hasFileScanPermission(activity)) {
            block.invoke(true)
            return
        }
        requestStoragePermission(activity, block)
    }


    fun isAbove30(): Boolean {
        return Build.VERSION.SDK_INT >= 30
    }
}