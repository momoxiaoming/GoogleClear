package com.mckj.module.cleanup.ui.appManager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.data.model.appManagerViewModel
import com.mckj.module.cleanup.databinding.CleanupItemAppManagerPageInstallTimeBinding
import com.mckj.module.cleanup.entity.AppInfoHolder
import com.mckj.module.cleanup.entity.ApplicationLocal
import com.mckj.module.cleanup.ui.adapter.AppRecyclerAdapter
import com.mckj.module.cleanup.ui.appManager.receiver.InstallReceiver
import com.mckj.module.cleanup.ui.appManager.widget.AppManagerLoadingDialog
import com.mckj.module.gen.St
import com.mckj.module.utils.EventTrack
import java.util.*

class AppManagerFragmentInstallTime : Fragment() {

    private var appList: MutableSet<ApplicationLocal> = mutableSetOf()

    private lateinit var appRecyclerAdapter: AppRecyclerAdapter

    private lateinit var searchEt: EditText


    private var progressDialog: AppManagerLoadingDialog? = null


    private lateinit var checkSize: TextView

    private lateinit var mViewModel: appManagerViewModel

    private lateinit var mInstallReceiver: AppManagerFragmentInstallTime.installReceiver

    private lateinit var dataBinding:CleanupItemAppManagerPageInstallTimeBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding=DataBindingUtil.inflate(inflater,R.layout.cleanup_item_app_manager_page_install_time,container,false)
        initView()
        return dataBinding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.appAllList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            appList = it
            appRecyclerAdapter.notifyDataSetChanged()
            if (it.isEmpty()){
                dataBinding.emptyLayout2.show()
            }else{
                dataBinding.emptyLayout2.gone()
            }
        })

        mViewModel.checkList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (mViewModel.checkList.value?.get(5) == true) {
                sortList("down")
            }

            if (mViewModel.checkList.value?.get(6) == true) {
                sortList("up")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchEt = activity?.findViewById(R.id.search)!!
        checkSize = activity?.findViewById(R.id.unInstall_btn_size)!!
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onResume() {
        super.onResume()
        searchEtListener()
    }

    //初始化view
    private fun initView() {
        mViewModel = ViewModelProvider(requireActivity()).get(appManagerViewModel::class.java)
        appRecyclerAdapter = AppRecyclerAdapter(appList, requireActivity(), true)
        mViewModel.sharedAdapter[3] = appRecyclerAdapter
        dataBinding.rvAppList.layoutManager = LinearLayoutManager(requireContext())
        dataBinding.rvAppList.adapter = appRecyclerAdapter

        appRecyclerAdapter.setOnItemClickListener(object : AppRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
            }
        })
    }

    private fun searchEtListener() {
        mViewModel.mEditTextLivaData.observe(requireActivity(), {
            val installList = mViewModel.appAllList.value
            val mInstallList: MutableSet<ApplicationLocal> = mutableSetOf()
            val findList: MutableSet<ApplicationLocal> = mutableSetOf()

            if (installList != null) {
                mInstallList.addAll(installList)
            }

            if (installList.isNullOrEmpty()) {
                showSearchList(findList)
                return@observe
            }

            if (it.isNullOrEmpty()) {
                //默认排序
                if(mViewModel.checkList.value?.get(5) == true) {
                    sortList(mInstallList, "down")
                }else{
                    sortList(mInstallList, "up")
                }

                showSearchList(mInstallList)
                return@observe
            }

            for (i in installList) {
                if (i.name.toLowerCase(Locale.CHINA).contains
                        (it.toLowerCase(Locale.CHINA))
                ) {
                    findList.add(i)
                }
            }
            showSearchList(findList)
        }
        )
    }

    private fun showSearchList(searchAppList: MutableSet<ApplicationLocal>) {
        EventTrack.stManagementSearchClick()
        appRecyclerAdapter.updateAdapterList(searchAppList,dataBinding.emptyLayout2)
        //搜索结果应用点击监听
        appRecyclerAdapter.setOnItemClickListener(object : AppRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
            }
        })
    }

    private fun sortList(type: String) {
        if(appList.isEmpty()){
            return
        }
        when (type) {
            "down" -> {
                var list: MutableList<ApplicationLocal> = mutableListOf()
                list = appList.sortedByDescending {
                    it.firstInstallTime
                } as MutableList<ApplicationLocal>

                appList.clear()
                appList.addAll(list)

                appRecyclerAdapter.updateAdapterList(appList,dataBinding.emptyLayout2)
            }

            "up" -> {
                var list: MutableList<ApplicationLocal> = mutableListOf()
                list = appList.sortedBy {
                    it.firstInstallTime
                } as MutableList<ApplicationLocal>

                appList.clear()
                appList.addAll(list)

                appRecyclerAdapter.updateAdapterList(appList,dataBinding.emptyLayout2)
            }
        }
    }

    private fun sortList(list: MutableSet<ApplicationLocal>, type: String) {
        if(list.isEmpty()){
            return
        }
        when (type) {
            "down" -> {
                var mlist: MutableList<ApplicationLocal> = mutableListOf()
                mlist = list.sortedByDescending {
                    it.firstInstallTime
                } as MutableList<ApplicationLocal>

                list.clear()
                list.addAll(mlist)
            }

            "up" -> {
                var mlist: MutableList<ApplicationLocal> = mutableListOf()
                mlist = list.sortedBy {
                    it.firstInstallTime
                } as MutableList<ApplicationLocal>

                list.clear()
                list.addAll(mlist)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mInstallReceiver = installReceiver()
        val filter = IntentFilter()
        filter.addAction("android.intent.action.PACKAGE_ADDED")
        filter.addAction("android.intent.action.PACKAGE_REMOVED")
        filter.addDataScheme("package")

        requireContext().registerReceiver(mInstallReceiver, filter)
    }

    override fun onDestroy() {
        requireContext().unregisterReceiver(mInstallReceiver)
        super.onDestroy()
    }

    //安装卸载广播接收器
    inner class installReceiver : InstallReceiver() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onReceive(context: Context?, intent: Intent?) {
            super.onReceive(context, intent)

            //卸载
            if (intent?.action?.equals("android.intent.action.PACKAGE_REMOVED") == true) {
                val packName = intent.dataString

                appList.removeIf { "package:"+it.packageName == packName }
                appRecyclerAdapter.updateAdapterList(appList,dataBinding.emptyLayout2)
                mViewModel.sharedAdapter[3]?.notifyDataSetChanged()
            }
        }
    }
}