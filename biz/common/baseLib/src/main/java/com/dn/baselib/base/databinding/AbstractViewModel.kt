package com.dn.baselib.base.databinding
import androidx.lifecycle.MutableLiveData
import com.dn.baselib.ui.view.loading.LoadingItem
import com.dn.vi.app.base.arch.mvvm.AbsAppViewModel

/**
 * Describe:BaseViewModel
 *
 * Created By yangb on 2020/9/25
 */

abstract class AbstractViewModel : AbsAppViewModel() {

    /**
     * 是否结束，true时，界面退出
     */
    val isFinish = MutableLiveData<Boolean>()


    /**
     * 是否显示loadingDialog
     */
    val mLoadingDialog = MutableLiveData<LoadingItem>()

}