package com.dn.openlib.ui.splash

import android.Manifest
import android.content.DialogInterface
import androidx.core.util.Consumer
import androidx.fragment.app.FragmentActivity
import com.dn.vi.app.cm.log.VLog
import com.dn.vi.app.repo.kv.KvSp

import com.tbruyelle.rxpermissions3.RxPermissions

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy

/**
 * Describe:启动页帮助类
 *
 * Created By yangb on 2020/12/16
 */
object SplashHelper {

    private const val PERMISSION_KEY = "permission_key"
    //协议最大拒绝次数
    private const val PROTOCOL_REFUSE_MAX = 2
    //当前协议页拒绝次数
    private var protocolRefuseCount = 0

    val log: VLog.Logger
        get() = VLog.scoped("startup")






    /**
     * 权限申请
     *
     * @param permissions 权限列表
     * @param callback (单个权限名，单个权限结果, 是否申请完成)
     */
    fun requestPermissions(
        activity: FragmentActivity,
        permissions: Array<String>,
        phoneCallback: Consumer<Boolean>,
        callback: Consumer<Boolean>
    ) {
        log.i("requestPermissions: ")
        //权限为空时
        if (permissions.isEmpty()) {
            callback.accept(true)
            return
        }

        val rxPermissions = RxPermissions(activity)
        var hasPhonePermission: Boolean = false
        //设备权限独立
        val newPermissions: Array<String> = permissions.filter {
            val result = it != Manifest.permission.READ_PHONE_STATE
            if (!result) {
                hasPhonePermission = true
            }
            result
        }.toTypedArray()
        if (hasPhonePermission) {
            rxPermissions.request(Manifest.permission.READ_PHONE_STATE)
                .subscribeBy { result ->
                    phoneCallback.accept(result)
                    requestPermissions(rxPermissions, newPermissions, callback)
                }
        } else {
            //没有设备权限，直接返回false
            phoneCallback.accept(false)
            requestPermissions(rxPermissions, newPermissions, callback)
        }
    }

    private fun requestPermissions(
        rxPermissions: RxPermissions,
        permissions: Array<String>,
        callback: Consumer<Boolean>
    ) {
        if (permissions.isEmpty()) {
            KvSp.putBool(PERMISSION_KEY, true)
            callback.accept(true)
            return
        }
        rxPermissions.request(*permissions)
            .subscribeBy(onError = {
                KvSp.putBool(PERMISSION_KEY, false)
                callback.accept(false)
            }) { result ->
                KvSp.putBool(PERMISSION_KEY, true)
                callback.accept(result)
            }
    }

}