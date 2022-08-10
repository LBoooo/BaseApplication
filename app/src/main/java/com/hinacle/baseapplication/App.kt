package com.hinacle.baseapplication

import com.drake.net.NetConfig
import com.hinacle.base.app.AppApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : AppApplication() {

    override fun onCreate() {
        super.onCreate()
        // 配置全局的请求url
        NetConfig.host = BuildConfig.URL


    }
}