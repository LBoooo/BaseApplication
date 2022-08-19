package com.hinacle.baseapplication.simple

import androidx.activity.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hinacle.base.app.AppActivity
import com.hinacle.base.util.logcat.logcat
import com.hinacle.base.util.onShakeClickListener
import com.hinacle.base.util.toast.toast
import com.hinacle.baseapplication.R
import com.hinacle.baseapplication.databinding.ActivityNetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // hilt注解 注入当前activity
class NetActivity : AppActivity(R.layout.activity_net) {

    // 这里代理viewBinding 使用的开源项目 没有用反射版本 反射会影响性能
    // 反射版本 写法和viewModel是一样的 private val viewBinding by viewBinding<ActivityNetBinding>()
    // 使用带反射版本的需要在gradle配置文件依赖相应版本
    private val viewBinding by viewBinding(ActivityNetBinding::bind)

    // 这里使用google的jetpack 模组 代理viewModel
    // 等同于  private val viewModel :NetViewModel by viewModels()
    // fragment 共享数据使用 by activityViewModels() 代理函数
    private val viewModel by viewModels<NetViewModel>()


    override fun initView() {
        with(viewBinding) {
            refreshBtn.onShakeClickListener {
                viewModel.pagingListData.refresh()
            }

            loadMoreBtn.onShakeClickListener {
                viewModel.pagingListData.loadMore()
            }
        }
    }

    override fun initData() {
        viewModel.pagingListData.pagedList.observe(this){
            logcat { it.toString() }
        }

        viewModel.pagingListData.pageStep.observe(this){
            // 刷新 加载更多 数据状态
            logcat { it.name }
        }



//        // 作用域全局 慎用
//        scope {
//
//        }
//
//        // 作用域全局 慎用 可捕获异常 走onError回调
//        scopeNet {
//
//        }
//
//        // 作用域 当前activity activity销毁时自动取消
//        scopeLife {
//
//        }
//
//        // 作用域 当前activity activity销毁时自动取消 可捕获异常 走onError回调
//        scopeNetLife {
////            viewModel.getData()
//        }
//
//        // 开始时自动显示loading 结束时自动取消loading
//        scopeDialog {
//
//        }

        // 以上是net网络框架的简单用法 为了符合mvvm设计模式 请使用以下方式

//        // 请求网络获取数据 可以在具体位置时访问
//        viewModel.getData()
//
//        // 注册请求回调
//        viewModel.getLiveData.observe(this) {
//
//        }





    }



}