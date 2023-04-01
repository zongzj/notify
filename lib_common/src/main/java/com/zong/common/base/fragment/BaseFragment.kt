package com.zong.common.base.fragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.MenuInflater
import androidx.appcompat.view.SupportMenuInflater
import androidx.appcompat.widget.Toolbar
import com.zong.common.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import me.yokeyword.fragmentation.SupportFragment

/**
 * @author administrator
 */
abstract class BaseFragment : SupportFragment(), CoroutineScope by MainScope() {
    val TAG = this.javaClass.name
    var supportToolbar: Toolbar? = null
        private set

    val menuInflater: MenuInflater
        @SuppressLint("RestrictedApi")
        get() = SupportMenuInflater(requireContext())


    protected open fun initToolbarNav(toolbar: Toolbar) {
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { _mActivity.onBackPressed() }
    }
    protected fun String.print() {
        Log.d("android_test1${this.javaClass.name}", this)
    }

    private fun logLifecycle(message: String) {
        Log.d("android_lifecycle${this.javaClass.name}", message)
    }
    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}
