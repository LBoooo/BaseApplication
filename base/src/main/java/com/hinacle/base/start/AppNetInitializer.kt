package com.hinacle.base.start

import android.content.Context
import androidx.startup.Initializer
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.drake.net.NetConfig
import com.drake.net.cookie.PersistentCookieJar
import com.drake.net.interceptor.LogRecordInterceptor
import com.drake.net.okhttp.*
import com.hinacle.appdialog.extensions.newAppDialog
import com.hinacle.base.BuildConfig
import com.hinacle.base.http.converter.SerializationConverter
import com.hinacle.base.http.error.ErrorHandler
import com.hinacle.base.http.interceptor.HttpRequestInterceptor
import com.hinacle.base.widget.dialog.loading.HttpLoadingDialog
import okhttp3.Cache
import java.util.concurrent.TimeUnit

class AppNetInitializer : Initializer<NetConfig> {
    override fun create(context: Context): NetConfig {
        return NetConfig.also {
            it.initialize(context = context) {
                addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG))
                // 超时设置
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
                // 本框架支持Http缓存协议和强制缓存模式
                // 缓存设置, 当超过maxSize最大值会根据最近最少使用算法清除缓存来限制缓存大小
                cache(Cache(context.cacheDir, 1024 * 1024 * 128))
                // LogCat是否输出异常日志, 异常日志可以快速定位网络请求错误
                setDebug(BuildConfig.DEBUG)
                // AndroidStudio OkHttp Profiler 插件输出网络日志
                addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG))
                // 添加持久化Cookie管理
                cookieJar(PersistentCookieJar(context))

                // 通知栏监听网络日志
                if (BuildConfig.DEBUG) {
                    addInterceptor(
                        ChuckerInterceptor.Builder(context)
                            .collector(ChuckerCollector(context))
                            .maxContentLength(250000L)
                            .redactHeaders(emptySet())
                            .alwaysReadResponseBody(false)
                            .build()
                    )
                }

                // 添加请求拦截器, 可配置全局/动态参数
                setRequestInterceptor(HttpRequestInterceptor())
                // 数据转换器
                setConverter(SerializationConverter())
                // 异常捕获
                setErrorHandler(ErrorHandler())
                // 自定义全局加载对话框 如不需要可以删除
                setDialogFactory {fa->
                    HttpLoadingDialog(fa)
                }
            }
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(AppUtilInitializer::class.java)
    }
}