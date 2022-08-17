package com.hinacle.base.widget.dialog

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.FragmentManager
import com.hinacle.appdialog.AppDialog
import com.hinacle.appdialog.extensions.bindingListenerFun
import com.hinacle.appdialog.extensions.newAppDialog
import com.hinacle.appdialog.other.DialogGravity
import com.hinacle.appdialog.R
import com.hinacle.base.databinding.WidgetDialogPermissionsRationaleBinding
import com.hinacle.base.util.px

/**
 * 申请权限需要的说明弹窗
 * eg: 申请定位权限 title = 定位权限使用说明 depict = 申请位置权限是为了向您提供基于您的位置信息附近的优质内容
 * 可能需要延迟一秒弹出 以实现同步效果
 */
class PermissionsRationaleDialog(
    private val context: Context,
    val title: String, val depict: String
) {
    private lateinit var dialog: AppDialog

    init {
        initDialog()
    }

    private fun initDialog() {
        dialog = newAppDialog {
            layoutId = com.hinacle.base.R.layout.widget_dialog_permissions_rationale
            isFullHorizontal = true
            fullHorizontalMargin = 15.px
            bindingListenerFun(
                "",
                WidgetDialogPermissionsRationaleBinding::class
            ) { binding, _ ->
                binding.msg = PermissionsExplain(title, depict)
            }
        }
    }

    fun show(fm: FragmentManager) {
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                dialog.showOnWindow(
                    fm,
                    DialogGravity.CENTER_TOP,
                    R.style.TopTransAlphaDeceAnimation
                )
            }, 1000)
        }


    }

    fun dismiss() {
        if (dialog.isVisible)
            dialog.dismiss()
    }

    class PermissionsExplain(val title: String, var depict: String)
}