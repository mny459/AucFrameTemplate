package com.mny.wan.pkg.presentation.setting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Process
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LanguageUtils
import com.blankj.utilcode.util.LogUtils
import com.mny.mojito.mvvm.BaseAction
import com.mny.mojito.mvvm.BaseState
import com.mny.mojito.mvvm.BaseViewModel
import com.mny.wan.pkg.utils.SettingHelper
import com.mny.wan.pkg.utils.isNightMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class SettingsViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    @Assisted protected val mSavedStateHandle: SavedStateHandle
) :
    BaseViewModel<SettingsViewModel.ViewState, SettingsViewModel.Action>(ViewState()) {
    init {
        initState()
//        mSavedStateHandle.getLiveData<ViewState>(ViewState)
    }

    private fun initState() {
        val themeFollowSystem = SettingHelper.isThemeFollowSystem()
        val themeDark = if (themeFollowSystem) context.isNightMode()
        else SettingHelper.isThemeDark()
        LogUtils.d("初始化主题配置 是否跟随系统主题 $themeFollowSystem 是否采用深色模式 $themeDark")
        sendAction(
            Action.UpdateTheme(
                themeFollowSystem = themeFollowSystem,
                themeDark = themeDark,
                languageZH = !LanguageUtils.isAppliedLanguage(Locale.ENGLISH)
            )
        )
    }

    fun isThemeFollowSystem() = SettingHelper.isThemeFollowSystem()
    fun isThemeDark(): Boolean {
        return if (isThemeFollowSystem()) context.isNightMode()
        else SettingHelper.isThemeDark()
    }


    data class ViewState(
        val themeFollowSystem: Boolean = false,
        val themeDark: Boolean = false,
        val languageZH: Boolean = false,
    ) : BaseState

    sealed class Action : BaseAction {
        class UpdateTheme(
            val themeFollowSystem: Boolean? = null,
            val themeDark: Boolean? = null,
            val languageZH: Boolean? = null
        ) : Action()

        class UpdateLanguage(val languageZH: Boolean) : Action()
    }

    override fun onReduceState(viewAction: Action): ViewState = when (viewAction) {
        is Action.UpdateTheme -> state.copy(
            themeFollowSystem = viewAction.themeFollowSystem ?: state.themeFollowSystem,
            themeDark = viewAction.themeDark ?: state.themeDark,
            languageZH = viewAction.themeDark ?: state.languageZH,
        )
        is Action.UpdateLanguage -> state.copy(languageZH = viewAction.languageZH)
    }

    fun switchThemeFollowSystem(): Boolean {
        val follow = !state.themeFollowSystem
        sendAction(
            Action.UpdateTheme(
                themeFollowSystem = follow,
                themeDark = context.isNightMode()
            )

        )
        val restart = context.isNightMode() != state.themeDark
        viewModelScope.launch {
            delay(300)
            if (follow) {
                SettingHelper.setThemeFollowSystem()
            } else {
                SettingHelper.setThemeFollowAppSetting()
            }
        }
        return restart
    }

    fun switchThemeDark(): Boolean {
        if (state.themeFollowSystem) return false
        val dark = !state.themeDark
        sendAction(
            Action.UpdateTheme(
                themeFollowSystem = false,
                themeDark = dark
            )
        )
        viewModelScope.launch {
            delay(300)
            if (dark) SettingHelper.setThemeDark()
            else SettingHelper.setThemeLight()
        }
        return true
    }

    fun switchLanguage() {
        val appliedEnglish = LanguageUtils.isAppliedLanguage(Locale.ENGLISH)
        if (appliedEnglish) {
            LanguageUtils.applySystemLanguage()
        } else {
            LanguageUtils.applyLanguage(Locale.ENGLISH)
        }
        sendAction(
            Action.UpdateTheme(
                languageZH = !appliedEnglish
            )
        )
    }

    fun restartApp() {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            context.startActivity(intent)
            Process.killProcess(Process.myPid())
        } else {
            finishActivityWithoutCount(1)
            if (ActivityUtils.getActivityList().isNotEmpty()) {
                ActivityUtils.getActivityList().get(0).recreate()
            }
        }
    }

    fun recreate() {
        if (ActivityUtils.getActivityList() != null && ActivityUtils.getActivityList()
                .isNotEmpty()
        ) {
            for (activity in ActivityUtils.getActivityList()) {
                activity.recreate()
            }
        }
    }

    fun finishActivityWithoutCount(count: Int) {
        if (ActivityUtils.getActivityList().isEmpty()) {
            return
        }
        if (count <= 0) {
            ActivityUtils.finishAllActivities()
            return
        }
        for (i in ActivityUtils.getActivityList().size - 1 downTo count) {
            ActivityUtils.finishActivity(ActivityUtils.getActivityList().get(i))
        }
    }

    fun finishActivityWithout(cls: Class<out Activity?>?) {
        if (cls == null) {
            ActivityUtils.finishAllActivities()
            return
        }
        if (ActivityUtils.getActivityList().isEmpty()) {
            return
        }
        for (i in ActivityUtils.getActivityList().indices.reversed()) {
            val activity: Activity = ActivityUtils.getActivityList().get(i)
            if (cls != activity.javaClass) {
                ActivityUtils.finishActivity(activity)
            }
        }
    }


}