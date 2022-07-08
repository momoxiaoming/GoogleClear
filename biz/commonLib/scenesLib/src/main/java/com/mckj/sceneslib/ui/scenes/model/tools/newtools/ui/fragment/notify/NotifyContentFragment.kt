package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.notify

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.org.openlib.help.Consumer
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.datalib.entity.ARouterPath
import com.org.openlib.helper.startFragment
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.NotifyFragmentContentBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter.NotifyAdapter
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.NotifyEmptyEvent
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.NotifyInfo
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.NotifySingle
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.dust.DustFragment
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.LottieHelper
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.notify.NotifyHelper
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.notify.NotifyItemHelperCallback
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.notify.NotifyService
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm.NotifyContentViewModel
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm.obtainViewModel
import com.mckj.sceneslib.util.Log

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.notify
 * @data  2022/3/4 15:52
 */
@Route(path = ARouterPath.NewTools.NEW_TOOLS_NOTIFY_CONTENT)
class NotifyContentFragment :
    DataBindingFragment<NotifyFragmentContentBinding, NotifyContentViewModel>() {

    companion object{
        fun newInstance(consumer: Consumer<Boolean>): NotifyContentFragment {
            return NotifyContentFragment().also {
                it.mConsumer = consumer
            }
        }
    }
    private var mConsumer: Consumer<Boolean>? = null

    private val mNotifyItemHelper by lazy { NotifyItemHelperCallback() }
    private val mNotifyAdapter by lazy { NotifyAdapter() }



    override fun initData() {
        NotifyHelper.startNotifyService(requireContext())
        LottieHelper.preLottie(mBinding.notifyCleanLottie,LottieHelper.NOTIFY_CLEAN_LOTTIE,0, end = {
            mConsumer?.accept(true)
        })
    }
    override fun getLayoutId(): Int = R.layout.notify_fragment_content
    override fun getViewModel(): NotifyContentViewModel = obtainViewModel()
    override fun initView() {
        St.stNoticeCleanShow()
        initEvent()
        mBinding.apply {
            notifyNewTitleBar.apply {
                setTitle("通知栏清理")
                setTitleBarBgColor(R.color.notify_color)
                // showSetIcon()
                setTitleBarListener({
                    requireActivity().finish()
                },
                    {
                        requireActivity().startFragment(ARouterPath.NewTools.NEW_TOOLS_NOTIFY_APP)
                    })
            }
            notifyContent.layoutManager = LinearLayoutManager(requireContext())
            notifyContent.adapter = mNotifyAdapter
            val itemTouchHelper = ItemTouchHelper(mNotifyItemHelper)
          //  itemTouchHelper.attachToRecyclerView(notifyContent)
        }
    }


    private fun initEvent() {
        mBinding.apply {
            mNotifyItemHelper.setDeleteItem { position ->
                val notifyInfo = mNotifyAdapter.data[position]
                cleanNotify(mNotifyAdapter,notifyInfo,position)
            }

            notifyClickContainer.setOnClickListener {
                val currentState = mModel.getCurrentState()
                if (currentState) {
                    mNotifyAdapter.removeSelectAll()
                } else {
                    mNotifyAdapter.selectAll()
                }
                mModel.setClickAllState(!currentState)
            }


            mNotifyAdapter.setAdapterClickAction{
                Log.i("666", "setAdapterClickAction--------------$it")
                mModel.setClickState()
            }

//            mNotifyAdapter.setOnItemClickListener { adapter, view, position ->
//                val notifyInfo = adapter.data[position] as NotifyInfo
//                startOtherApp(notifyInfo.packageName)
//                cleanNotify(adapter,notifyInfo,position)
//            }

            notifyCleanBt.setOnClickListener {

                cleanAllUpdateView()
            }
        }
    }

    private fun startOtherApp(packName:String){
        val intent = requireActivity().packageManager.getLaunchIntentForPackage(packName)
        startActivity(intent)
    }


    private fun cleanNotify(adapter: BaseQuickAdapter<*,*>, notifyInfo: NotifyInfo, position:Int){
        adapter.data.remove(notifyInfo)
        NotifyHelper.removeNotifyList(notifyInfo)
        NotifyService.sendCleanNotifyBroadcast(requireContext(),NotifyService.SINGLE_NOTIFY,notifyInfo.key)
        adapter.notifyItemRemoved(position)
    }

    private fun cleanAllUpdateView(){
        St.stNoticeCleanAllClick()
        NotifyService.sendCleanNotifyBroadcast(requireContext(),NotifyService.ALL_NOTIFY)
        val selectList = mNotifyAdapter.getSelectList()
        if (selectList.size==mNotifyAdapter.data.size){
            mBinding.notifyCleanContainer.isVisible=true
            mBinding.notifyCleanLottie.playAnimation()
        }else{
           mNotifyAdapter.removePartSelect(selectList)
            mModel.setClickState()
        }
    }

    override fun initObserver() {
        val that = this
        mBinding.apply {
            NotifyHelper.currentNotifyList.observe(that) { data ->
                data.list?.let {
                    val notEmpty = it.isNotEmpty()
                    if (notEmpty) {
                        mNotifyAdapter.setList(it)
                        mNotifyAdapter.selectAll()
                        mModel.setClickAllState(true)
                    }
                    notifyYseContainer.isVisible= notEmpty
                    notifyNoContainer.isVisible=!notEmpty
                }
            }

            NotifyHelper.emptyEvent.observe(that) {data->
                if (data.isShow) {
                    val emptyState = data.emptyState
                    notifyYseContainer.isVisible = !emptyState
                    notifyNoContainer.isVisible = emptyState
                    if (emptyState) mModel.setClickAllState(false)
                }
            }

            NotifyHelper.currentNotify.observe(that) { data ->
                data.notify?.let {
                    if (data.isShow) {
                        if (data.isAdd) {
                            mNotifyAdapter.addData(0, it)
                            notifyContent.scrollToPosition(0)
                        }else{
                            mNotifyAdapter.remove(it)
                        }
//                        val currentState = mModel.getCurrentState()
//                        if (currentState) {
//                            mNotifyAdapter.selectAll()
//                        } else {
//                            mNotifyAdapter.removeSelectAll()
//                        }
                    }
                }
            }


            mModel.selectAllState.observe(that) {
                changeImageState(it)
                changeBtState(it)
                if (it) setSelectNotifyToHelper(mNotifyAdapter.getSelectList())
            }

            mModel.selectState.observe(that){
                val selectList = mNotifyAdapter.getSelectList()
                when {
                    selectList.size==mNotifyAdapter.data.size -> {
                       mModel.setClickAllState(true)
                    }
                    selectList.isNotEmpty() -> {
                        changeImageState(false)
                        changeBtState(true)
                        setSelectNotifyToHelper(selectList)
                    }
                    else -> {
                        mModel.setClickAllState(false)
                    }
                }

            }
        }
    }

    private fun setSelectNotifyToHelper(selectList:List<NotifyInfo>){
        NotifyHelper.getSelectNotifyList(selectList)
    }

    private fun changeImageState(state: Boolean){
        mBinding.notifySelectIcon.setImageResource(if (state) R.drawable.ic_notify_sure else R.drawable.ic_notify_cancel)
    }

    private fun changeBtState(state:Boolean){
        mBinding.notifyCleanBt.background = ContextCompat.getDrawable(
            requireContext(),
            if (state) R.drawable.shape_notify_clean_yes_bt else R.drawable.shape_notify_clean_no_bt
        )
        mBinding.notifyCleanBt.isClickable = state
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //   NotifyHelper.currentNotifyList.value= NotifySet(null,false)
        NotifyHelper.currentNotify.value = NotifySingle(null, isAdd = false, isShow = false)
        NotifyHelper.emptyEvent.value = NotifyEmptyEvent(false, isShow = false)
    }

}