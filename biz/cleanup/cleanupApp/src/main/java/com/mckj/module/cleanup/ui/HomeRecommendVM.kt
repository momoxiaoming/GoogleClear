package com.mckj.module.cleanup.ui

import androidx.lifecycle.ViewModel
import com.dn.vi.app.cm.log.VLog
import com.mckj.module.cleanup.entity.AbsHomeRecommendScenes
import com.mckj.module.cleanup.entity.scenes.HomeRecommendManager

class HomeRecommendVM : ViewModel() {

    private val log by lazy {
        VLog.scoped("HomeRecommend")
    }

}
