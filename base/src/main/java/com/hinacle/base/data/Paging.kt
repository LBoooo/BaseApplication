package com.hinacle.base.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map

/**
 * 请求更多时页码自动+1 刷新时归零
 * 加载更多失败时跳过当前页码
 * 没有更多数据时不请求网络数据
 */
open class Paging<T> {
    var page = 0
    var step = 2
    private var isCanLoadMore = true

    val pagedList: MutableLiveData<List<T>> = MutableLiveData()
    val pageStep = map(pagedList) {
        if (page == 0) {
            if (it.isEmpty()) {
                PagerState.R_NO_DATA
            } else {
                PagerState.R_SUCCESS
            }
        } else {
            if (it.isEmpty() || it.size < step) {
                disableLoadMore()
                PagerState.L_NO_MORE_DATA
            } else {
                PagerState.L_SUCCESS
            }
        }
    }


    var request = {}

    fun refresh() {
        page = 0
        enableLoadMore()
        request.invoke()
    }

    fun loadMore() {
        if (isCanLoadMore) {
            page++
            request.invoke()
        }
    }

    fun disableLoadMore() {
        isCanLoadMore = false
    }

    fun enableLoadMore() {
        isCanLoadMore = true
    }

    enum class PagerState {
        R_SUCCESS, R_NO_DATA,
        L_SUCCESS, L_NO_MORE_DATA
    }
}

