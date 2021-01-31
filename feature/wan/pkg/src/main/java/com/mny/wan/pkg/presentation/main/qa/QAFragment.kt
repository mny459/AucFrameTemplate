package com.mny.wan.pkg.presentation.main.qa

import androidx.fragment.app.activityViewModels
import com.mny.wan.pkg.R
import com.mny.wan.pkg.presentation.article.BaseArticleFragment
import com.mny.wan.pkg.presentation.article.BaseArticleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QAFragment : BaseArticleFragment(R.layout.fragment_q_a) {

    private val mViewModel: QAViewModel by activityViewModels()

    override fun initArticleViewModel(): BaseArticleViewModel<*, *> = mViewModel

    companion object {
        @JvmStatic
        fun newInstance() = QAFragment()
    }
}