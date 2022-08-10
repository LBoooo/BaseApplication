package com.hinacle.base.di

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Listing<T> {
     var page = 0

    lateinit var pagedList: LiveData<T>

    lateinit var networkState: LiveData<Int>

    var refresh: () -> Unit  = {
        page = 0
    }

    var loadMore: () -> Unit = {
        page++
    }

}