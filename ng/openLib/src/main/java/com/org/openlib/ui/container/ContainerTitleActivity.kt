package com.org.openlib.ui.container

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.org.openlib.R
import com.org.openlib.databinding.OpenActivityContainerTitleBinding
import com.org.openlib.path.ARouterPath
import com.org.openlib.ui.container.ContainerActivity
import com.org.openlib.utils.SystemUiUtil

/**
 * Describe:
 *
 * Created By yangb on 2020/10/22
 */
@Route(path = ARouterPath.ACTIVITY_CONTAINER_TITLE)
class ContainerTitleActivity : ContainerActivity() {

    private lateinit var mBinding: OpenActivityContainerTitleBinding

    override fun onInternalCreate(savedInstanceState: Bundle?) {
        mBinding = DataBindingUtil.setContentView(this, R.layout.open_activity_container_title)
        SystemUiUtil.immersiveSystemUi(window)
        mBinding.headerLayout.headerToolbar.apply {
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun getContainerId(): Int {
        return mBinding.containerLayout.id
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
        mBinding.headerLayout.headerToolbar.title = title
    }

    override fun setTitle(titleId: Int) {
        super.setTitle(titleId)
        mBinding.headerLayout.headerToolbar.setTitle(titleId)
    }

}