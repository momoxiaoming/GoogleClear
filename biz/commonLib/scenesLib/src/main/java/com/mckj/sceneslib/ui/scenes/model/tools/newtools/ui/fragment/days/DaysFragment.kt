package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.days

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show

import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.datalib.entity.ARouterPath
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.DaysFragmentBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.DaysInfo
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.weight.DaysEventView
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.weight.DaysTimeView
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.DateUtil
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm.DaysViewModel
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm.obtainViewModel

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment
 * @data  2022/4/13 17:13
 */
@Route(path = ARouterPath.NewTools.NEW_TOOLS_DAYS)
class DaysFragment : DataBindingFragment<DaysFragmentBinding, DaysViewModel>() {

    companion object{
        private const val AD_ID="dsshome_msg"
    }

    override fun getLayoutId(): Int = R.layout.days_fragment

    override fun getViewModel(): DaysViewModel = obtainViewModel()

    override fun initData() {
        St.stSetCountdownShow()
        mBinding.data = mModel
        mModel.queryDaysInfo()
    }

    override fun initView() {
        showAd()
        mBinding.apply {
            daysTitleBar.apply {
                setTitle("倒数日", R.color.back)
                setBackColor()
                setTitleBarBgColor(R.color.white)
                setTitleBarListener(backAction = {
                    requireActivity().finish()
                })
            }
        }
        initEvent()
    }

    private fun showAd(){
//            AdManager.getInstance().showAd(
//               AD_ID,
//                ViewGroupAdContainer(mBinding.adContainer),
//                this,
//            ) {
//                when (it.adStatus) {
//                    AdStatus.SHOW_SUCCESS -> {
//                        mBinding.adContainer.show()
//                    }
//                    AdStatus.ERROR -> {
//                        mBinding.adContainer.gone()
//                    }
//                    AdStatus.CLICK -> {
//
//                    }
//                    AdStatus.CLOSE -> {
//                        mBinding.adContainer.gone()
//                    }
//                }
//            }
    }

    
    override fun initObserver() {
        super.initObserver()
        mBinding.apply {
            mModel.apply {
              daysInfo.observe(this@DaysFragment){
                  val intervalDays = DateUtil.intervalDays(it.eventTime).toString()
                  daysTime.textSize =  if (intervalDays.length>3)  80f else 133f
                  daysTime.text = intervalDays
                  daysDate.text = "${DateUtil.formatStampDate(it.eventTime)}"
                  daysTitle.text = "${it.eventName}还有"
              }
            }
        }
    }


    private fun initEvent() {
        mBinding.apply {
            daysEventEdit.setOnClickListener {
                St.stSetCountdownClick("设置")
                mModel.changeEditState(true)
            }

            daysEventView.setClickEvent {
                when(it){
                    DaysEventView.EVENT_CLOSE->{
                        mModel.changeEditState(false)
                    }
                    DaysEventView.EVENT_TIME->{
                        changeTimeViewState(true)
                        hideKeyboard()
                    }
                }
            }

            daysTimeView.setClickCallback {
                if (it> DaysTimeView.EVENT_CANCEL){
                    St.stSetCountdownClick("确定")
                    val daysInfo = DaysInfo(daysEventView.getDaysEventName(), it)
                    mModel.setDaysInfo(daysInfo)
                }
                changeTimeViewState(false)
                mModel.changeEditState(false)
                mBinding.daysEventView.setEditEmpty()
                mBinding.daysTimeView.setCheckBoxState()
            }

        }
    }

    private fun changeTimeViewState(state:Boolean){
        mBinding.daysTimeView.isVisible=state
    }


    private fun  hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mBinding.daysEventView.windowToken, 0);
    }


}