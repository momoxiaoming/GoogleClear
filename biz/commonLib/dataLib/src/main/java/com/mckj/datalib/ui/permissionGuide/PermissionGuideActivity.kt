package com.mckj.datalib.ui.permissionGuide

import android.os.Bundle
import android.view.View
import com.mckj.baselib.base.AbstractActivity
import com.mckj.datalib.R
import com.org.openlib.utils.onceClick

/**
 * Describe:
 *
 * Created By yangb on 2020/11/17
 */
class PermissionGuideActivity : AbstractActivity() {

    private lateinit var mEmptyView: View

    override fun getLayoutId(): Int {
        return R.layout.data_activity_permission_guide
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView() {
//        SystemUiUtil.immersiveSystemUi(window)
        mEmptyView = findViewById(R.id.permission_guide_empty_view)

        mEmptyView.onceClick {
            goBack()
        }
    }

}