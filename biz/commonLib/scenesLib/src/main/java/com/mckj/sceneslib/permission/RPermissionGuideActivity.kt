package com.mckj.sceneslib.permission

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.app.ViActivity
import com.mckj.baselib.util.SizeUtil
import com.mckj.datalib.helper.FingerHelper
import com.mckj.sceneslib.R

/**
 * @author leix
 * @version 1
 * @createTime 2021/12/15 17:22
 * @desc 设置页权限开关引导弹窗
 */
@Route(path = "/rp/main/guide")
open class RPermissionGuideActivity : ViActivity() {

    /**
     * dialog的window背景
     */
//    protected open val dialogBackgroundDrawable: Drawable by lazy {
//        SimpleRoundDrawable(this, 0xffffffff.toInt()).also {
//            this.also { ctx ->
//                it.roundRadius = ctx.dip(12).toFloat()
//            }
//        }
//    }

    private var mFrom: String = ""
    /**
     * 手指帮助类
     */
    private val mFingerHelper: FingerHelper by lazy { FingerHelper() }
    private var mFingerView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.apply {
//            setBackgroundDrawable(dialogBackgroundDrawable)
        }
        setContentView(R.layout.dialog_r_permission_guide)

        mFrom = intent.getStringExtra("from")!!
//        findViewById<TextView>(R.id.open).setOnClickListener {
//            FileUriUtils.startForRoot(this)
//            val intent = Intent(this@RPermissionGuideActivity, AndroidDataActivity::class.java)
//            startActivity(intent)
//        }

        val appName = resources.getText(R.string.app_name)
        findViewById<TextView>(R.id.desc).text = "Find [${appName}] and turn it on"

        findViewById<ConstraintLayout>(R.id.root_layout).setOnClickListener {
            close()
        }
        findViewById<ImageView>(R.id.close).setOnClickListener {
            close()
        }
        val rootLayout = findViewById<ConstraintLayout>(R.id.root_layout)
        val openLayout = findViewById<LinearLayout>(R.id.open)

        //手指动画
        openLayout.doOnLayout {
            mFingerHelper.remove(mFingerView)
            mFingerView = mFingerHelper.showFinger(
                rootLayout,
                openLayout, SizeUtil.dp2px(-50f), SizeUtil.dp2px(5f),
                100f
            )
        }
    }

    override fun initLayout() {

    }

    fun close() {
        finish()
//        val result = "${false}#步骤1"
//        FileUriUtils.takePersistableUriPermission(this, null, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        when (resultCode) {
//            RESULT_OK -> {
//                FileUriUtils.takePersistableUriPermission(this, data, true)
//            }
//            RESULT_CANCELED -> {
//                FileUriUtils.takePersistableUriPermission(this, data, false)
//            }
//        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mFingerHelper.remove(mFingerView)
        mFingerView = null
    }


}