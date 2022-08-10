package com.hinacle.base.app

import android.app.Application
import com.hinacle.base.util.logcat.AndroidLogcatLogger
import com.hinacle.base.util.logcat.LogPriority
import kotlin.properties.Delegates

open class AppApplication() : Application() {

    companion object {
        // 最好使用AppUtil的 context
        private var instance: AppApplication by Delegates.notNull()
        fun instance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // 只在debug版本中打印日志，在release版本中无操作。 全自动无需手动管理 minPriority 默认打印级别
        AndroidLogcatLogger.installOnDebuggableApp(instance, minPriority = LogPriority.VERBOSE)
    }


}