package com.mny.mojito.im.presentation.search

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.navigation.Navigation
import com.mny.mojito.base.BaseActivity
import com.mny.mojito.im.R
import com.mny.mojito.im.base.BaseToolbarActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : BaseToolbarActivity() {

    private var mSearchType = SEARCH_TYPE_USER
    private val mViewModel by viewModels<SearchViewModel>()

    override fun initArgs(bundle: Bundle?, savedInstanceState: Bundle?): Boolean {
        mSearchType = bundle?.getInt(EXTRA_TYPE, SEARCH_TYPE_USER) ?: return false
        return mSearchType == SEARCH_TYPE_USER || mSearchType == SEARCH_TYPE_GROUP
    }

    override fun layoutId(savedInstanceState: Bundle?): Int = R.layout.activity_search

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        // 默认是用户，群才做处理
        if (mSearchType == SEARCH_TYPE_GROUP) {
            navController.navigate(R.id.searchGroupFragment)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // 初始化菜单
        menuInflater.inflate(R.menu.search, menu)
        // 找到搜索菜单
        val searchItem = menu?.findItem(R.id.action_search)
        // 获取SearchView
        val searchView = searchItem?.actionView as? SearchView
        searchView?.apply {
            // 拿到一个搜索管理器
            val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            // 默认刚进去就打开搜索栏
            isIconified = true
//            setIconifiedByDefault(false)
            // 设置搜索栏的默认提示
            queryHint = "请输入关键词"
            // 获取输入框
            val et = findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)
            et.setHintTextColor(Color.WHITE)
            et.setTextColor(Color.WHITE)
            //设置触发查询的最少字符数（默认2个字符才会触发查询）
            et.setThreshold(1)
            // 设置提交按钮是否可见
            //  isSubmitButtonEnabled = true

            val searchEditFrame = findViewById<LinearLayout>(R.id.search_edit_frame)
            val params = searchEditFrame?.layoutParams as? ViewGroup.MarginLayoutParams
            params?.leftMargin = 0
            params?.rightMargin = 0
            searchEditFrame.layoutParams = params
            onActionViewExpanded()
            // 添加搜索监听
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String): Boolean {
                    // 当点击了提交按钮的时候
                    search(query)
                    return true
                }

                override fun onQueryTextChange(s: String): Boolean {
                    // 当文字改变的时候，不会实时搜索，只在为null的情况下进行搜索
                    if (TextUtils.isEmpty(s)) {
                        search("")
                        return true
                    }
                    return false
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    fun search(query:String){
        if (mSearchType == SEARCH_TYPE_GROUP){
            mViewModel.searchGroups(query)
        }else{
            mViewModel.searchUsers(query)
        }
    }

    companion object {
        const val SEARCH_TYPE_USER = 1
        const val SEARCH_TYPE_GROUP = 2
        private const val EXTRA_TYPE = "EXTRA_TYPE"
        fun go(activity: BaseActivity, type: Int) {
            val intent = Intent(activity, SearchActivity::class.java)
            intent.putExtra(EXTRA_TYPE, type)
            activity.startActivity(intent)
        }
    }
}