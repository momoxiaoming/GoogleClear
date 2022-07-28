package com.mckj.module.cleanup.ui.appManager

import android.annotation.SuppressLint
import android.app.Activity
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
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.data.model.appManagerViewModel
import com.mckj.module.cleanup.databinding.CleanupItemAppManagerPageAppSizeBinding
import com.mckj.module.cleanup.databinding.CleanupItemAppManagerPageInstallTimeBinding
import com.mckj.module.cleanup.entity.AppInfoHolder
import com.mckj.module.cleanup.entity.ApplicationLocal
import com.mckj.module.cleanup.ui.adapter.AppRecyclerAdapter
import com.mckj.module.cleanup.ui.appManager.receiver.InstallReceiver
import com.mckj.module.cleanup.ui.appManager.widget.AppManagerLoadingDialog
import com.mckj.module.cleanup.ui.dialog.OpenFailDialog
import com.mckj.module.gen.St
import com.mckj.module.utils.EventTrack
import com.org.openlib.utils.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.internal.notifyAll
import java.math.BigDecimal
import java.util.*

/**
 * Describe:
 *
 * Created By yangb on 2021/8/11
 */
class AppManagerFragmentAppSize : Fragment() {
    private lateinit var mViewModel: appManagerViewModel

    private var appList: MutableSet<ApplicationLocal> = mutableSetOf()
    private lateinit var appRecyclerAdapter: AppRecyclerAdapter
    private lateinit var searchEt: EditText
    private lateinit var checkSize: TextView
    private var isLoadList: Boolean = false
    private var progressDialog: AppManagerLoadingDialog? = null
    var isPermission: Boolean = false
    private lateinit var softNums: TextView
    private lateinit var occupySize: TextView
    private lateinit var checkedSize: TextView
    private lateinit var mInstallReceiver: installReceiver
    private lateinit var dataBinding: CleanupItemAppManagerPageAppSizeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding= DataBindingUtil.inflate(inflater,R.layout.cleanup_item_app_manager_page_app_size,container,false)
        initView()
        return dataBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel.appAllList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            appList = it
            //初始化默认排序
            if (appList.isNotEmpty()) {
                mViewModel.checkList.value?.clear()
                if (isPermission) {
                    mViewModel.checkClick(1, true)
                } else {
                    mViewModel.checkClick(3, true)
                }
            }

