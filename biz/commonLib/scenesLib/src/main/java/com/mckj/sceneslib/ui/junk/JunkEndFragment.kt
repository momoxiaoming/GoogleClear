package com.mckj.sceneslib.ui.junk

import androidx.lifecycle.ViewModelProvider
import com.org.openlib.help.Consumer
import com.mckj.baselib.util.ResourceUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.ui.overseas.end.OvsScenesEndFragment

class JunkEndFragment : OvsScenesEndFragment() {


    companion object {
        const val TAG = "ScenesAnimFragment"

        fun newInstance(consumer: Consumer<Boolean>): JunkEndFragment {
            return JunkEndFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    private val mJunkModel by lazy {
        ViewModelProvider(requireActivity(), JunkViewModelFactory()).get(
            JunkViewModel::class.java
        )
    }

    override fun startAnim(consumer: Consumer<Boolean>) {
        val mTotalSize = mJunkModel.mTotalSize
        if (mTotalSize==0L){
            mBinding.taskNameTv.text = ResourceUtil.getText(R.string.scenes_junk_perfect)
        }
        super.startAnim(consumer)
    }

    override fun preFinish(result: Boolean) {
        St.stLevelFlashEnd(mModel.enterFlag,mModel.getScenesData().name)

        mConsumer?.accept(result)
    }
}