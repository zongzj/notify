package com.zong.call.ui.mine

import android.app.Activity
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.NotificationUtils
import com.blankj.utilcode.util.RomUtils
import com.zong.call.utils.NotifyOPPOUtil
import com.zong.common.base.fragment.BaseBindingFragment
import com.zong.common.ext.toastOnUi
import com.zong.call.R
import com.zong.call.databinding.FragmentPermissionBinding
import com.zong.call.ext.BAO_HUO
import com.zong.call.ext.ZHINAN
import com.zong.call.ext.initToolbarClose
import com.zong.call.ext.powerManager
import com.zong.call.ui.MainFragment
import com.zong.call.utils.AutoStartUtil.startToAutoStartSetting
import com.zong.call.utils.FloatWindowPermissionChecker
import splitties.init.appCtx
import splitties.systemservices.powerManager

class PermissionFragment : BaseBindingFragment<FragmentPermissionBinding>() {
    override fun viewCreated(binding: FragmentPermissionBinding) {
        binding.includeToolbar.toolbar.run {
            initToolbarClose("权限设置") {
                _mActivity.onBackPressed()
            }
            inflateMenu(R.menu.permission_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.question -> {
                        start(WebViewFragment.newInstance(BAO_HUO, "保活教程"))
                    }
                }
                true
            }
        }

        initUi()
    }

    private fun initUi() {

        binding?.apply {

            arrayOf(myBattery, myFloat, myNotify, myAuto).forEach { layout ->
                layout.setmOnLSettingItemClick {
                    when (layout.id) {
                        R.id.my_notify_content -> {
                            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                            startActivity(intent)
                        }
                        R.id.my_battery -> {
                            ignoreBatteryOptimization(requireActivity())
                        }
                        R.id.my_float -> {
                            if (!FloatWindowPermissionChecker.checkAlertWindowsPermission(requireActivity())) {
                                FloatWindowPermissionChecker.startActivityForResult(requireActivity(), 100)
                            } else {
                                toastOnUi("已授权")
                            }
                        }
                        R.id.my_notify -> {
                            NotifyOPPOUtil.openOPPOPush(requireActivity())
                        }
                        R.id.my_auto -> {
                            if (RomUtils.isHuawei()) {
                                AppUtils.launchApp("com.huawei.systemmanager")
                            } else {
                                startToAutoStartSetting(requireActivity())
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        binding?.initPermissionState()

    }

    /**
     * 忽略电池优化
     */
    private fun ignoreBatteryOptimization(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasIgnored = powerManager?.isIgnoringBatteryOptimizations(activity.packageName)
            //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
            if (!hasIgnored!!) {
                try {
                    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                    intent.data = Uri.parse("package:" + activity.packageName)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun FragmentPermissionBinding.initPermissionState() {
        myNotifyContent.isSelected = FloatWindowPermissionChecker.isEnabledNotify(requireActivity())
        myFloat.isSelected = FloatWindowPermissionChecker.checkAlertWindowsPermission(requireActivity())
        myNotify.isSelected = NotificationManagerCompat.from(appCtx).areNotificationsEnabled()
        val hasIgnored: Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            powerManager.isIgnoringBatteryOptimizations(requireActivity().packageName)
        } else {
            true
        }
        myBattery.isSelected = hasIgnored


    }

}