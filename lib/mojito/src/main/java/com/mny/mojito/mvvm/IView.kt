package com.mny.mojito.mvvm

/**
 *@author mny on 2020/5/17.
 *        Email：mny9@outlook.com
 *        Desc:
 */
interface IView {
    fun showLoading(msg:String?){}
    fun hideLoading(){}
    fun showMessage(msg: String){}
    fun killMySelf(){}
}