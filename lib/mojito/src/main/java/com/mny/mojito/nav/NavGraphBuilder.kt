package com.mny.mojito.nav

import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavigatorProvider

object NavGraphBuilder {
    fun build(activity: FragmentActivity, controller: NavController, containerId: Int) {
        val provider: NavigatorProvider = controller.navigatorProvider
        //FragmentNavigator fragmentNavigator = provider.getNavigator(FragmentNavigator.class);
        //fragment的导航此处使用我们定制的FixFragmentNavigator，底部Tab切换时 使用hide()/show(),而不是replace()
        val fragmentNavigator = FixFragmentNavigator(activity, activity.supportFragmentManager, containerId)
        provider.addNavigator(fragmentNavigator)

        //NavGraphNavigator也是页面路由导航器的一种，只不过他比较特殊。
        //它只为默认的展示页提供导航服务,但真正的跳转还是交给对应的navigator来完成的
//        val navGraph = NavGraph(NavGraphNavigator(provider))

//        val activityNavigator: ActivityNavigator = provider.getNavigator<ActivityNavigator>(
//            ActivityNavigator::class.java
//        )
//        val destConfig: HashMap<String, Destination> = AppConfig.getDestConfig()
//        val iterator: Iterator<Destination> = destConfig.values.iterator()
//        while (iterator.hasNext()) {
//            val node: Destination = iterator.next()
//            if (node.isFragment) {
//                val destination: FragmentNavigator.Destination =
//                    fragmentNavigator.createDestination()
//                destination.setId(node.id)
//                destination.setClassName(node.className)
//                destination.addDeepLink(node.pageUrl)
//                navGraph.addDestination(destination)
//            } else {
//                val destination: ActivityNavigator.Destination =
//                    activityNavigator.createDestination()
//                destination.setId(node.id)
//                destination.setComponentName(
//                    ComponentName(
//                        AppGlobals.getApplication().getPackageName(), node.className
//                    )
//                )
//                destination.addDeepLink(node.pageUrl)
//                navGraph.addDestination(destination)
//            }
//
//            //给APP页面导航结果图 设置一个默认的展示页的id
//            if (node.asStarter) {
//                navGraph.setStartDestination(node.id)
//            }
//        }
//        controller.setGraph(navGraph)
    }
}