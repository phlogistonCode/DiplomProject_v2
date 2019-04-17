package com.phlogiston

import android.app.Activity
import android.os.Bundle

class SetPreferenceActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)

        fragmentManager.beginTransaction().replace(android.R.id.content,
                PrefsFragment()).commit()
    }
}