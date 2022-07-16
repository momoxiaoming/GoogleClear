package com.mckj.sceneslib.permission

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.app.ViActivity
import com.dn.vi.app.cm.utils.TextUtils
import com.mckj.sceneslib.R

/**
 * @author xx
 * @version 1
 * @createTime 2021/12/15 17:22
 * @desc
 */
@Route(path = "/rp/androidData")
open class AndroidDataActivity : ViActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.apply {
            setBackgroundDrawable(getDrawable(R.drawable.shape_android_data_activity))
        }
        setContentView(R.layout.dialog_android_data_activity)

        findViewById<RelativeLayout>(R.id.root_layout).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.desc).text = TextUtils.string2SpannableString(
            getString(R.string.rp_click_here_and_start),
            getString(R.string.rp_boosting),
            22,
            Color.parseColor("#FFFF00"), true
        )
    }

    override fun initLayout() {

    }


}