package com.zong.common.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.zong.common.ext.inflateBindingWithGeneric


/**
 * @author administrator
 */
abstract class BaseBindingFragment<VB : ViewBinding> : BaseFragment() {

    protected var binding: VB? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = inflateBindingWithGeneric(inflater,container,false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            viewCreated(this)
        }
    }

    abstract fun viewCreated(binding:VB)

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}
