package com.mckj.module.cleanup.ui.appManager

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.SizeUtil
import com.mckj.datalib.entity.ARouterPath
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.data.model.appManagerViewModel
import com.mckj.module.cleanup.entity.AppInfoHolder
import com.mckj.module.cleanup.ui.adapter.MyPagerAdapter
import com.mckj.module.cleanup.ui.dialog.OpenFailDialog
import com.mckj.module.cleanup.util.Log
import com.mckj.module.gen.St
import com.mckj.module.utils.EventTrack
import com.org.openlib.utils.SystemUiUtil
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.dip
import org.jetbrains.anko.matchParent


@Route(path = ARouterPath.Cleanup.ACTIVITY_APP_MANAGER)
class AppManagerActivity : AppCompatActivity() {

    private var viewPager: ViewPager? = null
    private var fragmentList: ArrayList<Fragment>? = null
    private var fragAdapter: MyPagerAdapter? = null
    private var tabLayout: TabLayout? = null
    private lateinit var searchEt: EditText
    private lateinit var searchBt: ImageView
    private lateinit var searchCloseBt: ImageView
    private lateinit var middleText: LinearLayout
    private lateinit var uninstallBtn: LinearLayout
    private lateinit var uninstallBtnSize: TextView
    private lateinit var title: LinearLayout
    private lateinit var titleToolBar: Toolbar

    //下拉列表list
    private lateinit var listPopData: List<String>
    private var popupWindow: PopupWindow? = null
    private lateinit var oneTab: TabLayout.TabView
    private lateinit var twoTab: TabLayout.TabView
    private lateinit var threeTab: TabLayout.TabView

    private lateinit var uninstallBt: LinearLayout

    //使用权限，控制使用频率tab是否展示
    var isPermission: Boolean = false

