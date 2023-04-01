package com.zong.call.ext

import android.net.http.SslError
import android.os.Build
import android.view.View
import android.webkit.*


/**
 * @author administrator
 */
fun WebView.initSetting() {


//是否阻止图像资源加载显示
    settings.setBlockNetworkImage(false);
    settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
    settings.useWideViewPort = true
    settings.databaseEnabled = true
    // 设置是否开启DOM存储API权限，默认false，未开启，设置为true，WebView能够使用DOM storage API
    settings.domStorageEnabled = true
    //设置WebView是否使用预览模式加载界面。
    settings.loadWithOverviewMode = true
    //设置脚本是否允许自动打开弹窗，默认false，不允许
    settings.javaScriptCanOpenWindowsAutomatically = true
    // 当被设置为true，当前页面包含viewport属性标签，在标签中指定宽度值生效，如果页面不包含viewport标签，无法提供一个宽度值，这个时候该方法将被使用。
    settings.useWideViewPort = true
    //设置在WebView内部是否允许访问文件，默认允许访问。
    settings.allowFileAccess = true
    //设置WebView访问第三方Cookies策略，参考CookieManager提供的方法：

    //设置当一个安全站点企图加载来自一个不安全站点资源时WebView的行为，android.os.Build.VERSION_CODES.KITKAT默认为MIXED_CONTENT_ALWAYS_ALLOW，
    // android.os.Build.VERSION_CODES#LOLLIPOP默认为MIXED_CONTENT_NEVER_ALLOW，取值其中之一：MIXED_CONTENT_NEVER_ALLOW、MIXED_CONTENT_ALWAYS_ALLOW、MIXED_CONTENT_COMPATIBILITY_MODE.
    if (Build.VERSION.SDK_INT >= 21) {
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
    }
    scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

    settings.javaScriptEnabled = true
    settings.builtInZoomControls = false
    settings.setSupportZoom(false)
    webChromeClient = WebChromeClient()

    webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request?.url != null) {
                view?.loadUrl(request.url.toString())
            }
            if (request?.url?.toString()?.startsWith("tel") == true) {

                return false
            }
            return true
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            handler?.proceed()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }

    }


    /**
     * 对图片进行重置大小，宽度就是手机屏幕宽度，高度根据宽度比便自动缩放
     **/
     fun imgReset() {
        loadUrl(
            "javascript:(function(){" +
                    "var objs = document.getElementsByTagName('img'); " +
                    "for(var i=0;i<objs.length;i++)  " +
                    "{"
                    + "var img = objs[i];   " +
                    "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                    "}" +
                    "})()"
        );

    }


}
