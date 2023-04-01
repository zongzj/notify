package com.zong.common.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.zong.common.recyclerview.LoadMoreListener

/**
 * @author administrator
 */

fun RecyclerView.loadMore(block: () -> Unit) {
    addOnScrollListener(LoadMoreListener(block))
}
