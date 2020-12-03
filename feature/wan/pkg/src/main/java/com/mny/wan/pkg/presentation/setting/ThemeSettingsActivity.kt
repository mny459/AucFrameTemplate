package com.mny.wan.pkg.presentation.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.blankj.utilcode.util.ActivityUtils
import com.mny.wan.pkg.R
import com.mny.wan.pkg.base.BaseToolbarActivity

class ThemeSettingsActivity : BaseToolbarActivity() {

    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.settings_activity

    companion object {
        fun show() {
            ActivityUtils.startActivity(ThemeSettingsActivity::class.java)
        }
    }
}