package com.zong.common.base.activity

import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass

/**
 * @author administrator
 */
abstract class BaseViewModelActivity<VB : ViewBinding, VM : ViewModel>(kClass: KClass<VM>) :
    BaseActivity<VB>() {

    /**
     * 创建ViewModel
     */
    protected val viewModel by lazy {
        ViewModelLazy(kClass, { viewModelStore }, {defaultViewModelProviderFactory}).value
    }


    protected fun <T> LiveData<T>.observerOnUi(block: (T) -> Unit) =
        observe(this@BaseViewModelActivity, Observer { block.invoke(it) })


}
