package com.mny.wan.im.presentation.main.contact

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mny.wan.entension.observe
import com.mny.wan.im.R
import com.mny.wan.im.base.BaseRecyclerViewFragment
import com.mny.wan.im.data.db.entity.UserEntity
import com.mny.wan.im.presentation.main.adapter.ContactAdapter
import com.mny.wan.im.widget.EmptyView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ContactFragment : BaseRecyclerViewFragment<UserEntity, ContactAdapter>(R.layout.fragment_contact) {
    private var mRvContacts: RecyclerView? = null
    private var mEmptyView: EmptyView? = null
    private var mRefresh: SmartRefreshLayout? = null

    @Inject
    lateinit var mAdapter: ContactAdapter
    private val mViewModel: ContactViewModel by viewModels()

    override fun initObserver() {
        super.initObserver()
        observe(mViewModel.stateLiveData, ViewStateObserver())
        observe(mViewModel.contacts, ContactStateObserver())
    }

    override fun getAdapter(): ContactAdapter = mAdapter
    override fun initView(view: View) {
        super.initView(view)
        mRvContacts = view.findViewById<RecyclerView>(R.id.rv_contacts)
        mRvContacts?.layoutManager = LinearLayoutManager(mActivity)
        mRvContacts?.adapter = mAdapter
        mRefresh = view.findViewById(R.id.refresh)
        mRefresh?.setOnRefreshListener {
            lifecycleScope.launch {
                mViewModel.loadData()
                it.finishRefresh(1000)
            }
        }

        mEmptyView = view.findViewById<EmptyView>(R.id.empty_view)
        mEmptyView?.bind(mRvContacts)
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mViewModel.loadData()
    }

    inner class ViewStateObserver : Observer<ContactViewModel.State> {
        override fun onChanged(state: ContactViewModel.State) {
            if (state.isLoading) {
                mEmptyView?.triggerLoading()
            } else {

            }
        }
    }

    inner class ContactStateObserver : Observer<List<UserEntity>> {
        override fun onChanged(users: List<UserEntity>) {
            diff(mAdapter.data, users)
            mEmptyView?.triggerOkOrEmpty(users.isNotEmpty())
        }
    }

    companion object {
        fun newInstance() = ContactFragment()
    }
}