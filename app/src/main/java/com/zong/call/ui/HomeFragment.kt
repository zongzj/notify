package com.zong.call.ui

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.zong.common.base.fragment.BaseBindingFragment
import com.zong.common.ext.GSON
import com.zong.common.ext.fromJsonArray
import com.zong.common.utils.LogUtils
import com.zong.call.R
import com.zong.call.adapter.FunctionAdapter
import com.zong.call.bean.FunctionBean
import com.zong.call.databinding.FragmentHomeBinding
import com.zong.call.ext.initToolbar
import com.zong.call.utils.MMKVUtils

class HomeFragment : BaseBindingFragment<FragmentHomeBinding>() {
    override fun viewCreated(binding: FragmentHomeBinding) {
        binding.includeToolbar.toolbar.run {
            initToolbar("首页")
//            inflateMenu(R.menu.home_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
//                    R.id.home_search -> {
//                        nav().navigateAction(R.id.action_mainfragment_to_searchFragment)
//                    }
                }
                true
            }

        }

        initUI()

    }

    private fun initUI() {

    }
}