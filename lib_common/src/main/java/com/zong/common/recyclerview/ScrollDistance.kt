package com.zong.common.recyclerview

import androidx.recyclerview.widget.RecyclerView

/**
 * @author administrator
 */
class ScrollDistance(private val block: (Int) -> Unit) : RecyclerView.OnScrollListener() {

    private var scrollY = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        scrollY += dy

        block.invoke(scrollY)

    }

}
