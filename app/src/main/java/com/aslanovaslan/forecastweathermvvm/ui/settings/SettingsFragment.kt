package com.aslanovaslan.forecastweathermvvm.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import com.aslanovaslan.forecastweathermvvm.R

class SettingsFragment :PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pereferences)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title="Settings"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle=null
    }
}