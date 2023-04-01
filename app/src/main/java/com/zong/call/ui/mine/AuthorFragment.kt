package com.zong.call.ui.mine

import com.zong.common.base.fragment.BaseBindingFragment
import com.zong.call.databinding.FragmentAuthorBinding
import com.zong.call.ext.initToolbarClose

class AuthorFragment: BaseBindingFragment<FragmentAuthorBinding>() {
    override fun viewCreated(binding: FragmentAuthorBinding) {
        binding.includeToolbar.toolbar.run {
            initToolbarClose("关于") {
                _mActivity.onBackPressed()
            }
        }
    }
}