    //viewModel
    private lateinit var mViewModel: appManagerViewModel

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cleanup_fragment_app_manager_main)

        initView()
        initData()
    }


    private fun initView() {
        SystemUiUtil.immersiveSystemUi(window)
        title = findViewById(R.id.title_layout)
        titleToolBar = findViewById(R.id.header_toolbar)
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabs)
        searchEt = findViewById(R.id.search)
        searchBt = findViewById(R.id.search_button)
        searchCloseBt = findViewById(R.id.search_close_button)
        middleText = findViewById(R.id.middle_text)
        uninstallBtn = findViewById(R.id.unInstall_btn)
        uninstallBtnSize = findViewById(R.id.unInstall_btn_size)
        uninstallBt = findViewById(R.id.unInstall_btn)!!

        mViewModel = ViewModelProvider(this).get(appManagerViewModel::class.java)

        //observe

        mViewModel.checkedSize.observe(this, Observer {
            if (!mViewModel.checkedSize.value.equals("")
                and !mViewModel.checkedSize.value?.startsWith("(0")!!
                and !mViewModel.checkedSize.value?.startsWith("(-")!!
            ) {
                uninstallBtnSize.visibility = View.VISIBLE
                uninstallBtnSize.text = mViewModel.checkedSize.value
            } else {
                uninstallBtnSize.visibility = View.GONE
            }
        })

        mViewModel.checkList.observe(this, Observer {
        })

        mViewModel.isUninstallClick.observe(this, Observer {
        })

        //标题
        title.backgroundResource = android.R.color.transparent
        titleToolBar.apply {
            navigationIcon = ResourceUtil.getDrawable(R.drawable.app_manager_back)
            setNavigationOnClickListener {
                finish()
            }
            setTitleTextColor(Color.parseColor("#FFFFFF"))
            title = ResourceUtil.getString(R.string.cleanup_app_manager)
        }

        //搜索框点击变大
        searchEt.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
            if (searchEt.isFocused) {
                playExpandAnim {
                    middleText.visibility = View.GONE
                    searchBt.visibility = View.GONE
                    searchCloseBt.visibility = View.VISIBLE
                }
            }
        }
        searchCloseBt.setOnClickListener {
            playShrinkAnim {
                searchEt.clearFocus()
                searchEt.setText("")
                middleText.visibility = View.VISIBLE
                searchCloseBt.visibility = View.GONE
                searchBt.visibility = View.VISIBLE
                hideSoftKeyboard()
            }
//
//            //隐藏键盘
//
//            mViewModel.mSelectedTotal.observe(this,{
//                uninstall.text = "卸载：$it"
//            })
        }

        searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                mViewModel.mEditTextLivaData.postValue(s.toString())
            }
        })

        uninstallBt.setOnClickListener {
            EventTrack.stManagementUnloadClick()
            when (fragAdapter?.getCurrentFragment()) {
                is AppManagerFragmentUseFrequency -> {
                    mViewModel.sharedAdapter[1]?.unInstallChecked()
                }
                is AppManagerFragmentAppSize -> {
                    mViewModel.sharedAdapter[2]?.unInstallChecked()
                }
                else -> {
                    mViewModel.sharedAdapter[3]?.unInstallChecked()
                }
            }
        }
    }

    private fun playExpandAnim(block: () -> Unit) {
        val valueAnimator =
            ValueAnimator.ofInt(SizeUtil.dp2px(120f), SizeUtil.dp2px(333f)).setDuration(400)
        valueAnimator.addUpdateListener {
            searchEt.layoutParams.width = it.animatedValue as Int
            searchEt.requestLayout()
        }
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                block.invoke()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationRepeat(animation: Animator?) {}
        })
        valueAnimator.start()
    }

    private fun playShrinkAnim(block: () -> Unit) {
        val valueAnimator =
            ValueAnimator.ofInt(SizeUtil.dp2px(333f), SizeUtil.dp2px(120f)).setDuration(400)
        valueAnimator.addUpdateListener {
            searchEt.layoutParams.width = it.animatedValue as Int
            searchEt.requestLayout()
        }
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                block.invoke()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationRepeat(animation: Animator?) {}
        })
        valueAnimator.start()
    }

    private class ViewWrapper(view: View) {
        private var mTarget: View = view

        fun setTrueWidth(width: Int) {
            mTarget.layoutParams.width = width
            mTarget.requestLayout()
        }

        fun getTrueWidth(): Int {
            return mTarget.layoutParams.width;
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun initData() {
        //默认为没权限，当前版本让所有机型不展示使用频率tab
        isPermission = false

        val list: ArrayList<String> = ArrayList()
        list.add(ResourceUtil.getString(R.string.cleanup_frequency))
        list.add(ResourceUtil.getString(R.string.cleanup_app_size))
        list.add(ResourceUtil.getString(R.string.cleanup_install_date))

        //权限判断
        if (!isPermission) {
            list.removeAt(0)
        } else {
            AppInfoHolder.initTimeList(this)
        }

        //listener

        //tab数据
        for (i in list.indices) {
            tabLayout?.addTab(tabLayout!!.newTab().setText(list[i]))
        }

        initTabView(list, R.drawable.app_manager_pop_up)

        if (isPermission) {
            val tabOne: TabLayout.Tab? = tabLayout?.getTabAt(0)
            val tabTwo: TabLayout.Tab? = tabLayout?.getTabAt(1)
            val tabThree: TabLayout.Tab? = tabLayout?.getTabAt(2)

            oneTab = tabOne?.view!!
            twoTab = tabTwo?.view!!
            threeTab = tabThree?.view!!

            //viewPager数据
            fragmentList = ArrayList()
            fragmentList!!.add(AppManagerFragmentUseFrequency())
            fragmentList!!.add(AppManagerFragmentAppSize())
            fragmentList!!.add(AppManagerFragmentInstallTime())
        } else {
            val tabTwo: TabLayout.Tab? = tabLayout?.getTabAt(0)
            val tabThree: TabLayout.Tab? = tabLayout?.getTabAt(1)

            twoTab = tabTwo?.view!!
            threeTab = tabThree?.view!!

            //viewPager数据
            fragmentList = ArrayList()
            fragmentList!!.add(AppManagerFragmentAppSize())
            fragmentList!!.add(AppManagerFragmentInstallTime())
        }


        fragAdapter = MyPagerAdapter(supportFragmentManager, fragmentList!!)
        viewPager?.adapter = fragAdapter
        viewPager?.currentItem = 0
        viewPager?.offscreenPageLimit = 3
        viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        //tab数据
        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                if (position != null) {
                    viewPager?.currentItem = position
                    mViewModel.checkList.value?.clear()
                    if (isPermission) {
                        when (position) {
                            //默认选中触发排序
                            0 -> mViewModel.checkClick(1, true)
                            1 -> mViewModel.checkClick(3, true)
                            2 -> mViewModel.checkClick(5, true)
                        }
                    } else {
                        when (position) {
                            //默认选中触发排序
                            0 -> mViewModel.checkClick(3, true)
                            1 -> mViewModel.checkClick(5, true)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (fragAdapter?.getCurrentFragment()) {
                    is AppManagerFragmentUseFrequency -> {
                        if (popupWindow == null || !popupWindow!!.isShowing) {
                            showPopWindow("useFrequency")
                        }
                        initTabViewAtIndex(R.drawable.app_manager_pop_down, 0)
                    }

                    is AppManagerFragmentAppSize -> {
                        if (popupWindow == null || !popupWindow!!.isShowing) {
                            showPopWindow("appSize")
                        }
                        if(isPermission) {
                            initTabViewAtIndex(R.drawable.app_manager_pop_down, 1)
                        }else{
                            initTabViewAtIndex(R.drawable.app_manager_pop_down, 0)
                        }
                    }

                    is AppManagerFragmentInstallTime -> {
                        if (popupWindow == null || !popupWindow!!.isShowing) {
                            showPopWindow("installTime")
                        }
                        if(isPermission) {
                            initTabViewAtIndex(R.drawable.app_manager_pop_down, 2)
                        }else{
                            initTabViewAtIndex(R.drawable.app_manager_pop_down, 1)
                        }
                    }
                }
            }
        })

    }

    private fun initTabView(list: List<String>, imageId: Int) {
        for (i in list.indices) {
            val view: View = LayoutInflater.from(this)
                .inflate(R.layout.cleanup_app_manager_tab_item, null)
            val text = view.findViewById<TextView>(R.id.tv)
            val image = view.findViewById<ImageView>(R.id.popup_item_checkbox)
            text.text = list[i]
            image.background = resources.getDrawable(imageId)
            tabLayout?.getTabAt(i)?.customView = view
        }
    }

    private fun initTabViewAtIndex(imageId: Int, index: Int) {
        val list: ArrayList<String> = ArrayList()
        list.add(ResourceUtil.getString(R.string.cleanup_frequency))
        list.add(ResourceUtil.getString(R.string.cleanup_app_size))
        list.add(ResourceUtil.getString(R.string.cleanup_install_date))

        if(!isPermission){
            list.removeAt(0)
        }

        val view: View = LayoutInflater.from(this)
            .inflate(R.layout.cleanup_app_manager_tab_item, null)
        val text = view.findViewById<TextView>(R.id.tv)
        val image = view.findViewById<ImageView>(R.id.popup_item_checkbox)

        image.background = resources.getDrawable(imageId)
        text.text = list[index]

        tabLayout?.getTabAt(index)?.customView = null
        tabLayout?.getTabAt(index)?.customView = view
    }

    private fun showPopWindow(type: String) {


        //1.PopupWindow即将显示的布局
        val contentView: View = LayoutInflater.from(this)
            .inflate(
                when (type) {
                    "useFrequency" -> R.layout.cleanup_app_manager_popup_window_use_frequency
                    "appSize" -> R.layout.cleanup_app_manager_popup_window_app_size
                    else -> R.layout.cleanup_app_manager_popup_window_install_time
                }, null
            )

        //初始化选中按钮
        val btnOne: RadioButton = contentView.findViewById(R.id.btnOne)
        val btnTwo: RadioButton = contentView.findViewById(R.id.btnTwo)
        when (type) {
            "useFrequency" -> {
                if (mViewModel.checkList.value?.get(1) == true) {
                    btnOne.isChecked = true
                }
                if (mViewModel.checkList.value?.get(2) == true) {
                    btnTwo.isChecked = true
                }
            }
            "appSize" -> {
                if (mViewModel.checkList.value?.get(3) == true) {
                    btnOne.isChecked = true
                }
                if (mViewModel.checkList.value?.get(4) == true) {
                    btnTwo.isChecked = true
                }
            }
            else -> {
                if (mViewModel.checkList.value?.get(5) == true) {
                    btnOne.isChecked = true
                }
                if (mViewModel.checkList.value?.get(6) == true) {
                    btnTwo.isChecked = true
                }
            }
        }

        val radioGroup: RadioGroup = contentView.findViewById(R.id.popupList)

        //2.poupWindow宽高
        popupWindow = PopupWindow(contentView, twoTab.width + 100, 200)

        //点击其他区域关闭PopUpWindow
        popupWindow!!.setBackgroundDrawable(resources.getDrawable(R.drawable.bg_pop_window))
        popupWindow!!.isOutsideTouchable = true

        //显示在某一个控件的下方，后面两个参数表示x,y轴 的偏移量
        when (type) {
            "useFrequency" -> {
                popupWindow!!.showAsDropDown(oneTab, 0, 0)
            }
            "appSize" -> popupWindow!!.showAsDropDown(twoTab, 0, 0)
            "installTime" -> popupWindow?.showAsDropDown(threeTab, 0, 0)
        }

        //设置监听
        radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                mViewModel.checkList.value?.clear()

                when (checkedId) {
                    R.id.btnOne -> {
                        when (type) {
                            "useFrequency" -> {
                                EventTrack.stManagementFrequencyOldestClick()
                                mViewModel.checkClick(1, true)
                            }
                            "appSize" -> {
                                EventTrack.stManagementSizeLargestClick()
                                mViewModel.checkClick(3, true)
                            }
                            "installTime" -> {
                                EventTrack.stManagementDateLatelyClick()
                                mViewModel.checkClick(5, true)
                            }
                        }
                        popupWindow?.dismiss()
                    }

                    R.id.btnTwo -> {
                        when (type) {
                            "useFrequency" -> {
                                EventTrack.stManagementFrequencyLatelyClick()
                                mViewModel.checkClick(2, true)
                            }
                            "appSize" -> {
                                EventTrack.stManagementSizeSmallestClick()
                                mViewModel.checkClick(4, true)
                            }
                            "installTime" -> {
                                EventTrack.stManagementDateOldestClick()
                                mViewModel.checkClick(6, true)
                            }
                        }
                        popupWindow?.dismiss()
                    }
                }
            }
        })

        popupWindow?.setOnDismissListener {
            if(isPermission) {
                when (type) {
                    "useFrequency" -> {
                        initTabViewAtIndex(R.drawable.app_manager_pop_up, 0)
                    }

                    "appSize" -> {
                        initTabViewAtIndex(R.drawable.app_manager_pop_up, 1)
                    }

                    "installTime" -> {
                        initTabViewAtIndex(R.drawable.app_manager_pop_up, 2)
                    }
                }
            }else{
                when (type) {
                    "appSize" -> {
                        initTabViewAtIndex(R.drawable.app_manager_pop_up, 0)
                    }

                    "installTime" -> {
                        initTabViewAtIndex(R.drawable.app_manager_pop_up, 1)
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onResume() {
        super.onResume()
        Log.i(
            "appPermission", " permission granted: " +
                    "${AppInfoHolder.isGrantedUsagePremission(this)}"
        )
    }

    //隐藏键盘
    private fun Activity.hideSoftKeyboard() {
        val view = window?.peekDecorView()
        hideKeyboard(view?.windowToken)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(ev?.action == MotionEvent.ACTION_DOWN){
            val view: View? = currentFocus
            if(isShouldHideKeyboard(view,ev)){
                hideKeyboard(view?.windowToken)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，
     * 来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     */
    private fun hideKeyboard(token: IBinder?) {
        if (token != null) {
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}