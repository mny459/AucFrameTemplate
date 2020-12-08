package com.mny.mojito.mvvm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 *@author mny on 2020/5/17.
 *        Email：mny9@outlook.com
 *        Desc:
 */
open class BaseModel() : LifecycleObserver {
//    val mRepository: IRepository by inject()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
//        mRepository = null
    }

}