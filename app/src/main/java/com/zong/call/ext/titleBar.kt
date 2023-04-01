package com.zong.call.ext

import androidx.appcompat.widget.Toolbar
import com.zong.common.ext.view.toHtml
import com.zong.call.R
import com.zong.call.utils.SettingUtil
import splitties.init.appCtx

/**
 * 初始化普通的toolbar 只设置标题
 */
fun Toolbar.initToolbar(titleStr: String = ""): Toolbar {
    setBackgroundColor(SettingUtil.getColor(appCtx))
    title = titleStr
    return this
}

/**
 * 初始化有返回键的toolbar
 */
fun Toolbar.initToolbarClose(
    titleStr: String = "",
    backImg: Int = R.mipmap.ic_back,
    onBack: (toolbar: Toolbar) -> Unit
): Toolbar {
    setBackgroundColor(SettingUtil.getColor(appCtx))
    title = titleStr.toHtml()
    setNavigationIcon(backImg)
    setNavigationOnClickListener { onBack.invoke(this) }
    return this
}