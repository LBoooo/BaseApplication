package com.hinacle.baseapplication.simple

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.scopeNetLife
import androidx.lifecycle.viewModelScope
import com.hinacle.base.data.Paging
import com.hinacle.base.util.logcat.LogPriority
import com.hinacle.base.util.logcat.logcat
import com.hinacle.base.vm.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // hilt注解 注入当前viewModel
class NetViewModel @Inject constructor() : AppViewModel() {

    // 可以用注解 也可以用代理 写法也很多样
    // eg：lazy代理 val netModel by lazy { NetModel() }
    // eg：可以直接放到构造方法里 class NetViewModel @Inject constructor(val netModel:NetModel)
    @Inject
    lateinit var netModel: NetModel


    // 单次 SingleLiveEvent
    val getLiveData by lazy {
        MutableLiveData<String>()
    }

    // 使用scopeNetLife扩展函数 在viewModel销毁时自动取消协程和网络请求 自动处理网络错误（全局）
    fun getData() = scopeNetLife {
        getLiveData.postValue(netModel.getRequest("param"))
    }

    // 使用google协程 需要自己处理异常
    fun getData2() = viewModelScope.launch {
        runCatching {}
            .onSuccess {}
            .onFailure {}
    }

//    lateinit var pagingData: LiveData<Listing<String>>


    private fun requestPaging(page: Int, step: Int) = scopeNetLife {
        logcat { "请求第${page}页的数据" }
        val data = netModel.getPagingData(page, step)
        pagingListData.pagedList.postValue(data)
    }

    val pagingListData: Paging<String> = Paging<String>().apply {
        request = {
            requestPaging(page, step)
//                .finally { throwable ->
//                    logcat { throwable?.message ?: "没有异常" }
//                }
        }
    }

    /*
    这是google官方推荐写法  给外部暴露的数据使用livedata无法改变数据 内部使用MutableLiveData更改数据
    初始化user的livedata时调用loadUser函数 但是这时应该是无法使用 user的MutableLiveData的 没想清楚怎么回事 可能就是为了这种模式做的示例吧
    class MyViewModel : ViewModel() {
    private val users: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().also {
            loadUsers()
        }
    }
    fun getUsers(): LiveData<List<User>> {
        return users
    }
    private fun loadUsers() {
        // Do an asynchronous operation to fetch users.
    }
}
     */

    override fun onCleared() {
        super.onCleared()
        logcat(LogPriority.ERROR) { "viewModel.onCleared" }
    }
}