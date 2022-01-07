package com.dn.openlib.ui.container

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route

import com.dn.openlib.OpenLibARouterPath
import com.dn.openlib.utils.SystemUiUtil


/**
 * Describe:
 *
 * Created By yangb on 2020/10/22
 */
@Route(path = OpenLibARouterPath.ACTIVITY_CONTAINER_FULL)
class ContainerFullScreenActivity : ContainerActivity() {

    override fun onInternalCreate(savedInstanceState: Bundle?) {
        SystemUiUtil.hideSystemUI(window)
    }

}