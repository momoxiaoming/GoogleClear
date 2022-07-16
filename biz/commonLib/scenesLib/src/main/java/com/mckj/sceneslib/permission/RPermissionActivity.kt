package com.mckj.sceneslib.permission

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.app.ViActivity
import com.dn.vi.app.base.view.drawable.SimpleRoundDrawable
import com.mckj.api.util.RFileUtils
import com.mckj.core.gen.St
import com.mckj.sceneslib.R
import org.jetbrains.anko.dip

/**
 * @author xx
 * @version 1
 * @createTime 2021/12/15 17:22
 * @desc
 */
@Route(path = "/rp/main")
open class RPermissionActivity : ViActivity() {

    /**
     * dialog的window背景
     */
    protected open val dialogBackgroundDrawable: Drawable by lazy {
        SimpleRoundDrawable(this, 0xffffffff.toInt()).also {
            this.also { ctx ->
                it.roundRadius = ctx.dip(12).toFloat()
            }
        }
    }

    private var mFrom: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.apply {
            setBackgroundDrawable(dialogBackgroundDrawable)
            addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        }
        setContentView(R.layout.dialog_r_permission)

        mFrom = intent.getStringExtra("from")!!
        St.stDataDialogPop(mFrom)
        findViewById<TextView>(R.id.open).setOnClickListener {
            RFileUtils.startForRoot(this)
            val intent = Intent(this@RPermissionActivity, AndroidDataActivity::class.java)
            startActivity(intent)
        }

        val content = application.resources.getString(R.string.on_new_page)
        val value = application.resources.getString(R.string.blue_button)
        val index = content.indexOf(value)
        val spannableString = SpannableString(content)
        if (index != -1) {
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor("#3E87FA")),
                index,
                index + value.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }


        findViewById<TextView>(R.id.tv_button_tips).text = spannableString

        findViewById<RelativeLayout>(R.id.root_layout).setOnClickListener {
            close()
        }
        findViewById<ImageView>(R.id.close).setOnClickListener {
            close()
        }
    }

    override fun initLayout() {

    }

    fun close() {
        finish()
        val result = "${false}#步骤1"
        St.stDataPermissionResult(result)
        RFileUtils.takePersistableUriPermission(this, null, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RESULT_OK -> {
                RFileUtils.takePersistableUriPermission(this, data, true)
                St.stDataPermissionResult("${true}#成功")
            }
            RESULT_CANCELED -> {
                RFileUtils.takePersistableUriPermission(this, data, false)
                St.stDataPermissionResult("${true}#失败")
            }
        }
        finish()
    }


}