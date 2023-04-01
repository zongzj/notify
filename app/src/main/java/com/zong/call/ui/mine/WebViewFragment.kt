package com.zong.call.ui.mine

import android.annotation.TargetApi
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.*
import com.blankj.utilcode.util.RomUtils
import com.zong.common.base.fragment.BaseBindingFragment
import com.zong.common.utils.LogUtils
import com.zong.call.R
import com.zong.call.databinding.FragmentPermissionBinding
import com.zong.call.databinding.FragmentWebviewBinding
import com.zong.call.ext.initSetting
import com.zong.call.ext.initToolbarClose
import com.zong.call.utils.MMKVUtils
import com.zong.call.utils.SelectImageContract
import com.zong.call.utils.launch

class WebViewFragment : BaseBindingFragment<FragmentWebviewBinding>() {
    var uploadMessage: ValueCallback<Uri?>? = null
    var uploadMessageAboveL: ValueCallback<Array<Uri?>?>? = null
    var FILE_CHOOSER_RESULT_CODE = 100

    override fun viewCreated(binding: FragmentWebviewBinding) {
        binding.includeToolbar.toolbar.run {
            var title = arguments?.getString("title").toString()
            initToolbarClose(title) {
                _mActivity.onBackPressed()
            }
        }
        binding.web.initSetting()
        var url = arguments?.getString("url").toString()
        binding.apply {
            val webViewClient: WebViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                    LogUtils.d(TAG, "url:" + url)
                    return try {
                        if (url!!.startsWith("http:") || url.startsWith("https:")) {
                            view.loadUrl(url)
                            true

                        } else {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                            false
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        false
                    }
                }
            }
            val webChrome: WebChromeClient = object : WebChromeClient() {
                fun openFileChooser(valueCallback: ValueCallback<Uri?>) {
                    uploadMessage = valueCallback
                    openImageChooserActivity()
                }

                //For Android API >= 11 (3.0 OS)
                fun openFileChooser(valueCallback: ValueCallback<Uri?>, acceptType: String?, capture: String?) {
                    uploadMessage = valueCallback
                    openImageChooserActivity()
                }

                //For Android API >= 21 (5.0 OS)
                override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri?>?>, fileChooserParams: FileChooserParams?): Boolean {
                    uploadMessageAboveL = filePathCallback
                    openImageChooserActivity()
                    return true
                }

            }

            web.webViewClient = webViewClient
            web.webChromeClient = webChrome
            web.loadUrl(url)

        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onActivityResultAboveL(intent: Intent?) {
        var results: Array<Uri?>? = null
        if (intent != null) {
            val dataString = intent.dataString
            val clipData: ClipData? = intent.clipData
            if (clipData != null) {
                results = arrayOfNulls(clipData.itemCount)
                for (i in 0 until clipData.itemCount) {
                    val item: ClipData.Item = clipData.getItemAt(i)
                    results[i] = item.uri
                }
            }
            if (dataString != null) results = arrayOf(Uri.parse(dataString))
        }

        uploadMessageAboveL!!.onReceiveValue(results)
        uploadMessageAboveL = null
    }

    private val selectImage = registerForActivityResult(SelectImageContract()) {
        if (null == uploadMessage && null == uploadMessageAboveL) return@registerForActivityResult
        it?.intent?.let { i ->
            val result: Uri? = i?.getData()
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(i)
            } else if (uploadMessage != null) {
                uploadMessage!!.onReceiveValue(result)
                uploadMessage = null
            }

        }
    }

    private fun openImageChooserActivity() {
//        if (MMKVUtils.is_agree){
        selectImage.launch()
//    }

    }

    override fun onBackPressedSupport(): Boolean {
        if (RomUtils.isHuawei()) {
            return super.onBackPressedSupport()
        }
        binding?.web?.let {
            if (it.canGoBack()) {
                it.settings.cacheMode = WebSettings.LOAD_NO_CACHE
                it.goBack()
                return super.onBackPressedSupport()

            }
        }

        return super.onBackPressedSupport()
    }

    override fun onDestroy() {
        binding?.web?.let {
            it.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            it.clearHistory();
            it.destroy()
        }
        super.onDestroy()

    }

    companion object {
        fun newInstance(url: String, title: String = ""): WebViewFragment {
            val args = Bundle()
            args.putString("url", url)
            args.putString("title", title)
            val fragment = WebViewFragment()
            fragment.arguments = args
            return fragment
        }
    }


}