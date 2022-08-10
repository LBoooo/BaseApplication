package com.hinacle.base.util.toast

import android.view.Gravity
import android.widget.Toast
import com.drake.net.utils.scope
import com.hinacle.base.util.AppUtil

private var toast: Toast? = null

fun toast(
    gravity: Int = Gravity.CENTER_HORIZONTAL and Gravity.BOTTOM,
    duration: Int = Toast.LENGTH_SHORT,
    message: () -> String
) {
    scope {
        toast = if (toast == null) {
            Toast.makeText(AppUtil.application, message.invoke(), duration)
        } else {
            toast?.cancel()
            toast = null
            Toast.makeText(AppUtil.application, message.invoke(), duration)
        }
        toast?.setGravity(gravity, 0, 0)
        toast?.show()
    }
}

















