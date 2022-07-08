package com.mckj.gallery.newstyle

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.dn.vi.app.cm.utils.DAttrUtil
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.baselib.base.databinding.DataBindingActivity
import com.mckj.gallery.R
import com.mckj.gallery.bean.ARouterPath
import com.mckj.gallery.databinding.CleanupxActivityGalleryResultBinding
import com.mckj.gallery.newstyle.local.LocalDataSource
import com.mckj.gallery.newstyle.viewmodel.GalleryResultViewModel
import com.org.openlib.utils.FragmentUtil
import com.org.openlib.utils.onceClick
import com.org.openlib.utils.SystemUiUtil

class GalleryResultActivity : DataBindingActivity<CleanupxActivityGalleryResultBinding, GalleryResultViewModel>(){
    companion object{
        const val PHOTOS = "photos"
        const val SIZE = "size"
    }
    override fun getViewModel(): GalleryResultViewModel {
        return ViewModelProvider(this).get(GalleryResultViewModel::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.cleanupx_activity_gallery_result
    }

    override fun initData(savedInstanceState: Bundle?) {
        SystemUiUtil.setBarColor(window, DAttrUtil.getPrimaryColor(this))
    }

    override fun initView() {
        FragmentUtil.show(supportFragmentManager,ARouterPath.FRAGMENT_SCENES_COMMON,mBinding.contentLayout.id)

        mBinding.resultGoGalleryBtn.onceClick {
            ARouter.getInstance().build("/gallery/main").navigation()
            LocalDataSource.saveAgain()
            finish()
        }
        mBinding.resultBack.onceClick {
            finish()
        }

        var photos = intent.getIntExtra(PHOTOS,0)
        var size = intent.getLongExtra(SIZE,0)

        if (photos > 0 && size > 0){
            mBinding.resultContentTv.text =
                Html.fromHtml(getString(R.string.result_content,photos,
                    "${FileUtil.getFileSizeNumberText(size)}${FileUtil.getFileSizeUnitText(size)}"))
        }else{
            mBinding.resultContentTv.text =  getString(R.string.result_content_finish)
        }

    }


}