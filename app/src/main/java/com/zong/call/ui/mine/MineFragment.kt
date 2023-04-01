package com.zong.call.ui.mine

import com.zong.common.base.fragment.BaseBindingFragment
import com.zong.call.R
import com.zong.call.databinding.FragmentMineBinding
import com.zong.call.ext.FEEDBACK_URL
import com.zong.call.ext.ZHINAN
import com.zong.call.ext.initToolbar
import com.zong.call.ui.MainFragment

class MineFragment : BaseBindingFragment<FragmentMineBinding>() {
    override fun viewCreated(binding: FragmentMineBinding) {
        binding.includeToolbar.toolbar.run {
            initToolbar("我的")
//            inflateMenu(R.menu.mine_menu)
//            setOnMenuItemClickListener {
//                when (it.itemId) {
//                   R.id.home_search -> {
//                       nav().navigateAction(R.id.action_mainfragment_to_searchFragment)
//                   }
//                }
//                true
//            }
        }
        binding.apply {
            arrayOf(tvGuide, tvPermission, tvFeedback, tvAbout,tvSound).forEach {layout->
                layout.setmOnLSettingItemClick {
                    when (layout.id) {
                        R.id.tv_guide -> {
                            (parentFragment as MainFragment?)!!.startBrotherFragment(WebViewFragment.
                            newInstance(ZHINAN,"使用指南"))
                        }
                        R.id.tv_permission -> {
                            (parentFragment as MainFragment?)!!.startBrotherFragment(PermissionFragment())
                        }
                        R.id.tv_sound -> {
                            (parentFragment as MainFragment?)!!.startBrotherFragment(SoundSettingFragment())
                        }
                        R.id.tv_feedback -> {

                            (parentFragment as MainFragment?)!!.startBrotherFragment(WebViewFragment.
                            newInstance(FEEDBACK_URL,"反馈"))

                        }
                        R.id.tv_about -> {
                            (parentFragment as MainFragment?)!!.startBrotherFragment(AboutFragment())

                        }
                    }
                }
            }
        }


    }



}