package com.hinacle.baseapplication.simple

import android.view.View
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.drake.net.utils.scope
import com.drake.net.utils.withIO
import com.hinacle.base.app.AppActivity
import com.hinacle.base.util.logcat.LogPriority
import com.hinacle.base.util.logcat.logcat
import com.hinacle.base.util.onShakeClickListener
import com.hinacle.base.util.toast.toast
import com.hinacle.base.widget.dialog.loading.showLoading
import com.hinacle.baseapplication.R
import com.hinacle.baseapplication.databinding.ActivityLogcatBinding
import kotlin.concurrent.thread

class LogcatActivity : AppActivity(R.layout.activity_logcat) {
    val binding by viewBinding(ActivityLogcatBinding::bind)
    override fun initView() {
        // 默认tag是引用者 eg： logcat{} == Any.logcat{} tag 即为any
        with(binding) {
            printBtn.onShakeClickListener {
                logcat { "----------->>普通打印" }
            }
            printEBtn.onShakeClickListener {
                logcat(LogPriority.ERROR) { "----------->>打印error级别" }
            }
            printTBtn.onShakeClickListener {
                logcat(tag = "printWithTag") { "----------->>带TAG打印" }
            }


        }
    }

    fun toastNormal(v: View) {
        showLoading()
        toast { "普通吐司提示" }
    }

    fun toastLongTime(v: View) {
        toast(duration = Toast.LENGTH_LONG) { "长时间吐司提示" }
    }

    fun threadToast(v: View) {
        scope {
            withIO {
                toast { "协程的吐司" }
            }
        }
    }
    fun threadToast1(v: View) {
        thread {
            toast { "子线程的吐司" }
        }
    }
}