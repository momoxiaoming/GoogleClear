package com.mckj.gallery.utils

import com.alibaba.android.arouter.launcher.ARouter
import com.mckj.gallery.bean.ARouterPath
import com.mckj.gallery.bean.InteractionBean

/**
 * @author leix
 * @version 1
 * @createTime 2021/9/17 15:36
 * @desc
 */
object GalleryInteraction {

    var interactionBean: InteractionBean? = null

    fun startGallery(bean: InteractionBean) {
        this.interactionBean = bean
        ARouter.getInstance().build(ARouterPath.GALLERY_PATH).navigation()
    }


    fun clearInteractionData() {
        this.interactionBean = null
    }

}