package com.mny.wan.di.factory

import com.mny.wan.di.module.GlobalModuleConfig

object GlobalConfigFactory {

    private var mConfig: GlobalModuleConfig? = null

    fun saveGlobalModuleConfig(config: GlobalModuleConfig) {
        this.mConfig = config
    }

    fun retrieveGlobalConfigModule(): GlobalModuleConfig {
        if (mConfig == null) {
            mConfig = GlobalModuleConfig.Builder().build()
        }
        return mConfig!!
    }
}