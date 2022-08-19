package com.hinacle.base.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map

open class Paging<T> {
    var page = 0
    var step = 2
    var pagedList: MutableLiveData<List<T>> = MutableLiveData()
    var pageStep = map(pagedList) {
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
    private var isCanLoadMore = false

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

