package com.hinacle.base.app

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class AppFragment(val layoutId: Int) : Fragment(layoutId) {
    private var isFirst = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        superOnCreate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    override fun onResume() {
        super.onResume()
        superOnResume()
        if (!isFirst) return
        lateInit()
        isFirst = false
    }

    open fun superOnCreate() {}
    open fun superOnResume() {}
    open fun initView() {}
    open fun initData() {}

    // 只调用一次的onResume viewpager2使用 实现懒加载
    open fun lateInit() {}
}