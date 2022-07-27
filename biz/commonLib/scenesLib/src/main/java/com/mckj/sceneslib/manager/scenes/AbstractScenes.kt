package com.mckj.sceneslib.manager.scenes

import android.content.Context
import android.content.pm.PackageInfo
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.log.VLog
import com.dn.vi.app.cm.utils.AppUtil
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.api.client.JunkConstants
import com.org.openlib.help.Consumer
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.TextUtils
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.*
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.SceneTaskProducer
import com.mckj.sceneslib.manager.strategy.helper.StrategyManager
import com.mckj.sceneslib.manager.strategy.helper.StrategyType
import com.mckj.sceneslib.manager.strategy.WeightsStrategy
import com.mckj.sceneslib.manager.strategy.helper.StrategyHelper
import com.mckj.sceneslib.ui.scenes.landing.header.*
import com.mckj.sceneslib.permission.DPermissionUtils
import com.mckj.sceneslib.sp.CommonScenesSp
import com.mckj.sceneslib.ui.scenes.landing.content.LandingContentSpeedSafetyFragment
import com.org.openlib.utils.FragmentUtil
import com.org.proxy.EvAgent
import kotlin.random.Random

/**
 * Describe:
 *
 * Created By yangb on 2021/5/31
 */
abstract class AbstractScenes() {

    private val log by lazy {
        VLog.scoped("AbstractScenes")
    }

    /**
     * 场景启动标识
     */
    var enterFlag: String = "in"

    var isOut: Boolean = false

    /**
     * 默认国外的实现
     */
    private val ovs: IOvsScenes = OvsScenes()

    /**
     * 场景数据
     */
    private var mData: ScenesData? = null

    /**
     * 初始化数据
     */
    abstract fun initData(): ScenesData

    var scenesStep: ScenesStep = ScenesStep.None

    /**
     * 落地推荐是否排除当前场景
     */
    open var isRepulsion: Boolean = false

    /**
     * 落地内容类型，推荐功能or新闻
     */
    open var landContentType: Int = 0

    /**
     * 落地推荐功能类型列表
     */
    open var landRecommendTypes: List<Int> = mutableListOf()


    /**
     * 获取引导icon
     */
    open fun getGuideIconResId() = R.drawable.ic_land_default

    open var loadAdAfterReachConditionConsumer: ((cons: Boolean) -> Unit)? = null

    /**
     * 曝光时间
     */
    var popTime = 0L


    /**
     * 跳转
     */
    open fun jumpPage(context: Context, invoke: ((accept: Boolean) -> Unit)? = null): Boolean {
        var jumpSuc = false
        if (isRequestPermission()) {
            requestPermission(context) { accept ->
                if (accept) {
                    jumpSuc = true
                    jump(context)
                }
                invoke?.invoke(accept)
            }
        } else {
            jumpSuc = true
            jump(context)
            invoke?.invoke(true)
        }
        return jumpSuc
    }

    /**
     * 跳转
     */
    open fun jumpPage(context: Context): Boolean {
        var jumpSuc = true
        jumpPage(context) { accept ->
            jumpSuc = accept
        }
        return jumpSuc
    }

    /**
     * 带场景标记的转跳
     * @param enterFlag 场景标记
     */
    fun jumpPage(context: Context, enterFlag: String): Boolean {
        this.enterFlag = enterFlag
        return jumpPage(context)
    }

    fun jump(context: Context) {
        val entity = getScenesJumpData()
        ovs.jumpPage(context, entity)
        handleClickEvent()
    }

    fun getScenesJumpData() = ScenesJumpData(
        getData().type,
        getGuideTypes(),
        getLandingBeforeAdName(),
        "clean_landing",
        "home_mfzs"
    )

    private fun getLandingBeforeAdName(): String {
        val rewardPercent = CommonScenesSp.instance.landingBeforeAdRat
        val random = (1..100).random()
        return if (random <= rewardPercent) {
            "clean_video"
        } else {
            "level_win"
        }
    }

    /**
     * 处理点击事件
     */
    protected fun handleClickEvent(customEvent: String? = null) {
        if (customEvent.isNullOrEmpty()) {
            St.stHomeFunctionClick(getData().name)
        } else {
            EvAgent.sendEvent(customEvent)
        }
        getData().exposeTimestamp = System.currentTimeMillis()
        SceneTaskProducer.getInstance().executeAutoTask(false)
    }

