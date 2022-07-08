package com.mckj.module.cleanup.manager


import com.mckj.module.cleanup.manager.jump.DefaultJumpPage
import com.mckj.module.cleanup.manager.jump.IJumpPage
import com.mckj.module.cleanup.manager.jump.model.HuaweiJumpPage
import com.mckj.module.cleanup.manager.jump.model.OppoJumpPage
import com.mckj.module.cleanup.manager.jump.model.VivoJumpPage
import com.mckj.module.cleanup.manager.jump.model.XiaomiJumpPage
import com.org.openlib.utils.RomUtils

/**
 * Describe:
 *
 * Created By yangb on 2021/7/22
 */
class SystemJumpManager private constructor() {

    companion object {

        const val TAG = "JumpPageManager"

        private val INSTANCE by lazy { SystemJumpManager() }

        fun getInstance(): SystemJumpManager = INSTANCE

    }

    fun getJumpPage(): IJumpPage {
        return when {
            RomUtils.isHuawei() -> {
                HuaweiJumpPage()
            }
            RomUtils.isXiaomi() -> {
                XiaomiJumpPage()
            }
            RomUtils.isVivo() -> {
                VivoJumpPage()
            }
            RomUtils.isOppo() -> {
                OppoJumpPage()
            }
            else -> {
                DefaultJumpPage()
            }
        }
    }

}