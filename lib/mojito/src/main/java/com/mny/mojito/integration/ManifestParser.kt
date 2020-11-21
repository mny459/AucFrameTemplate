package com.mny.mojito.integration

import android.content.Context
import android.content.pm.PackageManager
import java.util.*

class ManifestParser(private val context: Context) {
    companion object {
        private const val MODULE_VALUE = "ModuleConfig"
    }

    fun parse(): List<ModuleConfig> {
        val modules: MutableList<ModuleConfig> = ArrayList<ModuleConfig>()
        try {
            val appInfo = context.packageManager.getApplicationInfo(
                    context.packageName, PackageManager.GET_META_DATA)
            if (appInfo.metaData != null) {
                for (key in appInfo.metaData.keySet()) {
                    if (MODULE_VALUE == appInfo.metaData[key]) {
                        modules.add(parseModule(key))
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("Unable to find metadata to parse ModuleConfig", e)
        }
        return modules
    }

    private fun parseModule(className: String): ModuleConfig {
        val clazz: Class<*> = try {
            Class.forName(className)
        } catch (e: ClassNotFoundException) {
            throw IllegalArgumentException("Unable to find ModuleConfig implementation", e)
        }
        val module = try {
            clazz.newInstance()
        } catch (e: InstantiationException) {
            throw RuntimeException("Unable to instantiate ModuleConfig implementation for $clazz", e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Unable to instantiate ModuleConfig implementation for $clazz", e)
        }
        if (module !is ModuleConfig) {
            throw RuntimeException("Expected instanceof ModuleConfig, but found: $module")
        }
        return module
    }
}