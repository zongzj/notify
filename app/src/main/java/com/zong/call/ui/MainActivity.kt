package com.zong.call.ui

import android.content.Intent
import com.zong.common.base.activity.BaseActivity
import com.zong.call.R
import com.zong.call.constant.SoundConfig
import com.zong.call.databinding.ActivityMainBinding
import com.zong.call.utils.MMKVUtils

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun initIntent(intent: Intent) {
//        appDb.filterWord.insert(FilterWord())
    }

    override fun initUi() {
        if (findFragment(MainFragment::class.java) == null) {
            loadRootFragment(R.id.fragment_container_view, MainFragment())
        }
        initFirstConfig()
    }

    private fun initFirstConfig() {
        if (MMKVUtils.is_first_install) {
            MMKVUtils.is_first_install = false
            SoundConfig.getConfig().soundVolume=0
        }
    }

    override fun uiInteraction() {

    }

    override fun observerOnUi() {

    }
}