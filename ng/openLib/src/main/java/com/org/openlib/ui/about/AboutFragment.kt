package com.org.openlib.ui.about

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.adapter.BindingRecycleHolder
import com.dn.vi.app.base.adapter.RecycleDiffListAdapter
import com.dn.vi.app.base.app.DatabindingFragment
import com.dn.vi.app.cm.log.VLog
import com.dn.vi.app.cm.log.debug
import com.dn.vi.app.repo.kv.KvLite
import com.org.openlib.R
import com.org.openlib.databinding.OpenFragmentAboutBinding
import com.org.openlib.databinding.OpenItemAboutOpt2Binding

import com.org.openlib.ui.about.AboutFragment.AboutOptItem.Companion.TYPE_BUTTON
import com.org.openlib.path.ARouterPath
import com.org.openlib.ui.web.WebHelper
import com.org.openlib.utils.WebUtil
import com.org.proxy.AppProxy
import com.org.proxy.EvAgent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.ByteString.Companion.toByteString

/**
 * 关于页
 * Created by Vito on 2020/8/6.
 **/
@Route(path = ARouterPath.FRAGMENT_ABOUT)
open class AboutFragment : DatabindingFragment<OpenFragmentAboutBinding>() {

    companion object {
        const val PRIVATE_BUTTON_STATUS = "PRIVATE_BUTTON_STATUS"
    }

    private var optList = mutableListOf<AboutOptItem>()
    private var buttonStatus = true

    private lateinit var mBinding: OpenFragmentAboutBinding

    override fun onCreateDatabinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): OpenFragmentAboutBinding {
        mBinding = OpenFragmentAboutBinding.inflate(inflater, container, false)
        return mBinding
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        EvAgent.sendEvent("concern_show")
        //初始化标题
        activity?.title = getString(R.string.string_open_setting)
        initView()
    }

    private fun initView() {
        optList.add(AboutOptItem(
            0,
            getString(R.string.string_open_version),
            AboutOptItem.TYPE_TEXT,
            {}
        ).also { it.subText = "v${AppProxy.appVersion}" })

        optList.add(
            AboutOptItem(
                1,
                getString(R.string.string_open_privacy),
                AboutOptItem.TYPE_ARROW,
                Runnable { WebUtil.openProtocolByWeb(requireActivity()) })
        )
        optList.add(
            AboutOptItem(
                1,
                getString(R.string.string_open_lx),
                AboutOptItem.TYPE_TEXT,
                Runnable { }).also {
                it.subText = "missluoluo7@gmail.com"
            })

        val adapter = OptAdapter(requireContext())
        binding.list.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.list.adapter = adapter

        adapter.submitList(optList)
    }

    fun getCustomerService(): String {
        return ""

    }

    private fun openFeed() {
        // https://pro.77pin.net/feedback/questionFeedback.html?value=base64(appid=xxxpid=xxxlsn=xxx
        scope.launch {

            val finalUrl: String = with(Dispatchers.Default) {

                val param = listOf<Pair<String, String>>(
                    "appid" to AppProxy.appId,
                    "pid" to AppProxy.projectId,
                    "lsn" to AppProxy.lsn
                )

                var paramChain =
                    param.map { "${it.first}=${it.second}" }.joinToString(separator = "&") { it }

                //  appid=37936&pid=37936000&lsn=45635683
                debug {
                    "raw params: ${paramChain}"
                }

                paramChain = paramChain.toByteArray().toByteString().base64Url()

                // YXBwaWQ9Mzc5MzYmcGlkPTM3OTM2MDAwJmxzbj00NTYzNTY4Mw==
                debug {
                    "base64 params: ${paramChain}"
                }

                "https://pro.77pin.net/feedback/questionFeedback.html?imgOff=1&value=${paramChain}"
            }

            WebHelper.openWeb(
                context, WebHelper.Request(
                    webUrl = finalUrl,
                    title = "反馈",
                    contentKey = "feedback",
                )
            )

        }
    }

    fun checkButtonStatus(binding: OpenItemAboutOpt2Binding) {
        KvLite.async.getBool(PRIVATE_BUTTON_STATUS, true).observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                VLog.i("checkUnlock get value:${it}")
                buttonStatus = it
                if (it) {
                    binding.button.setBackgroundResource(R.drawable.shape_switch_btn)
                } else {
                    binding.button.setBackgroundResource(R.drawable.shape_switch_btn_un)
                }
            }
    }

    fun saveButtonStatus(status: Boolean) {
        KvLite.async.putBool(PRIVATE_BUTTON_STATUS, status)
            .observeOn(AndroidSchedulers.mainThread()).subscribeBy {
            VLog.i("ButtonStatus put value :${status}")
            //执行检查unlock的操作
            buttonStatus = status
        }
    }

    private inner class OptAdapter(context: Context) :
        RecycleDiffListAdapter<AboutOptItem, BindingRecycleHolder<AboutOptItem, OpenItemAboutOpt2Binding>>(
            context
        ) {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BindingRecycleHolder<AboutOptItem, OpenItemAboutOpt2Binding> {
            return OptHolder(OpenItemAboutOpt2Binding.inflate(inflater, parent, false))
        }

        override fun onBindViewHolder(
            holder: BindingRecycleHolder<AboutOptItem, OpenItemAboutOpt2Binding>,
            position: Int
        ) {
            getItem(position)?.also { item ->
                holder.onBindViewHolder(item, position)
            }
        }

        override fun getItemViewType(position: Int): Int {
            return currentList[position].type
        }
    }

    private inner class OptHolder(binding: OpenItemAboutOpt2Binding) :
        BindingRecycleHolder<AboutOptItem, OpenItemAboutOpt2Binding>(binding),
        View.OnClickListener {

        override fun onBindViewHolder(item: AboutOptItem, position: Int) {

            binding.root.setOnClickListener(this)

            binding.opt = item
            binding.executePendingBindings()
            checkButtonStatus(binding)
            if (item.type == TYPE_BUTTON) {
                changeButtonStatus(binding)
            }
        }

        override fun onClick(v: View?) {
            binding.opt?.also { opt ->
                opt.performRun?.run()
            }
        }

    }

    private fun changeButtonStatus(binding: OpenItemAboutOpt2Binding) {
        binding.holder.setOnClickListener {
            saveButtonStatus(!buttonStatus)
            if (buttonStatus) {
                binding.button.setBackgroundResource(R.drawable.shape_switch_btn_un)
            } else {
                binding.button.setBackgroundResource(R.drawable.shape_switch_btn)
            }
        }
    }

    data class AboutOptItem(val iconRes: Int, val name: CharSequence, val type: Int) {

        companion object {
            const val TYPE_ARROW = 1 //右边显示小箭头
            const val TYPE_TEXT = 2 //右边显示文字
            const val TYPE_BUTTON = 3 //右边显示按钮
        }

        var performRun: Runnable? = null

        var subText: String = ""

        constructor(iconRes: Int, name: CharSequence, type: Int, run: Runnable) : this(
            iconRes,
            name,
            type
        ) {
            this.performRun = run
        }

    }

    private fun finish() {
        activity?.finish()
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}