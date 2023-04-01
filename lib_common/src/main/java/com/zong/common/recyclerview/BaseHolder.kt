package com.android.common.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author administrator
 */
abstract class BaseHolder<D : Any, V : ViewBinding>(protected val binding: V) :
    RecyclerView.ViewHolder(binding.root) {

    abstract fun bindData(data: D)

    abstract fun setClick(click: View.() -> Unit)

    abstract fun initView()

    open fun anyData(data: Any){

    }


}
