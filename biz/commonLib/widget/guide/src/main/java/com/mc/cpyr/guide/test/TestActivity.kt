package com.mc.cpyr.guide.test

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mc.cpyr.guide.Guide
import com.mc.cpyr.guide.R
import com.mc.cpyr.guide.listener.MaskViewClickListener
import com.mc.cpyr.guide.listener.VisibilityChangedListener

/**
 * TestActviity
 *
 * @author mmxm
 * @date 2021/3/8 20:01
 */
class TestActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        findViewById<Button>(R.id.btn).setOnClickListener{
            showGuide()
        }
    }

    fun showGuide(){
        Guide.Builder()
            .setTargetView(findViewById(R.id.target_view))
            .setAlpha(200)
            .setHighTargetCorner(20)
            .setOutsideTouchable(true) //
            .setAutoDismiss(false)
            .setHighTargetPadding(10)
            .setMaskViewClickListener(object : MaskViewClickListener {
                override fun onClick(view : View, isTargetView: Boolean):Boolean {

                    return true
                }

            })
            .setOnVisibilityChangedListener(object : VisibilityChangedListener {
                override fun onShown() {

                }

                override fun onDismiss() {
                }

            }).build(this).show(this)
    }
}