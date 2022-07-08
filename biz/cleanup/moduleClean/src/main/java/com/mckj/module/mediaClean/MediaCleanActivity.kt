package com.mckj.module.mediaClean

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.mckj.api.client.JunkConstants
import com.mckj.api.entity.AppJunk
import com.dn.vi.app.base.app.kt.getAs
import com.dn.vi.app.base.helper.DataTransport
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.baselib.base.databinding.DataBindingActivity
import com.mckj.datalib.entity.ARouterPath
import com.mckj.gallery.bean.GalleryConstants
import com.mckj.module.bean.ChildFragment
import com.mckj.module.bean.QAuthorBean
import com.mckj.module.manager.CleanManager
import com.mckj.module.viewmodel.MediaCleanViewModel
import com.mckj.moduleclean.R
import com.mckj.moduleclean.databinding.CleanupActivityMediaCleanBinding
import com.org.openlib.utils.SystemUiUtil

@Route(path = ARouterPath.ModuleClean.MODULE_CLEAN)
class MediaCleanActivity :
    DataBindingActivity<CleanupActivityMediaCleanBinding, MediaCleanViewModel>() {

    private var mChildType: ChildFragment = ChildFragment.IMG

    override fun getViewModel(): MediaCleanViewModel {
        return ViewModelProvider(
            this,
            MediaCleanViewModel.MediaCleanViewModelFactory()
        ).get(
            MediaCleanViewModel::class.java
        )
    }

    override fun getLayoutId(): Int {
        return R.layout.cleanup_activity_media_clean
    }

    override fun onDestroy() {
        super.onDestroy()
        CleanManager.instance.clearSelectedData()
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initView() {
        SystemUiUtil.immersiveSystemUi(window)
        parseIntent()
        mBinding.recycledIcon.setOnClickListener {
            ARouter.getInstance().build(com.mckj.gallery.bean.ARouterPath.GALLERY_RECYCLED_PATH)
                .navigation()
        }
        mBinding.titleLayout.headerToolbar.apply {
            setNavigationOnClickListener {
                finish()
            }
        }
    }


    private fun parseIntent() {
        intent?.apply {
            mModel.mType = getIntExtra("appType", 0)
            val type = getIntExtra("type", 0)
            val appJunk = DataTransport.getInstance()
                .getAs<AppJunk>("app_junk")
            mModel.mAppJunk = appJunk
            setFragment(type)
        }
    }

    var fragments: Fragment? = null
    private fun setFragment(type: Int) {
        when (type) {
            JunkConstants.AppCacheType.IMG_CACHE -> {
                mChildType = ChildFragment.IMG
                mBinding.titleLayout.headerToolbar.title = "图片"
                mBinding.recycledIcon.show()
                fragments = ImgCleanFragment()
            }
            JunkConstants.AppCacheType.VIDEO_CACHE -> {
                mChildType = ChildFragment.VIDEO
                mBinding.titleLayout.headerToolbar.title = "视频"
                mBinding.recycledIcon.show()
                fragments = VideoCleanFragment()
            }
            JunkConstants.AppCacheType.DOCUMENT_CACHE -> {
                mChildType = ChildFragment.DOCUMENT
                mBinding.titleLayout.headerToolbar.title = "文档"
                mBinding.recycledIcon.gone()
                fragments = DocumentCleanFragment()
            }
        }
        fragments?.let {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, it)
                .commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (fragments is ImgCleanFragment) {
            (fragments as ImgCleanFragment).activityResult(requestCode, resultCode, data)
        }else if (fragments is VideoCleanFragment){
            (fragments as VideoCleanFragment).activityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}