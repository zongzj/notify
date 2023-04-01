package com.android.common.recyclerview.manager

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author administrator
 */
class NoScrollLinearLayoutManager : LinearLayoutManager {

    constructor(context: Context?) : super(context){

    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes){

    }

    constructor(
        context: Context,
        @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
        reverseLayout: Boolean = false
    ) : super(context, orientation, reverseLayout){
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

    override fun canScrollVertically(): Boolean {
        return false
    }

    override fun canScrollHorizontally(): Boolean {
        return false
    }


}
