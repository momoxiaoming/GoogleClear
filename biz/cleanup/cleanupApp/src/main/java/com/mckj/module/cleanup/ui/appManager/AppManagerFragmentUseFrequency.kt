package com.mckj.module.cleanup.ui.appManager

import android.annotation.SuppressLint
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.data.model.appManagerViewModel
import com.mckj.module.cleanup.entity.AppInfoHolder
import com.mckj.module.cleanup.entity.ApplicationLocal
import com.mckj.module.cleanup.ui.adapter.AppRecyclerAdapter

import com.mckj.module.utils.EventTrack

import java.util.*
import java.util.zip.Inflater

class AppManagerFragmentUseFrequency : Fragment() {

    private var appList: MutableSet<ApplicationLocal> = mutableSetOf()

    private lateinit var appRecyclerAdapter: AppRecyclerAdapter

    private lateinit var searchEt: EditText

    private lateinit var checkSize: TextView

    private lateinit var uninstallBt: LinearLayout

    private var rv_appList: RecyclerView? = null

    private lateinit var mViewModel: appManagerViewModel

    private var isAfterUnInstall: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.cleanup_item_app_manager_page_use_frequency,
            container,
            false
        )
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.appAllList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            appList = mViewModel.appAllList.value!!
            initView()
        })

        mViewModel.checkList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (mViewModel.checkList.value?.get(1) == true) {
                sortList("up")
            }

            if (mViewModel.checkList.value?.get(2) == true) {
                sortList("down")
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchEt = activity?.findViewById(R.id.search)!!
        checkSize = activity?.findViewById(R.id.unInstall_btn_size)!!
        uninstallBt = activity?.findViewById(R.id.unInstall_btn)!!
        mViewModel = ViewModelProvider(requireActivity()).get(appManagerViewModel::class.java)
    }

    private fun searchEtListener() {
        mViewModel.mEditTextLivaData.observe(requireActivity(), {
            val installList = mViewModel.appAllList.value
            val findList: MutableSet<ApplicationLocal> = mutableSetOf()
            if (installList.isNullOrEmpty()) {
                showSearchList(findList)
                return@observe
            }
            if (it.isNullOrEmpty()) {
                showSearchList(installList)
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
        })
        //搜索功能
//        searchEt.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                val value = searchEt.text.toString()
//                if (value.isEmpty()) {
//                    initView()
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                val value = searchEt.text.toString()
//                value.let {
//                    if (s != null) {
//                        if (it.isNotEmpty()) {
//                            val installList =
//                                AppInfoHolder.getAllNonSystemApplication(requireContext())
//                            val findList: MutableSet<ApplicationLocal> = mutableSetOf()
//                            for (i in appList) {
//                                if (i.name.toLowerCase(Locale.CHINA).contains
//                                        (value.toLowerCase(Locale.CHINA))
//                                ) {
//                                    findList.add(i)
//                                }
//                            }
//
//                            if (findList.isEmpty()) {
//                                //空了展示搜索结果为空白
//                                showSearchList(findList)
//                            } else {
//                                showSearchList(findList)
//                            }
//                        }
//                    }
//                }
//            }
//        })
    }

    override fun onResume() {
        super.onResume()
        searchEtListener()
    }

    //填充view
    private fun initView() {
        rv_appList = view?.findViewById(R.id.rv_appList)

        appRecyclerAdapter = AppRecyclerAdapter(appList, requireActivity(), false)
        mViewModel.sharedAdapter[1] = appRecyclerAdapter

        rv_appList?.layoutManager = LinearLayoutManager(requireContext())
        rv_appList?.adapter = appRecyclerAdapter

        appRecyclerAdapter.notifyDataSetChanged()

        appRecyclerAdapter.setOnItemClickListener(object : AppRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
            }
        })
    }

    private fun showSearchList(searchAppList: MutableSet<ApplicationLocal>) {
        EventTrack.stManagementSearchClick()
        appRecyclerAdapter.updateAdapterList(searchAppList,null)
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
                    it.lastUseTime
                } as MutableList<ApplicationLocal>

                appList.clear()
                appList.addAll(list)

                appRecyclerAdapter.notifyDataSetChanged()
            }

            "up" -> {
                var list: MutableList<ApplicationLocal> = mutableListOf()
                list = appList.sortedBy {
                    it.lastUseTime
                } as MutableList<ApplicationLocal>

                appList.clear()
                appList.addAll(list)

                appRecyclerAdapter.notifyDataSetChanged()
            }
        }
    }

}