package com.mckj.vest

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.adapter.GridDividerItemDecoration
import com.dn.vi.app.base.view.drawable.DividerDrawable
import com.mckj.baselib.util.SizeUtil
import com.mckj.datalib.entity.ARouterPath
import com.mckj.template.HomeFragment
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.template.abs.AbsMenuView
import com.mckj.template.entity.TepConstants
import com.mckj.template.entity.UIMenuBean
import com.mckj.template.template.MenuTemplate
import com.mckj.vest.databinding.CleanupFragmentHomeVestBinding
import com.mckj.vest.style.VestBusMenuStyle
import com.mckj.vest.style.VestMainMenuStyle
import com.mckj.vest.style.VestRecommendMenuStyle


@Route(path = ARouterPath.Cleanup.FRAGMENT_HOME)
class VestHomeFragment : HomeFragment<CleanupFragmentHomeVestBinding>() {


    override fun initView() {
        super.initView()
    }

    override fun getHeaderId(): Int {
        return R.id.content_layout
    }

    override fun getMenuLayout(): ViewGroup {
        return mBinding.menuLayout
    }

    override fun getDayUsedLayout(): ViewGroup? {
        return null
    }

    override fun registerHeaderFragment(): Fragment {
        return VestCleanHeaderFragment()
    }

    override fun registerMenuTemplate(): MutableMap<UIMenuBean, AbsMenuView> {
        val map = HashMap<UIMenuBean, AbsMenuView>()
        map[UIMenuBean(TepConstants.Menu.HOME_MENU_MAIN, "")] =
            getMainMenu(3)
        map[UIMenuBean(
            TepConstants.Menu.HOME_MENU_BUS,
            ""
        )] = getBusMenu(2)
        map[UIMenuBean(
            TepConstants.Menu.HOME_MENU_RECOMMEND,
            ""
        )] = getRecommendMenu(4)
        return map
    }

    /**
     * 获取主菜单 样式
     */
    private fun getMainMenu(spanSize: Int): AbsMenuView {
        val menuView = MenuTemplate(requireActivity(), spanSize)
        menuView.bindStyle(VestMainMenuStyle())
        menuView.setDecoration(GridDividerItemDecoration(SizeUtil.dp2px(25f), spanSize))
        return menuView
    }


    /**
     * 特色功能 样式
     */
    private fun getBusMenu(spanSize: Int): AbsMenuView {
        val menuView =
            MenuTemplate(requireActivity(), spanSize)
        menuView.bindStyle(VestBusMenuStyle())
        val decoration = DividerItemDecoration(
            requireActivity(),
            RecyclerView.HORIZONTAL
        ).also { decor ->
            decor.setDrawable(DividerDrawable(SizeUtil.dp2px(10f)).also { })
        }
        menuView.setDecoration(decoration)
        return menuView
    }

    /**
     * 推荐功能 样式
     */
    private fun getRecommendMenu(spanSize: Int): AbsMenuView {
        val menuView =
            MenuTemplate(requireActivity(), spanSize)
        menuView.bindStyle(VestRecommendMenuStyle())
        menuView.setDecoration(GridDividerItemDecoration(SizeUtil.dp2px(35f), spanSize))
        return menuView
    }

    /**
     * 主菜单数据
     */
    override fun getMenuData(): MutableList<MenuItem>? {
        val list = mutableListOf<MenuItem>()
        list.add(mModel.getMenuItem(type = ScenesType.TYPE_PHONE_SPEED))
        list.add(mModel.getMenuItem(type = ScenesType.TYPE_COOL_DOWN))
        list.add(mModel.getMenuItem(type = ScenesType.TYPE_POWER_SAVE))
//        list.add(mModel.getMenuItem(type = ScenesType.TYPE_TOOLS))
//        list.add(mModel.getMenuItem(type = ScenesType.TYPE_ALBUM_CLEAN))
//        list.add(mModel.getMenuItem(type = ScenesType.TYPE_CATON_SPEED))
        return list
    }

    override fun getBusMenuData(): MutableList<MenuItem>? {
        val list = mutableListOf<MenuItem>()
        list.add(mModel.getBusMenuItem(type = ScenesType.TYPE_ALBUM_CLEAN))
        list.add(mModel.getBusMenuItem(type = ScenesType.TYPE_APP_MANAGER))
        list.add(mModel.getBusMenuItem(type = ScenesType.TYPE_CATON_SPEED))
        list.add(mModel.getBusMenuItem(type = ScenesType.TYPE_UNINSTALL_CLEAN))
        list.add(mModel.getBusMenuItem(type = ScenesType.TYPE_JUNK_CLEAN))
        list.add(mModel.getBusMenuItem(type = ScenesType.TYPE_ONE_KEY_SPEED))
        return list
    }


    override fun getRecommendMenuData(): MutableList<MenuItem>? {
//        val list = mutableListOf<MenuItem>()
//        list.add(mModel.getRecommendMenuItem(type = ScenesType.TYPE_ENVELOPE_TEST))
//        list.add(mModel.getRecommendMenuItem(type = ScenesType.TYPE_SHORT_VIDEO_CLEAN))
//        list.add(mModel.getRecommendMenuItem(type = ScenesType.TYPE_APP_MANAGER))
//        list.add(mModel.getRecommendMenuItem(type = ScenesType.TYPE_NETWORK_TEST))
//        list.add(mModel.getRecommendMenuItem(type = ScenesType.TYPE_SIGNAL_SPEED))
//        list.add(mModel.getRecommendMenuItem(type = ScenesType.TYPE_UNINSTALL_CLEAN))
//        list.add(mModel.getRecommendMenuItem(type = ScenesType.TYPE_ANTIVIRUS))
//        list.add(mModel.getRecommendMenuItem(type = ScenesType.TYPE_NETWORK_SPEED))
        return null
    }

    override fun getLayoutId(): Int {
        return R.layout.cleanup_fragment_home_vest
    }

    override fun getMainMenu(): LinearLayout? {
        return mBinding.mainMenu
    }

    override fun getBusMenu(): LinearLayout? {
        return mBinding.busMenu
    }

    override fun getRecommendMenu(): LinearLayout? {
        return null
    }
}