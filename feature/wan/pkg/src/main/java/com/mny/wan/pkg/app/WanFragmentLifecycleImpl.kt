package com.mny.wan.pkg.app

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.LogUtils

class WanFragmentLifecycleImpl : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        super.onFragmentAttached(fm, f, context)
        LogUtils.v("onFragmentAttached() called with: fm = $fm, f = $f, context = $context")
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentCreated(fm, f, savedInstanceState)
        LogUtils.v("onFragmentCreated() called with: fm = $fm, f = $f, savedInstanceState = $savedInstanceState")
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        LogUtils.v("onFragmentViewCreated() called with: fm = $fm, f = $f, v = $v, savedInstanceState = $savedInstanceState")
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        LogUtils.v("onFragmentActivityCreated() called with: fm = $fm, f = $f, savedInstanceState = $savedInstanceState")
        super.onFragmentActivityCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        super.onFragmentStarted(fm, f)
        LogUtils.v("onFragmentStarted() called with: fm = $fm, f = $f")
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        super.onFragmentResumed(fm, f)
        LogUtils.v("onFragmentResumed() called with: fm = $fm, f = $f")
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        super.onFragmentPaused(fm, f)
        LogUtils.v("onFragmentPaused() called with: fm = $fm, f = $f")
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        super.onFragmentStopped(fm, f)
        LogUtils.v("onFragmentStopped() called with: fm = $fm, f = $f")
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentViewDestroyed(fm, f)
        LogUtils.v("onFragmentViewDestroyed() called with: fm = $fm, f = $f")
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentDestroyed(fm, f)
        LogUtils.v("onFragmentDestroyed() called with: fm = $fm, f = $f")
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        super.onFragmentDetached(fm, f)
        LogUtils.v("onFragmentDetached() called with: fm = $fm, f = $f")
    }

}