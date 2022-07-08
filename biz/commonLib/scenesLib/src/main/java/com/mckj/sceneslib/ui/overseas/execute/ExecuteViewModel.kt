package com.mckj.sceneslib.ui.overseas.execute

import android.content.pm.PackageInfo
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.org.openlib.utils.ProcessUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExecuteViewModel : AbstractViewModel() {
    suspend fun getRunningApps(): List<PackageInfo>? {
        return withContext(Dispatchers.IO) {
            return@withContext ProcessUtil.getRunningApps()
        }
    }
}