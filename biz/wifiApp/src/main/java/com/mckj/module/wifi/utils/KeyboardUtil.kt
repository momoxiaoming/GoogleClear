package com.mckj.module.wifi.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.mckj.baselib.helper.getApplication


/**
 * Describe:
 *
 * Created By yangb on 2020/10/28
 */
object KeyboardUtil {

    /**
     * Show the soft input.
     */
    fun showSoftInput() {
        val imm = getInputMethodManager() ?: return
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }


    /**
     * Show the soft input.
     *
     * @param view The view.
     */
    fun showSoftInput(view: View) {
        showSoftInput(view, 0)
    }

    /**
     * Show the soft input.
     *
     * @param view  The view.
     * @param flags Provides additional operating flags.  Currently may be
     * 0 or have the [InputMethodManager.SHOW_IMPLICIT] bit set.
     */
    fun showSoftInput(view: View, flags: Int) {
        val imm = getInputMethodManager() ?: return
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.requestFocus()
        imm.showSoftInput(view, flags, object : ResultReceiver(Handler()) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN
                    || resultCode == InputMethodManager.RESULT_HIDDEN
                ) {
                    toggleSoftInput()
                }
            }
        })
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    /**
     * Hide the soft input.
     *
     * @param activity The activity.
     */
    fun hideSoftInput(activity: Activity) {
        hideSoftInput(activity.window)
    }

    /**
     * Hide the soft input.
     *
     * @param window The window.
     */
    fun hideSoftInput(window: Window) {
        var view: View? = window.currentFocus
        if (view == null) {
            val decorView: View = window.decorView
            val focusView: View? = decorView.findViewWithTag("keyboardTagView")
            if (focusView == null) {
                view = EditText(window.context)
                view.setTag("keyboardTagView")
                (decorView as ViewGroup).addView(view, 0, 0)
            } else {
                view = focusView
            }
            view.requestFocus()
        }
        hideSoftInput(view)
    }

    /**
     * Hide the soft input.
     *
     * @param view The view.
     */
    fun hideSoftInput(view: View) {
        val imm = getInputMethodManager() ?: return
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Toggle the soft input display or not.
     */
    fun toggleSoftInput() {
        val imm = getInputMethodManager()
        imm?.toggleSoftInput(0, 0)
    }

    fun getInputMethodManager(): InputMethodManager? {
        return getApplication().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

}