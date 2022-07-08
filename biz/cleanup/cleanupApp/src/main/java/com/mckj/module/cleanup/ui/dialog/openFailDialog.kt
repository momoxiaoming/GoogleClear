package com.mckj.module.cleanup.ui.dialog

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.ui.adapter.AppRecyclerAdapter
import com.mckj.module.gen.St
import com.mckj.module.utils.EventTrack

object OpenFailDialog {

    var ad: AlertDialog? = null

    fun  show(context: Context, adapter: AppRecyclerAdapter?, type:String?) {
        var view: View? = null
        view = when(type){
            "卸载" ->
                View.inflate(context, R.layout.cleanup_app_manager_uninstall_remind, null)
            "访问权限" ->
                View.inflate(context, R.layout.cleanup_app_manager_permission_remind, null)
            else -> {
                View.inflate(context, R.layout.cleanup_app_manager_uninstall_remind, null)
            }
        }
        val cancelBtn: Button = view?.findViewById(R.id.button_cancel) as Button
        val rightBtn: Button = view.findViewById(R.id.button_unInstall) as Button

        ad = AlertDialog.Builder(context).apply {
            // 构建一个对话框
            setView(view)
            setCancelable(true)
        }.create()
        ad!!.show()

        //取消按钮
        cancelBtn.setOnClickListener(View.OnClickListener {
            when(type){
                "卸载" -> {
                    EventTrack.stManagementPopCancelClick()
                }
            }
            ad!!.cancel()
        })

        //右边按钮
        rightBtn.setOnClickListener {
            when(type) {
                "卸载" -> {
                    EventTrack.stManagementPopUnloadClick()
                    adapter?.unInstallChecked()
                    ad!!.cancel ()
                }
                "访问权限" -> {
                    context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                    ad!!.cancel ()
                }
            }
        }
    }

    fun isAdShow(): Boolean? {
        return ad?.isShowing
    }

}

