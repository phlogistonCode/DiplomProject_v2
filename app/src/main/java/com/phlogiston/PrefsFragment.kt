package com.phlogiston

import android.os.Bundle
import android.preference.PreferenceFragment

class PrefsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref)
    }
}