    /**
     * 添加场景view
     * @param consumer 消费事件。跳过/结束当前场景consumer.accept(true/false)
     */
    open fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        ovs.addScenesView(activity, parent, consumer)
    }


    /**
     * 添加任务（优化）场景
     * @param consumer 消费。跳过/结束当前场景consumer.accept(true/false)
     *
     */
    open fun addExecuteScenesFragment(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        ovs.addExecuteScenesFragment(activity, parent, consumer)
    }


    open fun addEndScenesFragment(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        ovs.addEndScenesFragment(activity, parent, consumer)
    }

    /**
     * 添加落地页头部view
     *
     * 该方法可在子场景里直接进行重写，进行头部Fragment替换
     */
    open fun addLandingHeaderView(
        activity: FragmentActivity,
        parent: ViewGroup,
        style: ScenesLandingStyle
    ) {
        val fragment = getLandingHeaderFragmentByStyle(style)
        if (fragment != null) {
            FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
        }
    }

    /**
     * 通过上层返回的style获取落地页头部fragment
     *
     * 在实现的场景中如果没有重写[addLandingHeaderView],则通过该方法判断落地页头部fragment
     */
    private fun getLandingHeaderFragmentByStyle(style: ScenesLandingStyle): Fragment? {
        val fragment = when (style) {
            ScenesLandingStyle.SPEED -> {
                LandingHeaderSpeedFragment()
            }
            ScenesLandingStyle.CLEAN -> {
                LandingHeaderCleanFragment()
            }
//            ScenesLandingStyle.NEWS -> {
//                if (ScenesManager.getInstance().isRegisterWifiBody()) {
//                    LandingHeaderSpeedFragment()
//                } else {
//                    LandingHeaderCleanFragment()
//                }
//            }
            ScenesLandingStyle.DEFAULT -> {
                LandingHeaderDefaultFragment()
            }
            else -> {
                null
            }
        }
        return fragment
    }

    /**
     * 添加落地页内容view
     *
     * 该方法可在子场景里直接进行重写，进行content Fragment替换
     */
    open fun addLandingContentView(
        activity: FragmentActivity,
        parent: ViewGroup,
        style: ScenesLandingStyle
    ) {
        val fragment = getLandingContentFragment(style)
        if (fragment != null) {
            FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
        }
    }

    /**
     * 通过上层返回的style获取落地页content fragment
     *
     * 在实现的场景中如果没有重写[addLandingContentView],则通过该方法判断落地页content fragment
     */
    private fun getLandingContentFragment(style: ScenesLandingStyle): Fragment? {
        return getRecommendFragment()
/*        val fragment = when (style) {
            ScenesLandingStyle.SPEED,
            ScenesLandingStyle.CLEAN -> {
                getRecommendFragment()
            }
            ScenesLandingStyle.NEWS -> {
                landContentType = ScenesType.LandType.TYPE_NEWS
                LandingContentNewsFragment()
            }
            else -> {
                null
            }
        }
        return fragment*/
    }

    private fun getRecommendFragment(): Fragment {
        val recommendTypes = getRecommendTypes()
        landContentType = if (!recommendTypes.isNullOrEmpty()) {
            ScenesType.LandType.TYPE_RECOMMEND
        } else {
            ScenesType.LandType.TYPE_NEWS
        }
        return LandingContentSpeedSafetyFragment()

        //国内版逻辑
/*        return if (getRecommendTypes()?.size!! < 3) {
            landContentType = ScenesType.LandType.TYPE_NEWS
            LandingContentNewsFragment()
        } else {
            landContentType = ScenesType.LandType.TYPE_RECOMMEND
            LandingContentSpeedFragment()
        }*/
    }

    /**
     * 获取扫描
     */
    open fun getLottieData(): ScenesLottieData? {
        return ovs.getLottieData()
    }

    /**
     * 获取执行
     */
    open fun getExecuteLottie(): ScenesLottieData? {
        return ovs.getExecuteLottie()
    }

    /**
     * 获取结束动画
     */
    open fun getEndLottie(): ScenesLottieData? {
        return ovs.getEndLottie()
    }

    /**
     * 获取引导类型
     */
    open fun getGuideTypes(): List<Int>? {
        // landRecommendTypes = getRecommendTypes()?.shuffled()?.take(4)!!
        landRecommendTypes = getRecommendTypes()!!
        return landRecommendTypes
    }

    /**
     * 当不同马甲包的时候，返回不同的场景排除项
     */
    open fun repulsionTypes(): MutableList<Int>? {
        return when (ScenesManager.getInstance().mVestMainBody) {
            ScenesManager.Vest.WIFI -> arrayListOf(
                ScenesType.TYPE_TOOLS,
                ScenesType.TYPE_APP_MANAGER,
                ScenesType.TYPE_ALBUM_CLEAN,
                ScenesType.TYPE_CALENDAR,
                ScenesType.TYPE_FLOW_USAGE,
                getData().type
            )


            ScenesManager.Vest.CLEAN, ScenesManager.Vest.POWER -> arrayListOf(
                ScenesType.TYPE_TOOLS,
                ScenesType.TYPE_ONE_KEY_SPEED,
                ScenesType.TYPE_CALENDAR,
                ScenesType.TYPE_FLOW_USAGE,
                ScenesType.TYPE_MAGNIFIER,
                ScenesType.TYPE_DUST,
                ScenesType.TYPE_NOTIFY,
                ScenesType.TYPE_AUDIO,
                ScenesType.TYPE_DAYS,
                ScenesType.TYPE_FONT_SCALE,
                ScenesType.TYPE_ACCOUNT,

                getData().type
            )
        }
    }

    fun checkSafeTimeState() = System.currentTimeMillis() - popTime < 3 * 60 * 1000


    open fun getRecommendTypes(): List<Int>? {
        return StrategyHelper.getInstance().doWeightsStrategy(this)
    }

    //type -> name
    open fun getNameByType(type: Int): String {
        val iterator = ScenesManager.getInstance().getScenesMap().iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next.key == type) {
                return next.value.getData().name
                break
            }
        }
        return ""
    }

    //type -> scene
    open fun getSceneByType(type: Int): AbstractScenes? {
        val iterator = ScenesManager.getInstance().getScenesMap().iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next.key == type) {
                return next.value
                break
            }
        }
        return null
    }

    /**
     * 获取警告描述,实时获取
     */
    open fun getWarningDescList(): List<String>? {
        return null
    }

    /**
     * 获取场景任务数据
     */
    open fun getTaskData(): ScenesTaskData? {
        return null
    }

    /**
     * 获取场景数据
     */
    fun getData(): ScenesData {
        var data = mData
        if (data == null) {
            data = initData()
            mData = data
        }
        return data
    }

    open fun getCleanLandingDesc(
        totalSize: Long,
        fileSize: Long
    ): CharSequence {
        val list: MutableList<CharSequence> = mutableListOf()
        var junkDesc = ""
        junkDesc = if (totalSize <= 10L * 1024 * 1024) {
            //10到15浮点随机值，保留一位小数
            val size = "${(10..15).random()}.${(0..9).random()}"
            "${size}MB"
        } else {
            "${FileUtil.getFileSizeNumberText(totalSize)}${
                FileUtil.getFileSizeUnitText(
                    totalSize
                )
            }"
        }
        val descOne = TextUtils.string2SpannableStringForSize(
            String.format(ResourceUtil.getString(R.string.scenes_x_memory_junk_clean), junkDesc),
            junkDesc,
            sizeDip = 22,
        )

        val value = if (fileSize <= 10L) {
            (10..15).random()
        } else {
            fileSize
        }.toString()

        val descTwo = TextUtils.string2SpannableStringForSize(
            String.format(ResourceUtil.getString(R.string.scenes_x_junk_flie_clean), value),
            value,
            sizeDip = 22,
        )
        if (list.isNullOrEmpty()) {
            list.add(descOne)
            list.add(descTwo)
        }
        return list.shuffled().take(1)[0]
    }


    open fun isScenesAutoRefresh(): Boolean {
        return false
    }

    open fun executorTask(block: (() -> Unit)? = null) {

    }
    open fun stopTask(){

    }
    open fun isRequestPermission(): Boolean {
        return false
    }

    fun requestPermission(context: Context, block: (accept: Boolean) -> Unit) {
        DPermissionUtils.requestFileScanPermission(context, getData().name) {
            block.invoke(it)
        }
    }

    open fun loadAdAfterCondition(): Boolean {
        return false
    }

    /**
     * 检查是否安装app
     * JunkType.WECHAT
     * unkType.QQ
     */
    open fun checkInstall(type: Int): Boolean {
        val packageInfoList: List<PackageInfo> = AppUtil.getInstalledAppInfoList(AppMod.app)
        var isInstall = false
        when (type) {
            JunkConstants.Session.WECHAT_CACHE -> {
                packageInfoList.forEach {
                    if (it.packageName.equals("com.tencent.mm")) {
                        isInstall = true
                    }
                }
            }
            JunkConstants.Session.QQ_CACHE -> {
                packageInfoList.forEach {
                    if (it.packageName.equals("com.tencent.mobileqq")) {
                        isInstall = true
                    }
                }
            }
        }
        return isInstall
    }
}