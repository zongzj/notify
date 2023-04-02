package com.zong.call.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.navigation.NavigationBarView
import com.zong.common.base.fragment.BaseBindingFragment
import com.zong.call.R
import com.zong.call.databinding.FragmentMainBinding
import com.zong.call.ui.app.AppFragment
import com.zong.call.ui.mine.MineFragment
import me.yokeyword.fragmentation.SupportFragment

class MainFragment : BaseBindingFragment<FragmentMainBinding>() {

    override fun viewCreated(binding: FragmentMainBinding) {
        binding.apply {
            bottomNavigationView.inflateMenu(R.menu.main_menu)

            binding.bottomNavigationView.labelVisibilityMode =
                NavigationBarView.LABEL_VISIBILITY_SELECTED
            //是否滑动
//            viewpager.isUserInputEnabled = false
            viewpager.offscreenPageLimit = 4
            viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.bottomNavigationView.menu
                        .getItem(position).isChecked = true
                }
            })
            //设置适配器
            viewpager.adapter = object : FragmentStateAdapter(this@MainFragment) {
                override fun createFragment(position: Int): Fragment {
                    when (position) {
                        0 -> {
                            return AppFragment()
                        }
                        1 -> {
                            return MineFragment()
                        }
                        2 -> {
                            return MineFragment()
                        }
                        else -> {
                            return HomeFragment()
                        }
                    }
                }

                override fun getItemCount() = 2
            }
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.bottom1 -> {
                        viewpager.setCurrentItem(0, false)
                    }
                    R.id.bottom2 -> {
                        viewpager.setCurrentItem(1, false)
                    }
                    R.id.bottom3 -> {
                        viewpager.setCurrentItem(2, false)
                    }
                }
                true
            }

        }

    }

    /**
     * start other BrotherFragment
     */
    fun startBrotherFragment(targetFragment: SupportFragment?) {
        start(targetFragment)
    }
}