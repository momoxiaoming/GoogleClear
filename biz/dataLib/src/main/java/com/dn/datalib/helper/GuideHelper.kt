package com.dn.datalib.helper

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Describe:引导页帮助类
 *
 * Created By yangb on 2021/1/7
 */
object GuideHelper {

    const val TAG = "GuideHelper"

    /**
     * 显示引导
     */
    fun showGuide(scope: CoroutineScope, targetView: View, block: () -> Unit) {
        scope.launch {
            val time = System.currentTimeMillis()
            var result = false
            //3秒内退出
            while (System.currentTimeMillis() - time < 3000) {
                if (targetView.width > 0 || targetView.height > 0) {
                    result = true
                    break
                }
                delay(50)
            }
            if (result) {
                block()
            }
        }
    }

}