package com.hinacle.base.util.toast

import android.widget.Toast
import com.drake.net.utils.scope
import com.hinacle.base.util.AppUtil

private var toast: Toast? = null

fun toast(
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
        toast?.show()
    }
}

















