package com.zong.common.recyclerview.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.common.recyclerview.BaseHolder
import kotlin.reflect.KClass

/**
 * @author administrator
 */
class GeneralAdapter<H : BaseHolder<T, *>, T : Any>(
    private val kClass: KClass<H>
) : RecyclerView.Adapter<BaseHolder<T, *>>() {

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
        data?.let {
            holder.bindData(it)
        }
    }

    private var anyData: Any = ""

    fun setAnyData(data: Any) {
        anyData = data
    }

    private var click: View.() -> Unit = {

    }

    fun setItemClick(block: View.() -> Unit) {
        click = block
    }

    override fun getItemCount() = 1

    private var data: T? = null

    fun setData(t: T) {
        this.data = t
        notifyDataSetChanged()
    }
}
