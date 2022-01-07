package com.mckj.sceneslib.data.model.impl

import android.Manifest.permission
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.dn.vi.app.base.app.AppMod
import com.mckj.sceneslib.data.model.ITools
import com.mckj.sceneslib.entity.OptItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * BellsImpl
 *
 * @author mmxm
 * @date 2021/3/4 10:12
 */
class ToolImpl : ITools {

    override suspend fun getBellsMenuList(): MutableList<OptItem> {

        return withContext(Dispatchers.IO) {
            val mutableList = mutableListOf<OptItem>()
            if (!isOpenAutoClean()) mutableList.add(OptItem(OptItem.AUTO_CLEAN))
            if (!isOpenDeskTool()) mutableList.add(OptItem(OptItem.DESK_TOOL))
            if (!isOpenNotPMis()) mutableList.add(OptItem(OptItem.OPEN_NOT))
            if (!isOpenFloatWin()) mutableList.add(OptItem(OptItem.OPEN_FLOAT))
            if (!isOpenAuthority()) mutableList.add(OptItem(OptItem.OPEN_AUTHORITY))

            mutableList
        }
    }


    private fun isOpenAutoClean(): Boolean {
        return false
    }

    private fun isOpenDeskTool(): Boolean {
        return true
    }

    private fun isOpenNotPMis(): Boolean {
        return true
    }

    private fun isOpenFloatWin(): Boolean {
        return true
    }

    private fun isOpenAuthority(): Boolean {
        val checkCallPhonePermission =
            ContextCompat.checkSelfPermission(AppMod.app, permission.READ_EXTERNAL_STORAGE)
        return checkCallPhonePermission== PackageManager.PERMISSION_GRANTED
    }
}