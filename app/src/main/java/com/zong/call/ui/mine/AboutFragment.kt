package com.zong.call.ui.mine

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.zong.common.base.fragment.BaseBindingFragment
import com.zong.common.ext.dp
import com.zong.common.ext.imageRipple
import com.zong.common.ext.toastOnUi
import com.zong.call.R
import com.zong.call.databinding.FragmentAboutBinding
import com.zong.call.ext.Lianxi
import com.zong.call.ext.YIN_SI
import com.zong.call.ext.initToolbarClose
import com.zong.call.ext.joinQQGroup
import com.zong.call.ui.MainFragment
import com.zong.call.utils.MarketUtils
import splitties.init.appCtx

class AboutFragment : BaseBindingFragment<FragmentAboutBinding>() {
    override fun viewCreated(binding: FragmentAboutBinding) {
        binding.includeToolbar.toolbar.run {
            initToolbarClose("关于") {
                _mActivity.onBackPressed()
            }
        }
        binding.apply {
            arrayOf(tvMarket, tvPrivacy, tvProtocol, tvUpdate, tvGzh, tvQq, tvLianxi).forEachIndexed { index, uiTextView ->
                uiTextView.imageRipple()
                uiTextView.setLine(R.color.lineColor3, 0.5.dp)
                uiTextView.setOnClickListener {
                    when (it.id) {
                        R.id.tv_market -> {
                            MarketUtils.launchAppDetail(requireActivity(), appCtx.packageName)
                        }
                        R.id.tv_privacy -> {
                            start(WebViewFragment.newInstance(YIN_SI, "隐私政策"))

                        }
                        R.id.tv_protocol -> {
                            (parentFragment as MainFragment?)!!.startBrotherFragment(WebViewFragment.newInstance("https://support.qq.com/embed/phone/349587/new-post", "用户协议"))
                        }
                        R.id.tv_update -> {
//                            var bundle = bundleOf("title" to "关于")
//                            openActivity(AboutActivity::class, bundle)
                            ToastUtils.showShort("已是最新版本")
                        }
                        R.id.tv_gzh -> {
                            ClipboardUtils.copyText("宗介网络科技")
                            toastOnUi("已复制")
                        }
                        R.id.tv_qq -> {
                            joinQQGroup("LfWts2z5Wg35imkJWSQ5HbKz-d1vb2_F")
                        }
                        R.id.tv_lianxi -> {
                            start(WebViewFragment.newInstance(Lianxi, "开发者"))
                        }

                    }
                }

            }
            tvUpdate.text = "版本更新（当前版本${AppUtils.getAppVersionName()}）"

        }
    }
}