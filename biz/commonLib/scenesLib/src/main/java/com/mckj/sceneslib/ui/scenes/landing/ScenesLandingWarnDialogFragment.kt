package com.mckj.sceneslib.ui.scenes.landing

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.dn.vi.app.scaffold.LightDialogBindingFragment
import com.mckj.sceneslib.R
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.databinding.ScenesFragmentLandingNewsBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.landing.content.LandingContentSpeedWarningFragment
import com.mckj.sceneslib.ui.scenes.landing.header.LandingHeaderCleanWarnFragment
import com.org.openlib.utils.FragmentUtil

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.landing
 * @data  2022/4/20 14:47
 */
class ScenesLandingWarnDialogFragment : LightDialogBindingFragment() {

    companion object{
        const val FINISH_STATE=0
        const val TAG="ScenesLandingWarnDialogFragment"
    }

    private lateinit var mBinding:ScenesFragmentLandingNewsBinding

    private val mModel by lazy {
        ViewModelProvider(requireActivity()).get(
            ScenesViewModel::class.java
        )
    }
    override  val dialogWindowWidth: Int = WindowManager.LayoutParams.MATCH_PARENT
    override  val dialogWindowHeight: Int = WindowManager.LayoutParams.MATCH_PARENT

    override fun getTheme(): Int {
        return R.style.OpenThemeDialog_Full
    }

    private val landingHeaderCleanWarningFragment by lazy { LandingHeaderCleanWarnFragment() }
    private val landingContentSpeedWarningFragment by lazy { LandingContentSpeedWarningFragment() }


    override fun onCreateRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewDataBinding? {
        mBinding = ScenesFragmentLandingNewsBinding.inflate(inflater, container, false)
        initView()
        return mBinding
    }


    private fun initView() {
        val scenesName = mModel.getScenesData().name
        St.stExitConfirmThreatenShow(scenesName)
        mBinding.titleLayout.headerStateBar.isVisible=false
        mBinding.titleLayout.headerToolbar.apply {
            title = scenesName
            setNavigationOnClickListener {
                St.stExitConfirmThreatenClick("close")
                ARouter.getInstance().build("/app/main").navigation()
                requireActivity().finish()
            }
        }
        showLandingView()
        landingHeaderCleanWarningFragment.setFinishAction {
            dismissAllowingStateLoss()
            dispatchButtonClickObserver(FINISH_STATE)
        }
    }


    /**
     * 显示落地页view
     */
    private fun showLandingView() {
        //添加headerView
        FragmentUtil.show(childFragmentManager,landingHeaderCleanWarningFragment,mBinding.headerLayout.id)

        //添加contentView
        FragmentUtil.show(childFragmentManager,landingContentSpeedWarningFragment,mBinding.contentLayout.id)
    }

}