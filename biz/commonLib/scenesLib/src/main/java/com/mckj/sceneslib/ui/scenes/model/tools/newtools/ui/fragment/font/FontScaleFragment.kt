package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.font

import android.content.res.Configuration
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.datalib.entity.ARouterPath
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.FontScaleFragmentBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter.FontScaleAdapter
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.FontScaleHelper
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm.FontScaleViewModel
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm.obtainViewModel
import com.mckj.sceneslib.util.Log
import kotlin.math.roundToInt

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.font
 * @data  2022/4/11 18:13
 */
@Route(path = ARouterPath.NewTools.NEW_TOOLS_FONT_SCALE)
class FontScaleFragment: DataBindingFragment<FontScaleFragmentBinding, FontScaleViewModel>() {
    companion object{
        private const val TITLE = "字体放大器"
    }
    private val mFontScaleAdapter by lazy {
        FontScaleAdapter()
    }
    private val fontList by lazy {
        arrayListOf(
            FontScaleHelper.getFontScale(requireContext()),1.6f,1.4f,1.3f,1.2f,1.0f
        )
    }
    private var mCurrentScale = 1.0f
    private  val registerForActivityResult =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (FontScaleHelper.checkPermission(requireContext())) {
            FontScaleHelper.setFontScale(requireContext(),mCurrentScale)
        }
    }
    override fun getLayoutId(): Int = R.layout.font_scale_fragment

    override fun getViewModel(): FontScaleViewModel = obtainViewModel()

    override fun initData() {
        St.stSetFontShow()
    }

    override fun initView() {
        mBinding.apply {
            fontScaleTitleBar.apply {
                setTitle(TITLE, R.color.back)
                setBackColor()
                showSetIcon()
                setTitleSize(18f)
                setTitleBarBgColor(R.color.white)
                setTitleBarListener(backAction = {
                    requireActivity().finish()
                }, setAction = {
                    showSetDialogState(true)
                })
            }


            fontScaleContainer.layoutManager = LinearLayoutManager(context)
            fontScaleContainer.adapter = mFontScaleAdapter
            mFontScaleAdapter.setList(fontList)
        }
        initEvent()
    }

    private fun initEvent() {
        mFontScaleAdapter.setOnItemChildClickListener { adapter, view, position ->
            mCurrentScale = adapter.data[position] as Float
            setFontScale()
        }

        mBinding.fontSuccessView.setDismissCallback {
            FontScaleHelper.setFontSuccessState(false)
            showSuccessDialogState(false)
        }

        mBinding.fontSetView.setCallback {
            showSetDialogState(false)
            if (it>0){
                mCurrentScale = it/100f
                setFontScale()
                St.stSetFontClick("自定义")
               // showSuccessDialogState(true)
            }
        }

        //显示dialog View
        showSuccessDialogState(FontScaleHelper.getFontSuccessState())
    }


    private fun setFontScale(){
        if (!FontScaleHelper.checkPermission(requireContext())) {
            val permissionPageIntent = FontScaleHelper.getPermissionPageIntent(requireContext())
            registerForActivityResult.launch(permissionPageIntent)
        }else{
            FontScaleHelper.setFontScale(requireContext(),mCurrentScale)
            St.stSetFontClick("${(mCurrentScale*100).roundToInt()}%")
        }
    }

   private fun showSuccessDialogState(state: Boolean){
        mBinding.fontSuccessView.isVisible = state
    }


   private fun showSetDialogState(state: Boolean){
       mBinding.fontSetView.isVisible=state
   }


}