package com.mny.wan.nav

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import java.util.*

/**
 * 定制的Fragment导航器，替换ft.replace(mContainerId, frag);为 hide()/show()
 */
@Navigator.Name("fixfragment")
class FixFragmentNavigator(
    private val mContext: Context,
    private val mManager: FragmentManager,
    private val mContainerId: Int
) : FragmentNavigator(
    mContext, mManager, mContainerId
) {
    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        if (mManager.isStateSaved) {
            Log.i(TAG, "Ignoring navigate() call: FragmentManager has already saved its state")
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = mContext.packageName + className
        }
        //final Fragment frag = instantiateFragment(mContext, mManager,
        //       className, args);
        //frag.setArguments(args);
        val ft = mManager.beginTransaction()
        // 转场动画
        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }
        // 1. 先隐藏当前显示的 Fragment
        val fragment = mManager.primaryNavigationFragment
        Log.d(TAG, "navigate: fragment = $fragment")
        if (fragment != null) {
            ft.hide(fragment)
            ft.setMaxLifecycle(fragment, Lifecycle.State.STARTED);
        }
        // 2. 显示目标 Fragment
        var frag: Fragment? = null
        val tag = destination.id.toString()
        Log.d(TAG, "navigate: fragment tag = $tag")
        frag = mManager.findFragmentByTag(tag)
        Log.d(TAG, "navigate: 将要显示的 frag = $frag")
        if (frag != null) {
            // 直接显示
            ft.show(frag)
            ft.setMaxLifecycle(frag,Lifecycle.State.RESUMED)
        } else {
            // 先创建、在现实
            frag = instantiateFragment(mContext, mManager, className, args)
            frag.arguments = args
            ft.add(mContainerId, frag, tag)
        }
        //ft.replace(mContainerId, frag);
        ft.setPrimaryNavigationFragment(frag)
        Log.d(TAG, "navigate: 将要显示的 frag = $frag")

        @IdRes val destId = destination.id
        var mBackStack: ArrayDeque<Int>? = null
        try {
            val field = FragmentNavigator::class.java.getDeclaredField("mBackStack")
            field.isAccessible = true
            mBackStack = field[this] as ArrayDeque<Int>
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        val initialNavigation = mBackStack!!.isEmpty()
        // TODO Build first class singleTop behavior for fragments
        val isSingleTopReplacement = (navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && mBackStack.peekLast() == destId)

        val isAdded: Boolean
        isAdded = when {
            initialNavigation -> {
                true
            }
            isSingleTopReplacement -> {
                // Single Top means we only want one instance on the back stack
                if (mBackStack.size > 1) {
                    // If the Fragment to be replaced is on the FragmentManager's
                    // back stack, a simple replace() isn't enough so we
                    // remove it from the back stack and put our replacement
                    // on the back stack in its place
                    mManager.popBackStack(
                        generateBackStackName(mBackStack.size, mBackStack.peekLast()),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    ft.addToBackStack(generateBackStackName(mBackStack.size, destId))
                }
                false
            }
            else -> {
                ft.addToBackStack(generateBackStackName(mBackStack.size + 1, destId))
                true
            }
        }
        Log.d(TAG, "navigate: isAdded = $isAdded")
        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key, value)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()
        // The commit succeeded, update our view of the world
        return if (isAdded) {
            mBackStack.add(destId)
            destination
        } else {
            null
        }
    }

    private fun generateBackStackName(backStackIndex: Int, destDd: Int): String {
        return "$backStackIndex-$destDd"
    }

    companion object {
        private const val TAG = "FixFragmentNavigator"
    }
}