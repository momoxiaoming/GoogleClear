package com.mckj.template

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mckj.api.client.base.JunkClient
import com.mckj.module.cleanup.BaseHomeFragment
import com.mckj.template.impl.IHomeData
import com.mckj.template.abs.AbsMenuView
import com.mckj.template.entity.TepConstants
import com.mckj.template.entity.UIMenuBean
import com.mckj.template.impl.IHomeStyle
import com.mckj.sceneslib.entity.MenuItem
import com.org.openlib.utils.FragmentUtil

/**
 * @author xx
 * @version 1
 * @createTime 2022/2/24 18:08
 * @desc 首页
 */
abstract class HomeFragment<T : ViewDataBinding> :
    BaseHomeFragment<T, BaseHomeViewModel>(), IHomeData,
    IHomeStyle {

    private var mRegisterMenuList = mutableMapOf<UIMenuBean, AbsMenuView>()
    private var recommendList = mutableListOf<MenuItem>()

    override fun initView() {
        FragmentUtil.show(requireActivity().supportFragmentManager, registerHeaderFragment(), getHeaderId())
    }


    override fun getViewModel(): BaseHomeViewModel {
        return ViewModelProvider(requireActivity()).get(
            BaseHomeViewModel::class.java
        )
    }


    private fun subscribeUi() {
        mRegisterMenuList = registerMenuTemplate()
        //陪护提示
        getDayUsedLayout()?.let {
            it.removeAllViews()
            registerDayUsedView()?.apply {
                it.addView(this)
            }
        }

        mModel.mHomeDataLiveData.observe(requireActivity()) {
            val mainMenuList = it.homeList.filter { menuItem ->
                menuItem.menuType == TepConstants.Menu.HOME_MENU_MAIN
            }
            val businessMenuList = it.homeList.filter { menuItem ->
                menuItem.menuType == TepConstants.Menu.HOME_MENU_BUS
            }

            val businessPlusMenuList = it.homeList.filter { menuItem ->
                menuItem.menuType == TepConstants.Menu.HOME_MENU_BUS_PLUS
            }

            val recommendMenuList = it.homeList.filter { menuItem ->
                menuItem.menuType == TepConstants.Menu.HOME_MENU_RECOMMEND
            }

            mRegisterMenuList.forEach { (uiMenuBean, absMenuView) ->
                when (uiMenuBean.type) {
                    TepConstants.Menu.HOME_MENU_MAIN -> {
                        uiMenuBean.menuList = mainMenuList
                        getMainMenu()?.let { container ->
                            container.removeAllViews()
                            container.addView(absMenuView.getRoot())
                        }
                        if (mainMenuList.isNullOrEmpty()) {
                            emptyData(TepConstants.Menu.HOME_MENU_MAIN)
                        }
                    }
                    TepConstants.Menu.HOME_MENU_BUS -> {
                        uiMenuBean.menuList = businessMenuList
                        getBusMenu()?.let { container ->
                            container.removeAllViews()
                            container.addView(absMenuView.getRoot())
                        }
                        if (businessMenuList.isNullOrEmpty()) {
                            emptyData(TepConstants.Menu.HOME_MENU_BUS)
                        }
                    }
                    TepConstants.Menu.HOME_MENU_BUS_PLUS -> {
                        uiMenuBean.menuList = businessPlusMenuList
                        getBusPlusMenu()?.let { container ->
                            container.removeAllViews()
                            container.addView(absMenuView.getRoot())
                        }
                        if (businessMenuList.isNullOrEmpty()) {
                            emptyData(TepConstants.Menu.HOME_MENU_BUS_PLUS)
                        }
                    }

                    TepConstants.Menu.HOME_MENU_RECOMMEND -> {
                        uiMenuBean.menuList = recommendMenuList
                        getRecommendMenu()?.let { container ->
                            container.removeAllViews()
                            container.addView(absMenuView.getRoot())
                        }
                        if (recommendMenuList.isNullOrEmpty()) {
                            emptyData(TepConstants.Menu.HOME_MENU_RECOMMEND)
                        }else{
                            recommendList = recommendMenuList as MutableList<MenuItem>
                        }
                    }
                }
                absMenuView.setData(uiMenuBean)
            }
        }
        mModel.loadHomeData(getMenuData(), getBusMenuData(), getBusPlusMenuData(), getRecommendMenuData())
    }


    override fun getMenuData(): MutableList<MenuItem>? {
        return null
    }

    override fun getBusMenuData(): MutableList<MenuItem>? {
        return null
    }

    override fun getBusPlusMenuData(): MutableList<MenuItem>? {
        return null
    }

    override fun getRecommendMenuData(): MutableList<MenuItem>? {
        return null
    }


    override fun registerHeaderFragment(): Fragment {
        return CleanHeaderFragment()
    }

    override fun registerMenuTemplate(): MutableMap<UIMenuBean, AbsMenuView> {
        return HashMap()
    }

    override fun registerDayUsedView(): View? {
        return mModel.getDefaultDayUsedStyle(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        JunkClient.instance.clear()
    }

    override fun initData() {
        subscribeUi()
    }


    abstract fun getHeaderId(): Int

    abstract fun getMenuLayout(): ViewGroup

    abstract fun getDayUsedLayout(): ViewGroup?

    fun getHeadLayout():ViewGroup?{
        return null
    }

    override fun getMainMenu(): LinearLayout? {
        return null
    }

    override fun getBusMenu(): LinearLayout? {
        return null
    }

    override fun getBusPlusMenu(): LinearLayout? {
        return null
    }

    override fun getRecommendMenu(): LinearLayout? {
        return null
    }

    /**
     * 空数据回调
     * TepConstants.Menu.HOME_MENU_MAIN 主菜单数据为空
     * TepConstants.Menu.HOME_MENU_BUS 特色菜单数据为空
     * TepConstants.Menu.HOME_MENU_RECOMMEND 推荐菜单为空
     */
    protected open fun emptyData(type: Int) {

    }

    protected open fun getRecommendList(): MutableList<MenuItem> {
        return recommendList
    }

}