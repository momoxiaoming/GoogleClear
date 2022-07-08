package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.magnifier

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.dn.vi.app.cm.log.VLog

/**
 * @Description
 * @CreateTime 2022/6/2 12:09
 * @Author
 */
class SelectPhotoContract : ActivityResultContract<Unit?,Uri?>() {
    companion object {
        private const val TAG = "SelectPhotoContract"
    }
    override fun createIntent(context: Context, input: Unit?): Intent {
        return Intent(Intent.ACTION_PICK).setType("image/*")
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        VLog.d("Select photo uri: ${intent?.data}")
        return intent?.data
    }
}