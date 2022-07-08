package com.mckj.sceneslib.ui.scenes.model.tools

import android.content.Context
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.util.Log
import com.mckj.baselib.util.launch
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType
import com.mckj.sceneslib.ui.scenes.model.tools.data.entity.MenuToolsItem
import com.mckj.sceneslib.ui.scenes.model.tools.data.entity.ToolsMenuData
import com.mckj.sceneslib.ui.scenes.model.tools.data.model.IToolsData
import com.mckj.sceneslib.ui.scenes.model.tools.utils.FlashUtils
import com.mckj.datalib.entity.ARouterPath
import com.org.openlib.helper.startFragment
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.SystemJumpManager
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.model.tools.dialog.openFailDialog


/**
 * Describe:
 *
 * Created By yangb on 2021/3/1
 */
class ToolsViewModel(iToolsData: IToolsData) : AbstractViewModel() {

    /**
     * 工具菜单列表
     */
    val mToolsDataLiveData = MutableLiveData<ToolsMenuData>()

    val mToolsData: IToolsData = iToolsData

    companion object {
        const val TAG = "ToolsViewModel"
    }

    init {
        launch {
            mToolsDataLiveData.value = loadToolsData()
        }
    }

    private suspend fun loadToolsData(): ToolsMenuData {
        val toolsList = mToolsData.getToolsMenuList()
        //填充列表
        toolsList.forEach {
            it.resId = mToolsData.getMenuResId(it.type)
        }
        return ToolsMenuData(toolsList)
    }

    /**
     * 更新配置
     */
    fun updateMenuConfig(adapter: RecyclerView.Adapter<*>, position: Int, menuItem: MenuToolsItem) {
        menuItem.update = System.currentTimeMillis()
        if (!menuItem.isOpenFlash) {
            menuItem.isOpenFlash = true
            adapter.notifyItemChanged(position)
        } else {
            menuItem.isOpenFlash = false
            adapter.notifyItemChanged(position)
        }
        launch {
            val list: List<MenuToolsItem> =
                mToolsDataLiveData.value?.toolsList ?: return@launch
            mToolsData.saveToolsMenuList(list)
        }
    }

    /**
     * 菜单监听
     */
    fun menuClickListener(context: Context, menuItem: MenuToolsItem) {
        val type = menuItem.type
        val name = ToolsType.getNameByType(type)
        St.stKitFunctionClick(name)
        when (type) {
            //天气跳转，之前优先自研天气，现在直接跳系统天气
            ToolsType.TYPE_WEATHER -> {
                try {
                    openWeatherSystem(context)
                } catch (e: Exception) {
                    e.printStackTrace()
                    openFailDialog.show(context)
                }
            }

            //日历跳转，之前优先自研日历，现在跳系统日历
            ToolsType.TYPE_CALENDAR -> {
                try {
                    openCalendarSystem(context)
                } catch (e: Exception) {
                    e.printStackTrace()
                    openFailDialog.show(context)
                }
            }

            ToolsType.TYPE_FLOW_USING -> {
                openFlow(context)
            }

            ToolsType.TYPE_PHONE_PARAMETERS -> {
                openParam(context)
            }

            ToolsType.TYPE_FLASHLIGHT -> {
                if (!FlashUtils.open_status) {
                    try {
                        FlashUtils.open()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        openFailDialog.show(context)
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        try {
                            FlashUtils.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            openFailDialog.show(context)
                        }
                    }
                }
            }

            ToolsType.TYPE_FONT_SIZE -> {
                openFontSize(context)
            }


            ToolsType.TYPE_ALARM_CLOCK -> {
                openClock(context)
            }

            ToolsType.TYPE_ALBUM -> {
                openAlbum(context)
            }
            ToolsType.TYPE_DUST->{
                openDust(context)
            }
            ToolsType.TYPE_MAGNIFIER->{
                openMagnifier(context)
            }
            ToolsType.TYPE_INCREASE_VOICE->{
                openAudio(context)
            }
            ToolsType.TYPE_NOTIFY_CLEAR->{
                openNotify(context)
            }
            ToolsType.TYPE_CHECK_NET_WORK->{
                openCheckNetwork(context)
            }
            ToolsType.TYPE_FONT_SCALE->{
                openFontScale(context)
            }
            ToolsType.TYPE_ACCOUNT->{
                openAccount(context)
            }
            ToolsType.TYPE_DAYS->{
                openDays(context)
            }
            else -> {
                openFailDialog.show(context)
            }
        }
    }

    private fun openMagnifier(context: Context){
        context.startFragment(ARouterPath.NewTools.NEW_TOOLS_MAGNIFIER)
    }

    private fun openDust(context: Context){
        ScenesManager.getInstance().jumpPage(context, ScenesType.TYPE_DUST)
    }

    private fun openNotify(context: Context){
        ScenesManager.getInstance().jumpPage(context, ScenesType.TYPE_NOTIFY)
    }

    private fun openAudio(context: Context){
        ScenesManager.getInstance().jumpPage(context, ScenesType.TYPE_AUDIO)
    }

    private fun openCheckNetwork(context: Context){
        ScenesManager.getInstance().jumpPage(context, ScenesType.TYPE_CHECK_NET_WORK)
    }

    private fun openFontScale(context: Context){
        ScenesManager.getInstance().jumpPage(context, ScenesType.TYPE_FONT_SCALE)
    }

    private fun openAccount(context: Context){
        ScenesManager.getInstance().jumpPage(context, ScenesType.TYPE_ACCOUNT)
    }

    private fun openDays(context: Context){
        ScenesManager.getInstance().jumpPage(context, ScenesType.TYPE_DAYS)
    }

    private fun openWeatherSystem(context: Context) {
        val result = SystemJumpManager.getInstance().getJumpPage().jumpWeather(context)
        Log.i(TAG, "openWeatherSystem: result:$result")
    }

    private fun openCalendarSystem(context: Context) {
        val result = SystemJumpManager.getInstance().getJumpPage().jumpCalender(context)
        Log.i(TAG, "openCalendarSystem: result:$result")
    }

    private fun openFontSize(context: Context) {
        val result = SystemJumpManager.getInstance().getJumpPage().jumpFontSize(context)
        Log.i(TAG, "openFontSize: result:$result")
    }

    private fun openAlbum(context: Context) {
        val result = SystemJumpManager.getInstance().getJumpPage().jumpAlbum(context)
        Log.i(TAG, "openAlbum: result:$result")
    }

    private fun openFlow(context: Context) {
        val result = SystemJumpManager.getInstance().getJumpPage().jumpNetData(context)
        Log.i(TAG, "openFlow: result:$result")
    }

    private fun openParam(context: Context) {
        val result = SystemJumpManager.getInstance().getJumpPage().jumpParam(context)
        Log.i(TAG, "openParam: result:$result")
    }

    private fun openClock(context: Context) {
        val result = SystemJumpManager.getInstance().getJumpPage().jumpClock(context)
        Log.i(TAG, "openClock: result:$result")
    }
}