            appRecyclerAdapter.notifyDataSetChanged()
        })

        mViewModel.checkList.observe(viewLifecycleOwner, Observer {
            if (mViewModel.checkList.value?.get(3) == true) {
                sortList("down")
            }

            if (mViewModel.checkList.value?.get(4) == true) {
                sortList("up")
            }
        })

        mViewModel.isUninstallClick.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
        })

        loadAppList()
        isLoadList = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchEt = activity?.findViewById(R.id.search)!!
        checkSize = activity?.findViewById(R.id.unInstall_btn_size)!!
        isPermission = false
        softNums = activity?.findViewById(R.id.soft_number)!!
        occupySize = activity?.findViewById(R.id.occupy)!!
        checkSize = activity?.findViewById(R.id.unInstall_btn_size)!!
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    @SuppressLint("CheckResult")
    private fun loadAppList() {
        Log.i("appManager", "load app list")
        Observable.create(ObservableOnSubscribe<MutableList<ApplicationLocal>> {
            AppInfoHolder.init(requireContext())
            val toMutableList = AppInfoHolder.getAllNonSystemApplication(requireContext())
            it.onNext(toMutableList)
            it.onComplete()
        }).subscribeOn(Schedulers.io()).doOnSubscribe {
            startLoading()
        }.doFinally {
//            activity?.runOnUiThread {
//
//            }
        }.observeOn(AndroidSchedulers.mainThread()).subscribe({
            appList.clear()
            appList.addAll(it)
        }, {
        }, {
            cancelLoading()
            mViewModel.setAppList(appList)
            if(appList.size==0){
                dataBinding.emptyLayout.show()
            }else{
                dataBinding.emptyLayout.gone()
            }
        })
    }

    private fun startLoading(cancelable: Boolean = false) {
        if (progressDialog == null) {
            progressDialog = AppManagerLoadingDialog(requireContext())
        }
        progressDialog?.start(cancelable, cancelable)
    }

    private fun cancelLoading() {
        progressDialog?.dismiss()
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

    //安装卸载广播接收器
    inner class installReceiver : InstallReceiver() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onReceive(context: Context?, intent: Intent?) {
            super.onReceive(context, intent)
            //安装
            if (intent?.action?.equals("android.intent.action.PACKAGE_ADDED") == true) {
                val packName = intent.dataString
                loadAppList()
            }

            //卸载
            if (intent?.action?.equals("android.intent.action.PACKAGE_REMOVED") == true) {
                val packName = intent.dataString

                for (i in appList) {
                    if ("package:" + i.packageName == packName) {
                        mViewModel.checkedMap[i.packageName] = false
                        appRecyclerAdapter.refreshCheckedSize()
                        break
                    }
                }

                appList.removeIf { "package:"+it.packageName == packName }
                appRecyclerAdapter.updateAdapterList(appList,dataBinding.emptyLayout)
                mViewModel.sharedAdapter[2]?.notifyDataSetChanged()

                softNums.text = appList.size.toString()
                occupySize.text = appRecyclerAdapter.getTotalAppSize()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onResume() {
        super.onResume()
        searchEtListener()

        if (!isLoadList) {
            loadAppList()
        }

        //卸载之后清空搜索文字
        if (mViewModel.isUninstallClick.value == true) {
            searchEt.setText("")
        }
    }

    override fun onDestroy() {
        requireContext().unregisterReceiver(mInstallReceiver)
        super.onDestroy()
    }

    private fun initView() {
        mViewModel = ViewModelProvider(requireActivity()).get(appManagerViewModel::class.java)
        appRecyclerAdapter = AppRecyclerAdapter(appList, requireActivity(), true)
        softNums.text = appList.size.toString()
        occupySize.text = appRecyclerAdapter.getTotalAppSize()

        mViewModel.sharedAdapter[2] = appRecyclerAdapter

        dataBinding.rvAppList.layoutManager = LinearLayoutManager(requireContext())
        dataBinding.rvAppList.adapter = appRecyclerAdapter

        appRecyclerAdapter.setOnItemClickListener(object : AppRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
            }
        })

    }

    private fun searchEtListener() {
        mViewModel.mEditTextLivaData.observe(requireActivity(), Observer {

            val installList = mViewModel.appAllList.value
            val mInstallList: MutableSet<ApplicationLocal> = mutableSetOf()
            val findList: MutableSet<ApplicationLocal> = mutableSetOf()

            if (installList != null) {
                mInstallList.addAll(installList)
            }

            if (installList.isNullOrEmpty()) {
                showSearchList(findList)
                return@Observer
            }

            if (it.isNullOrEmpty()) {
                //默认排序
                if (mViewModel.checkList.value?.get(3) == true) {
                    sortList(mInstallList, "down")
                } else {
                    sortList(mInstallList, "up")
                }

                showSearchList(mInstallList)
                return@Observer
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
    }

    private fun showSearchList(searchAppList: MutableSet<ApplicationLocal>) {
        EventTrack.stManagementSearchClick()
        appRecyclerAdapter.updateAdapterList(searchAppList,dataBinding.emptyLayout)
        //搜索结果应用点击监听
        appRecyclerAdapter.setOnItemClickListener(object : AppRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
            }
        })
    }

    private fun sortList(type: String) {
        if (appList.isEmpty()) {
            return
        }
        when (type) {
            "down" -> {
                var list: MutableList<ApplicationLocal> = mutableListOf()
                list = appList.sortedByDescending {
                    it.apkSize
                } as MutableList<ApplicationLocal>

                appList.clear()
                appList.addAll(list)

                appRecyclerAdapter.updateAdapterList(appList,dataBinding.emptyLayout)
            }

            "up" -> {
                var list: MutableList<ApplicationLocal> = mutableListOf()
                list = appList.sortedBy {
                    it.apkSize
                } as MutableList<ApplicationLocal>

                appList.clear()
                appList.addAll(list)

                appRecyclerAdapter.updateAdapterList(appList,dataBinding.emptyLayout)
            }
        }
    }

    private fun sortList(list: MutableSet<ApplicationLocal>, type: String) {
        if (list.isEmpty()) {
            return
        }
        when (type) {
            "down" -> {
                var mlist: MutableList<ApplicationLocal> = mutableListOf()
                mlist = list.sortedByDescending {
                    it.apkSize
                } as MutableList<ApplicationLocal>

                list.clear()
                list.addAll(mlist)
            }

            "up" -> {
                var mlist: MutableList<ApplicationLocal> = mutableListOf()
                mlist = list.sortedBy {
                    it.apkSize
                } as MutableList<ApplicationLocal>

                list.clear()
                list.addAll(mlist)
            }
        }
    }


}
