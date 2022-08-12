package com.hinacle.appdialog.extensions

import android.content.DialogInterface
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.hinacle.appdialog.BR
import com.hinacle.appdialog.AppDialog
import com.hinacle.appdialog.listener.DataConvertListener
import com.hinacle.appdialog.listener.DialogShowOrDismissListener
import com.hinacle.appdialog.listener.OnKeyListener
import com.hinacle.appdialog.listener.ViewConvertListener
import com.hinacle.appdialog.other.DialogOptions
import com.hinacle.appdialog.other.ViewHolder
import kotlin.reflect.KClass

/**
 * 创建一个dialog
 * 你也可以继承AppDialog，实现更多功能，并通过扩展函数来简化创建过程
 */
inline fun newAppDialog(options: DialogOptions.(dialog: AppDialog) -> Unit): AppDialog {
    val appDialog = AppDialog()
    appDialog.getDialogOptions().options(appDialog)
    return appDialog
}

/**
 * 当需要通过继承AppDialog来分离Activity与dialog的代码时，
 * 可通过该扩展方法在子类中创建DialogOptions，可见AAA
 */
inline fun AppDialog.dialogOptionsFun(dialogOp: DialogOptions.() -> Unit): DialogOptions {
    val options = DialogOptions()
    options.dialogOp()
    setDialogOptions(options)
    return options
}

/**
 * 设置convertListener的扩展方法
 */
inline fun DialogOptions.convertListenerFun(crossinline listener: (holder: ViewHolder, dialog: AppDialog) -> Unit) {
    val viewConvertListener = object : ViewConvertListener() {
        override fun convertView(holder: ViewHolder, dialog: AppDialog) {
            listener.invoke(holder, dialog)
        }
    }
    convertListener = viewConvertListener
}

/**
 * 设置dataListener的扩展方法
 */
inline fun <VB : ViewDataBinding> DialogOptions.dataConvertListenerFun(
    bindingClass: KClass<VB>,
    crossinline listener: (dialogBinding: VB, dialog: AppDialog) -> Unit
) {
    val dataBindingConvertListener = object : DataConvertListener() {
        override fun convertView(dialogBinding: Any, dialog: AppDialog) {
            listener.invoke(dialogBinding as VB, dialog)
        }
    }
    dataConvertListener = dataBindingConvertListener
}

/**
 * 设置dataBindingListener的扩展方法
 */
inline fun <T : Any, VB : ViewDataBinding> DialogOptions.bindingListenerFun(
    data: T, bindingClass: KClass<VB>, crossinline listener: (dialogBinding: VB, dialog: AppDialog) -> Unit
) {
    val newBindingListener = {inflater:LayoutInflater, container: ViewGroup?, dialog: AppDialog ->
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, container, false) as VB
        binding.setVariable(BR.data, data)
        binding.lifecycleOwner = dialog
        listener.invoke(binding, dialog)
        dialog.dialogBinding = binding
        binding.root
    }
    bindingListener = newBindingListener
}

/**
 * 设置dataBindingListener的扩展方法 不设置data
 */
inline fun <VB :ViewDataBinding> DialogOptions.bindingListenerFun(
    bindingClass: KClass<VB>, crossinline listener: (dialogBinding: VB, dialog: AppDialog) -> Unit){
    val newBindingListener = {inflater:LayoutInflater, container: ViewGroup?, dialog: AppDialog ->
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, container, false) as VB
        binding.lifecycleOwner = dialog
        listener.invoke(binding, dialog)
        dialog.dialogBinding = binding
        binding.root
    }
    bindingListener = newBindingListener
}


/**
 * 添加DialogShowOrDismissListener的扩展方法
 */
inline fun DialogOptions.addShowDismissListener(
    key: String,
    dialogInterface: DialogShowOrDismissListener.() -> Unit
): DialogOptions {
    val dialogShowOrDismissListener = DialogShowOrDismissListener()
    dialogShowOrDismissListener.dialogInterface()
    showDismissMap[key] = dialogShowOrDismissListener
    return this
}

/**
 * 设置OnKeyListener的扩展方法
 */
inline fun DialogOptions.onKeyListenerForOptions(crossinline listener: (keyCode: Int, event: KeyEvent) -> Boolean) {
    val onKey = object : OnKeyListener() {
        override fun onKey(dialog: DialogInterface, keyCode: Int, event: KeyEvent): Boolean {
            return listener.invoke(keyCode, event)
        }
    }
    onKeyListener = onKey
}

/**
 * 针对特殊动画需要调用appdialog.dismiss()的方法
 */
inline fun AppDialog.onKeyListenerForDialog(crossinline listener: (appdialog: AppDialog, dialogInterFace: DialogInterface, keyCode: Int, event: KeyEvent) -> Boolean): AppDialog {
    val onKey = object : OnKeyListener() {
        override fun onKey(dialog: DialogInterface, keyCode: Int, event: KeyEvent): Boolean {
            return listener.invoke(this@onKeyListenerForDialog, dialog, keyCode, event)
        }
    }
    getDialogOptions().onKeyListener = onKey
    return this
}

