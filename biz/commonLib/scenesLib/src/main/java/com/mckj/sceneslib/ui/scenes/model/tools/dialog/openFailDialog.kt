package com.mckj.sceneslib.ui.scenes.model.tools.dialog

import android.content.Context
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.mckj.sceneslib.R

object openFailDialog {

    fun show(context: Context) {

        val view: View = View.inflate(context, R.layout.cleanupx_open_tools_failure, null)
        val btn: Button = view.findViewById(R.id.button) as Button

        val ad = AlertDialog.Builder(context).apply {
            // 构建一个对话框
            setView(view)
            setCancelable(true)
        }.create()
        ad.show()
        btn.setOnClickListener(View.OnClickListener {
            ad.cancel()
        })
    }
}

