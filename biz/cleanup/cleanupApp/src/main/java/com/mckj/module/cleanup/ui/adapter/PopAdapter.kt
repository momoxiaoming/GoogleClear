package com.mckj.module.cleanup.ui.adapter

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.util.Log

class PopAdapter(context: Context, list: List<String>, type: String) : BaseAdapter() {
    private val list: List<String> = list
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val mType: String = type
    private var isOneCheck: Boolean = false
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView: View? = convertView
        val holder: ViewHolder
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cleanup_app_manager_popup_window_item, null)
            holder = ViewHolder()
            holder.popItemCheck = convertView.findViewById(R.id.popup_item_select)
            holder.popItemText = convertView.findViewById(R.id.popup_item_text) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.popItemText?.text = list[position]
        holder.popItemCheck?.setOnCheckedChangeListener { _, isChecked ->

            if(isChecked) {

                when (position) {
                    0 -> {
                        when (mType) {
                            "useFrequency" -> Log.i("popUp", "===很久")

                        }
                    }

                    1 -> {
                        when (mType) {
                            "useFrequency" -> Log.i("popUp", "===最近")
                        }
                    }

                }
            }
        }

        return convertView
    }

    internal inner class ViewHolder {
        var popItemText: TextView? = null
        var popItemCheck: RadioButton? = null
    }

}