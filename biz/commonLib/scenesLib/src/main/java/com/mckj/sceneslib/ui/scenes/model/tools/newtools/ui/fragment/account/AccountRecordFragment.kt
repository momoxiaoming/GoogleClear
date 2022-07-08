package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.account

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.app.DatabindingFragment
import com.mckj.baselib.helper.showToast
import com.mckj.datalib.entity.ARouterPath
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.AccountRecordFragmentBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter.AccountPayTypeAdapter
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.AccountInfo
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.AccountTypeInfo
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.AccountHelper
import java.math.BigDecimal
import java.util.*

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.account
 * @data  2022/4/12 17:32
 */
@Route(path = ARouterPath.NewTools.NEW_TOOLS_ACCOUNT_RECORD)
class AccountRecordFragment:DatabindingFragment<AccountRecordFragmentBinding>() {

    private val mAccountTypeAdapter by lazy { AccountPayTypeAdapter() }

    private var currentType = ""


    override fun onCreateDatabinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): AccountRecordFragmentBinding {
        return  AccountRecordFragmentBinding.inflate(inflater,container,false)
    }



    override fun initLayout() {
        initView()
        initEvent()
    }


    private fun initView() {


        binding.apply {
            recordTitleBar.apply {
                setTitle(" 支出记账", R.color.white)
                setBackColor(R.color.white)
                setTitleBarBgColor(R.color.account_record_color)
                setTitleBarListener(backAction = {
                    requireActivity().finish()
                })
            }
            showSoftInput()
            AccountHelper.setEditRule(editTypeMoney)

            typeContainer.layoutManager = GridLayoutManager(context,4)
            typeContainer.adapter = mAccountTypeAdapter
            mAccountTypeAdapter.setList(AccountHelper.getAccountTypeData())

            val accountType = AccountHelper.getAccountTypeData()[0]
            currentType=accountType.payType
            setSelectContent(accountType)
        }
    }


   private fun setSelectContent(accountTypeInfo: AccountTypeInfo){
        binding. typeTitle.text = currentType
        binding. typeImage.setImageResource(accountTypeInfo.payIcon)
    }

    private fun initEvent() {
        binding.apply {
            activity?.window?.let {
                editTypeMoney.viewTreeObserver.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
                    val rect = Rect()
                    it.decorView.getWindowVisibleDisplayFrame(rect) //获取当前界面可视部分
                    val screenHeight: Int =
                        it.decorView.rootView.height //获取屏幕高度
                    val heiDifference: Int =
                        screenHeight - rect.bottom //获取键盘高度，键盘没有弹出时，高度为0，键盘弹出时，高度为正数
                    setEdittextPosition(heiDifference)
                })
            }



            mAccountTypeAdapter.setOnItemClickListener { adapter, view, position ->
                val accountType = adapter.data[position] as AccountTypeInfo
                currentType = accountType.payType
                setSelectContent(accountType)
            }

            typeSure.setOnClickListener {
                val moneyStr = editTypeMoney.text.toString()
                if (moneyStr.isNotEmpty()) {
                    try {
                        val money = moneyStr.toDouble()
                        val bigDecimal = BigDecimal(money).setScale(1, BigDecimal.ROUND_HALF_UP)
                        AccountHelper.addAccountInfo(
                            AccountInfo(
                                currentType,
                                bigDecimal,
                                System.currentTimeMillis(),
                                AccountHelper.getNowDateFormat(AccountHelper.FORMAT_YMD),
                                AccountHelper.getNowDateFormat(AccountHelper.FORMAT_YM),
                                AccountHelper.getCurrentDate()[0],
                                AccountHelper.getCurrentDate()[1]
                            )
                        )
                        St.stSetAccountingClick("确定")
                        requireActivity().finish()
                    }catch (e:Exception){
                        badInput()
                    }

                }else{
                    badInput()
                }
            }
        }
    }

    private fun badInput(){
        binding.editTypeMoney.setText("")
        showToast("请输入正确的花销")
    }


    private fun setEdittextPosition(margin:Int){
      binding.apply {
          val layoutParams = inputContainer.layoutParams as ConstraintLayout.LayoutParams
          layoutParams.bottomMargin=margin
          inputContainer.layoutParams= layoutParams
      }

    }

    private fun showSoftInput(){
        binding.inputContainer.requestFocus()
        val manager =requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.showSoftInput(binding.inputContainer, 0)
    }




}