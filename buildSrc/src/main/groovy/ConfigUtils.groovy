import org.gradle.api.Project
import org.gradle.api.ProjectEvaluationListener
import org.gradle.api.ProjectState
import org.gradle.api.invocation.Gradle

class ConfigUtils {
    // 在 根 build.gradle 上调用，初始化各种依赖项
    static init(Gradle gradle) {
        generateDep(gradle)
        addCommonGradle(gradle)
        TaskDurationUtils.init(gradle)
    }

    /**
     * 根据 depConfig 生成 dep
     */
    private static void generateDep(Gradle gradle) {
        def configs = [:]
        for (Map.Entry<String, DepConfig> entry : Config.depConfig.entrySet()) {
            // 解构赋值 depConfig 的 key 和 值
            def (name, config) = [entry.key, entry.value]
            // 根据条件判断最终的 dep 是什么
            // 如果配置了 插件，那么就是插件
            if (config.pluginPath) {
                config.dep = config.pluginPath
            } else {
                if (config.useLocal) {
                    // 结果形如： project ':feature:launcher:feature_launcher_app'
                    config.dep = gradle.rootProject.findProject(config.projectPath)
                } else {
                    config.dep = config.remotePath
                }
            }
            configs.put(name, config)
        }
        GLog.l("generateDep = ${GLog.object2String(configs)}")
    }
    // 通过这种方式，可以写通用的 apply
    private static addCommonGradle(Gradle gradle) {
        gradle.addProjectEvaluationListener(new ProjectEvaluationListener() {
            @Override
            void beforeEvaluate(Project project) {
                // 在 project 的 build.gradle 前 do sth.
                if (project.subprojects.isEmpty()) {
                    // 表明不是 Project 的 build.gradle
                    // 而是 module 的 build.gradle
                    if (project.path.contains(":plugin:")) {
                        // 需要遵守约定，插件都以 plugin 作为包名
                        return
                    }
                    if (project.name.endsWith("_app")) {
                        GLog.l(project.toString() + " applies buildApp.gradle")
                        // 给 app 设置 buildApp.gradle
                        project.apply {
                            from "${project.rootDir.path}/buildApp.gradle"
                        }
                    } else {
                        GLog.l(project.toString() + " applies buildLib.gradle")
                        // 其他的 设置 buildLib.gradle
                        project.apply {
                            from "${project.rootDir.path}/buildLib.gradle"
                        }
                    }
                }
            }

            @Override
            void afterEvaluate(Project project, ProjectState state) {
                // 在 project 的 build.gradle 末 do sth.
            }
        })
    }
    // 获取所有的 plugin，命名都以 plugin_ 起头
    static getApplyPlugins() {
        def plugins = [:]
        for (Map.Entry<String, DepConfig> entry : Config.depConfig.entrySet()) {
            if (entry.value.isApply && entry.key.startsWith("plugin_")) {
                plugins.put(entry.key, entry.value)
            }
        }
        GLog.d("getApplyPlugins = ${GLog.object2String(plugins)}")
        return plugins
    }

    static getApplyPkgs() {
        def pkgs = [:]
        for (Map.Entry<String, DepConfig> entry : Config.depConfig.entrySet()) {
            if (entry.value.isApply && entry.key.endsWith("_pkg")) {
                pkgs.put(entry.key, entry.value)
            }
        }
        GLog.d("getApplyPkgs = ${GLog.object2String(pkgs)}")
        return pkgs
    }

    static getApplyExports() {
        def exports = [:]
        for (Map.Entry<String, DepConfig> entry : Config.depConfig.entrySet()) {
            if (entry.value.isApply && entry.key.endsWith("_export")) {
                exports.put(entry.key, entry.value)
            }
        }
        GLog.d("getApplyExports = ${GLog.object2String(exports)}")
        return exports
    }
}