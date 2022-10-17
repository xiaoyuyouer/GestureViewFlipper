package com.magic.gestureviewflipper

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.magic.gestureviewflipper.view.GestureViewFlipper

class MainActivity : AppCompatActivity() {
    private lateinit var vf: GestureViewFlipper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vf = findViewById(R.id.vf_notice)

        vf.playFlipping()
        vf.setOnFocusChangedListener(object : GestureViewFlipper.FlipperFocusChangedListener {
            override fun onFlipperChanged(index: Int) {
                Log.e("TAG", "onFlipperChanged: $index")
            }
        })

        initData()
    }

    private fun initData() {
        val list: MutableList<String> = ArrayList()
        list.add("这是公告111")
        list.add("这是公告222")
        list.add("这是公告333")

        //先移除之前的所有view
        vf.removeAllViews()
        for (i in list.indices) {
            val view: View = LayoutInflater.from(this).inflate(R.layout.layout_banner_notice, null)
            val tvValue = view.findViewById<View>(R.id.tv_value) as TextView
            tvValue.text = list[i]
            view.setOnClickListener {
                Toast.makeText(this@MainActivity, list[i], Toast.LENGTH_SHORT).show()
            }
            vf.addView(view) //add方式进行添加
        }

    }
}