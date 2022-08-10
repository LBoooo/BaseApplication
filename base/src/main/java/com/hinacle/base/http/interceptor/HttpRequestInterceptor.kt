package com.hinacle.base.http.interceptor

import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.request.BaseRequest
import com.hinacle.base.datastore.UserToken
import com.hinacle.base.datastore.queryUserInfo

/** 请求拦截器, 一般用于添加全局参数 */
class HttpRequestInterceptor : RequestInterceptor {

    /** 本方法每次请求发起都会调用, 这里添加的参数可以是动态参数 */
    override fun interceptor(request: BaseRequest) {
        request.addHeader("client", "Android")
        queryUserInfo <UserToken>()?.token?.let {
            request.setHeader("token", it)
        }
    }
}