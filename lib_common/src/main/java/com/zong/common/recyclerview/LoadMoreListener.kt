package com.zong.common.recyclerview

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * @author administrator
 */
class LoadMoreListener(
    private val onLoadMore: () -> Unit
) : RecyclerView.OnScrollListener() {

    //用来标记是否正在向上滑动
    private var isSlidingUpward = false
    override fun onScrollStateChanged(
        recyclerView: RecyclerView,
        newState: Int
    ) {
        super.onScrollStateChanged(recyclerView, newState)

        when (val manager = recyclerView.layoutManager) {
            is LinearLayoutManager -> {
                //获取最后一个完全显示的itemPosition
                val lastItemPosition = manager.findLastCompletelyVisibleItemPosition()
                val itemCount = manager.itemCount
                loadMore(newState, lastItemPosition, itemCount)
            }
            is GridLayoutManager -> {
                //获取最后一个完全显示的itemPosition
                val lastItemPosition = manager.findLastCompletelyVisibleItemPosition()
                val itemCount = manager.itemCount
                loadMore(newState, lastItemPosition, itemCount)
            }
            is StaggeredGridLayoutManager ->{
                //获取最后一个完全显示的itemPosition
                val lastArray = intArrayOf(0,0)
                val lastItemPosition = manager.findLastVisibleItemPositions(lastArray)
                val itemCount = manager.itemCount

                var loadMore = false

                lastArray.forEach {
                    if(itemCount - 1 == it){
                        loadMore = true
                    }
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 判断是否滑动到了最后一个item，并且是向上滑动
                    if (loadMore&& isSlidingUpward) {
                        //加载更多
                        onLoadMore()
                    }
                }

            }
        }


    }

    private fun loadMore(newState: Int, lastItemPosition: Int, itemCount: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            // 判断是否滑动到了最后一个item，并且是向上滑动
            if (lastItemPosition == itemCount - 1 && isSlidingUpward) {
                //加载更多
                onLoadMore()
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
        isSlidingUpward = dy > 0
    }

}
