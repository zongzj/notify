package com.zong.common.recyclerview.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.android.common.recyclerview.BaseHolder
import kotlin.reflect.KClass

/**
 * @author administrator
 */
class GeneralListAdapter<H : BaseHolder<T, *>, T : Any>(
    private val kClass: KClass<H>,
    diffCallback: DiffUtil.ItemCallback<T>
) :
    ListAdapter<T, BaseHolder<T, *>>(diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<T, *> {
        val constructor = kClass.constructors.first()
        val map = mapOf(constructor.parameters[0] to parent)
        val instance = constructor.callBy(map)
        instance.initView()
        instance.setClick(click)
        instance.anyData(anyData)
        return instance
    }

    override fun onBindViewHolder(holder: BaseHolder<T, *>, position: Int) {

        holder.bindData(getItem(position))
    }


    private var click: View.() -> Unit = {

    }

    fun setItemClick(block: View.() -> Unit) {
        click = block
    }

    private var anyData: Any = ""

    fun setAnyData(data: Any) {
        anyData = data
    }

    override fun getItemCount(): Int {
        Log.e("getItemCount", super.getItemCount().toString())
        return super.getItemCount()
    }

}
