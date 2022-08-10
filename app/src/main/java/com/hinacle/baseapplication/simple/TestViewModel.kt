package com.hinacle.baseapplication.simple

import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeNetLife
import com.hinacle.base.vm.AppViewModel
import com.hinacle.base.vm.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor():AppViewModel() {
    @Inject
    lateinit var netModel: NetModel
    // 单次 SingleLiveEvent
    val getLiveData by lazy {
        SingleLiveEvent<String>()
    }

    // 使用scopeNetLife扩展函数 在viewModel销毁时自动取消协程和网络请求 自动处理网络错误（全局）
    fun getData() = scopeNetLife {
        getLiveData.postValue(netModel.getRequest("param"))
    }

}