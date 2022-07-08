package com.mckj.module.cleanup.ui.appManager.widget

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.mckj.module.cleanup.R
import java.util.ArrayList

class SpinnerActivity : Activity() {
    private val list: MutableList<String> = ArrayList()
    private var textview: TextView? = null
    private var spinnertext: Spinner? = null
    private var adapter: ArrayAdapter<String>? = null
    public override fun onCreate(savedlnstanceState: Bundle?) {
        super.onCreate(savedlnstanceState)
        setContentView(R.layout.cleanup_app_manager_spiner)
        //第一步：定义下拉列表内容
        list.add("A型")
        list.add("B型")
        list.add("O型")
        list.add("AB型")
        list.add("其他")
        spinnertext = findViewById<Spinner>(R.id.spinner1)
        //第二步：为下拉列表定义一个适配器
        adapter = ArrayAdapter(this, R.layout.simple_spinner_item, list)
        //第三步：设置下拉列表下拉时的菜单样式
        adapter!!.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        //第四步：将适配器添加到下拉列表上
        spinnertext!!.adapter = adapter
        //第五步：添加监听器，为下拉列表设置事件的响应
        spinnertext!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        spinnertext!!.setOnTouchListener { v, event -> // TODO Auto-generated method stub
            // 将mySpinner隐藏
            v.visibility = View.INVISIBLE
            false
        }

        //焦点改变事件处理
        spinnertext!!.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus -> // TODO Auto-generated method stub
                v.visibility = View.VISIBLE
            }
    }
}