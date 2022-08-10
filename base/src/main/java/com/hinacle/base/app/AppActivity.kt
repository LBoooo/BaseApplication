package com.hinacle.base.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hinacle.base.vm.AppViewModel

abstract class AppActivity(private val layoutId: Int) : AppCompatActivity(layoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        superOnCreate()
        initView()
        initData()
    }

    override fun onResume() {
        super.onResume()
        superOnResume()
    }

//    fun bindViewModel(viewModel:AppViewModel){
//        lifecycle.addObserver(viewModel)
//    }
//
//    fun unBindViewModel(viewModel:AppViewModel){
//        lifecycle.removeObserver(viewModel)
//    }
    open fun superOnCreate() {}
    open fun superOnResume() {}
    open fun initView() {}
    open fun initData() {}

    // 支持normal标题栏的返回函数
    open fun onBackClick(v: View) {
        onBackPressed()
    }

    // 支持带按钮的标题栏的按钮点击函数 这个其实可以放到具体activity中实现
    open fun onTitleButtonClick(v: View) {

    }
}