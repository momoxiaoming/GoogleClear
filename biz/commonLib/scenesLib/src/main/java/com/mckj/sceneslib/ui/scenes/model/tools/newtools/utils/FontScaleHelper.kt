package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.TypedValue
import android.widget.TextView
import com.dn.vi.app.base.app.AppMod

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.wjm.util.main.font
 * @data  2022/4/1 11:11
 */
object FontScaleHelper {

        private var fontSuccessState = false

        fun setFontSuccessState(state:Boolean){
            fontSuccessState = state
        }

        fun getFontSuccessState() = fontSuccessState


        private const val DEFAULT_FONT_SIZE = 17f

        fun checkPermission(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Settings.System.canWrite(
                context
            ) else false
        }

        fun getPermissionPageIntent(context: Context): Intent {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:" + context.packageName)
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            return intent
        }

        fun getFontScale(context: Context) = Settings.System.getFloat(context.contentResolver, Settings.System.FONT_SCALE)

        fun setFontScale(context: Context,value:Float) {
            fontSuccessState=true
            Settings.System.putFloat(context.contentResolver, Settings.System.FONT_SCALE,value)
        }

        fun setTextFontScale(textView: TextView,scaleRatio:Float){
            //textView.textSize = DEFAULT_FONT_SIZE*scaleRatio
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,DEFAULT_FONT_SIZE*scaleRatio)
        }
    }
