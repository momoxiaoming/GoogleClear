package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.account

import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.datalib.entity.ARouterPath
import com.org.openlib.helper.startFragment
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.AccountFragmentBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter.AccountMonthAdapter
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter.AccountYearAdapter
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.AccountHelper
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.DateUtil
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm.AccountViewModel
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm.obtainViewModel

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.account
 * @data  2022/4/12 16:52
 */
@Route(path = ARouterPath.Cleanupx.FRAGMENT_TAB_ACCOUNT)
class AccountTabFragment : DataBindingFragment<AccountFragmentBinding, AccountViewModel>() {


    private val mAccountMonthAdapter by lazy { AccountMonthAdapter() }
    private val mAccountYearAdapter by lazy { AccountYearAdapter() }

    override fun getLayoutId(): Int = R.layout.account_fragment

    override fun getViewModel(): AccountViewModel = obtainViewModel()


    override fun initData() {
        AccountHelper.queryAllAccountInfo()
    }

    override fun initView() {
        mBinding.data =mModel
        mBinding.apply {
            accountTitleBar.isVisible=false
            accountMonthData.layoutManager = LinearLayoutManager(context)
            accountMonthData.adapter = mAccountMonthAdapter

            accountYearData.layoutManager = LinearLayoutManager(context)
            accountYearData.adapter = mAccountYearAdapter

        }
        initEvent()
        St.stSetAccountingShow()
    }

    private var monthAllMoney = 0.0

    override fun initObserver() {
        super.initObserver()
        mBinding.apply {
            AccountHelper.accountMonthLiveData.observe(this@AccountTabFragment) { it ->
                val hasData = it.isNotEmpty()
                accountNoData.isVisible = !hasData
                accountMonthData.isVisible = hasData
                if (hasData){
                    val groupDayList = AccountHelper.groupDayList(it)
                    val toList = groupDayList.toList()
                    mAccountMonthAdapter.setList(toList)

                    monthAllMoney=AccountHelper.sumMoney(it).toDouble()
                    mModel.setTotalMoney(monthAllMoney)
                }
            }


            mModel.apply {
                currentTimeModel.observe(this@AccountTabFragment){
                    val isMonthModel = it == AccountViewModel.MODEL_MONTH
                    accountMonthData.isVisible =isMonthModel
                    accountYearData.isVisible = !isMonthModel
                    if (!isMonthModel) getAccountYearData() else mModel.setTotalMoney(monthAllMoney)
                }


                yearList.observe(this@AccountTabFragment){
                    mAccountYearAdapter.setList(it)
                }

            }

        }
    }

    private fun initEvent() {
        mBinding.apply {
            accountAdd.setOnClickListener {
                St.stSetAccountingClick("添加")
                requireActivity().startFragment(ARouterPath.NewTools.NEW_TOOLS_ACCOUNT_RECORD)
            }

            accountMore.setOnClickListener {
                mModel.setShowTimeModelView(true)
            }

            accountPayModel.setOnClickListener {
                mModel.setShowTimeModelView(false)
                mModel.changeTimeModel()
            }

            accountModelView.setOnClickListener {
                mModel.setShowTimeModelView(false)
            }

        }
    }




}