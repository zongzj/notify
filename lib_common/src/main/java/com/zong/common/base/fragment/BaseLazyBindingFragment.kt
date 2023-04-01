package com.zong.common.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding


/**
 * @author administrator
 */
abstract class BaseLazyBindingFragment<V : ViewBinding> : BaseFragment() {

    protected var binding: V? = null

    //是否第一次加载
    private var isFirst: Boolean = true
    abstract fun createBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = createBinding(inflater, container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = true

        binding?.apply {
            viewCreated(this)
        }
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()
            isFirst = false

        }
    }

    /**
     * 懒加载
     */
    abstract fun lazyLoadData()
    abstract fun viewCreated(binding: V)

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}
