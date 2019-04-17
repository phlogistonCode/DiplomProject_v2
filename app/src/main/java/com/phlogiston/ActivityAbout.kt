package com.phlogiston

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class ActivityAbout : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val tv = findViewById<TextView>(R.id.textView2)
        tv.setText(R.string.textAbout)
    }
}
