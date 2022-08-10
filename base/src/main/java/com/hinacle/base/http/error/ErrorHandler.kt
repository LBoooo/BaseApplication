package com.hinacle.base.http.error

import com.drake.net.exception.ResponseException
import com.drake.net.interfaces.NetErrorHandler

class ErrorHandler : NetErrorHandler {
    override fun onError(e: Throwable) {

        // .... 其他错误
        if (e is ResponseException && e.tag == 1) { // 判断异常为token失效
            // 打开登录界面或者弹登录失效对话框


        }
    }
}