package com.mny.wan.pkg.presentation.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.mny.wan.pkg.R

class ThemeSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.theme_preferences, rootKey)
    }
}