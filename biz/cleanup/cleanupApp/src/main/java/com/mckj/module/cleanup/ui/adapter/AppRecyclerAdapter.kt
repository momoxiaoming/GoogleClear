package com.mckj.module.cleanup.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mckj.baselib.helper.getApplicationContext
import com.mckj.baselib.util.ResourceUtil
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.data.model.appManagerViewModel
import com.mckj.module.cleanup.entity.AppInfoHolder
import com.mckj.module.cleanup.entity.ApplicationLocal
import com.mckj.module.cleanup.ui.appManager.AppManagerActivity
import com.mckj.module.cleanup.ui.appManager.AppManagerFragmentAppSize
import com.mckj.module.cleanup.ui.appManager.AppManagerFragmentInstallTime
import com.mckj.module.cleanup.ui.appManager.AppManagerFragmentUseFrequency
import com.mckj.module.cleanup.util.DateTools
import com.mckj.module.cleanup.util.Log
import java.math.BigDecimal


open class AppRecyclerAdapter(
    private var appList: MutableSet<ApplicationLocal>, context: Activity,
    isInstallTime: Boolean
) : RecyclerView.Adapter<AppRecyclerAdapter.AppViewHolder>() {

    // 上下文
    private val mContext: Activity = context

    private var packageManager: PackageManager? = null

    private var mIsInstallTime: Boolean = isInstallTime

    private var totalSize: BigDecimal = BigDecimal(0L)

//    private var checkedSize: BigDecimal = BigDecimal(0L)

    //viewModel
    private var mViewModel: appManagerViewModel =
        ViewModelProvider(mContext as AppManagerActivity).get(appManagerViewModel::class.java)

    init {

        for (i in appList) {
            //设置默认的checkBox选中
            mViewModel.checkedMap[i.packageName] = false
            totalSize = totalSize.add(BigDecimal(i.apkSize))
        }

        packageManager = mContext.packageManager
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return if (appList.isNullOrEmpty()) 0 else appList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onBindViewHolder(viewHolder: AppViewHolder, position: Int) {

        viewHolder.iv_appIcon.setImageDrawable(appList.elementAt(position).icon)
        //app名字
        viewHolder.tv_appName.text = appList.elementAt(position).name
        //上次使用时间Mills
        val lastUseTime = AppInfoHolder.getNonSystemApplicationTime(
            mContext,
            appList.elementAt(position).packageName
        )

        if (mIsInstallTime) {
            viewHolder.tv_appNoUseTime.text = appList.elementAt(position).firstInstallTimeFormat
        } else {
            viewHolder.tv_appNoUseTime.text =
                if (lastUseTime == null) {
                    "未使用过"
                } else {
                    AppInfoHolder.getAppNoUseTimeFormat(lastUseTime)
                }
        }

        viewHolder.tv_appSize.text = appList.elementAt(position).apkSizeFormat

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.i("appNoUseTime", "appName:${appList.elementAt(position).name}, " +
                    "packageName: ${appList.elementAt(position).packageName}, " +
                    "noUseTime: $lastUseTime, " +
                    "noUseDate: ${lastUseTime?.let { DateTools.timeToDate(lastUseTime) }}"
            )
        }

        viewHolder.checkBoxItem.setOnCheckedChangeListener(null)
        viewHolder.checkBoxItem.isChecked = mViewModel.checkedMap[appList.elementAt(position).packageName] == true

        viewHolder.checkBoxItem.setOnCheckedChangeListener { _, isChecked -> //用集合保存当前状态
            mViewModel.checkedMap[appList.elementAt(position).packageName] = isChecked
            refreshCheckedSize()
        }

        viewHolder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }
    }

    open fun getTotalAppSize(): String? {
        return Formatter.formatShortFileSize(getApplicationContext(), totalSize.toLong())
    }

    //刷新选中大小
    open fun refreshCheckedSize(){
        mViewModel.checkedSizeByte.value = BigDecimal(0L)
        for(i in appList){
            if(mViewModel.checkedMap[i.packageName] == true){
                mViewModel.checkedSizeByte.value =
                    mViewModel.checkedSizeByte.value?.add(BigDecimal(i.apkSize))
            }else{

            }
        }

        mViewModel.checkedSize.value = mViewModel.checkedSizeByte.value?.let {
            getCheckedSizeFormat(
                it
            )
        }
    }

    open fun getCheckedSizeFormat(mCheckSize: BigDecimal): String {
        return "(" + Formatter.formatShortFileSize(
            getApplicationContext(),
            mCheckSize.toLong()
        ) + ")"
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(
                R.layout.cleanup_item_app_manager,
                viewGroup, false
            )
        )
    }

    class AppViewHolder constructor(val view: View) : RecyclerView.ViewHolder(view) {

        val iv_appIcon = view.findViewById<ImageView>(R.id.iv_appIcon)
        val tv_appName = view.findViewById<TextView>(R.id.tv_appName)
        val tv_appNoUseTime = view.findViewById<TextView>(R.id.tv_appNoUseTime)
        val tv_appSize = view.findViewById<TextView>(R.id.tv_appSize)
        val checkBoxItem = view.findViewById<CheckBox>(R.id.app_select)

    }

    //卸载选中数据
    open fun unInstallChecked() {
        var nums = 0
        for (i in appList) {
            if (mViewModel.checkedMap[i.packageName] == true) {
                uninstallApp(i.packageName)
                nums++
            }
        }
        notifyDataSetChanged()

        if (nums > 0) {
            mViewModel.isUninstallClick.value = true
        }

        if (nums == 0) {
            Toast.makeText(mContext, ResourceUtil.getString(R.string.cleanup_no_select_app), Toast.LENGTH_SHORT).show()
        }
    }

    //卸载应用
    private fun uninstallApp(packageName: String) {
        val unInstallIntent = Intent()
        unInstallIntent.action = Intent.ACTION_DELETE
        unInstallIntent.data = Uri.parse("package:$packageName")
        startActivity(mContext, unInstallIntent, null)
    }

    fun updateAdapterList(list: MutableSet<ApplicationLocal>) {
        val iterator = list.iterator()
        while (iterator.hasNext()){
            val next = iterator.next()
            if (next.apkSize == 0L){
                iterator.remove()
            }
        }


        //更新总大小
        totalSize = BigDecimal(0L)
        for (i in appList) {
            totalSize = totalSize.add(BigDecimal(i.apkSize))
        }

        this.appList = list
        notifyDataSetChanged()
    }
}