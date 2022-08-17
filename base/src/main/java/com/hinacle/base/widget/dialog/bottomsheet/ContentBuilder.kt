package com.hinacle.base.widget.dialog.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class ContentBuilder<VB : ViewDataBinding> {
    lateinit var viewBinding :VB
    private lateinit var contentView: View
    abstract val layoutId: Int
    abstract fun init()
    private var afterShow: (() -> Unit)? = null

    /**
     * 用于 初始化视图
     * @param action Function0<Unit>
     */
    fun afterShow(action: () -> Unit) {
        afterShow = action
    }

    fun build(context: Context): View {
        if (!::contentView.isInitialized) {
//            contentView = LayoutInflater.from(context).inflate(layoutRes, null, false)
            viewBinding = DataBindingUtil.inflate(LayoutInflater.from(context) ,layoutId , null , false)
            contentView = viewBinding.root
            init()
            updateContent(-1)
            afterShow?.invoke()

        }
        return contentView
    }

    open fun onAfterShow() {}
    open fun onShow() {}

//    fun listenStatus(lis: StatusCallback) {
//        dialog.listenStatus(lis)
//    }

    /**
     * 更新内容 渲染 装饰
     * 初始化时，type为-1
     * 供[listenToUpdate]监听
     * @param type Int 可用于局部刷新
     */
    abstract fun updateContent(type: Int, data: Any? = null)

}