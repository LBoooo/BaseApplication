package com.hinacle.baseapplication.simple

import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.WindowMetrics
import by.kirich1409.viewbindingdelegate.viewBinding
import com.drake.net.utils.scope
import com.drake.net.utils.scopeDialog
import com.drake.statusbar.*
import com.hinacle.base.app.AppActivity
import com.hinacle.base.util.*
import com.hinacle.base.util.logcat.logcat
import com.hinacle.base.util.toast.toast
import com.hinacle.baseapplication.R
import com.hinacle.baseapplication.databinding.ActivityScreenBinding
import com.hinacle.baseapplication.main.MainActivity
import kotlinx.coroutines.delay


class ScreenActivity : AppActivity(R.layout.activity_screen) {
    private val viewBinding by viewBinding(ActivityScreenBinding::bind)

    override fun initView() {
        super.initView()


        val st = """尺寸1：${screenSize().first}--${screenSize().second}
                   尺寸2：${screenSize.first}--${screenSize.second}
                   屏幕DPI：${screenDPI}
                   屏幕密度：${screenDensity}
                   屏幕方向：${screenOrientation}"""
        viewBinding.screenWidth = st


    }


    fun setScreenPortrait(v: View) {
        if (isPortrait) toast { "已经是竖屏了" }
        setScreenPortrait()
    }

    fun setScreenLandscape(v: View) {
        if (isLandscape) toast { "已经是横屏了" }
        setScreenLandscape()
    }

    fun toggleScreenOrientation(v: View) {
        toggleScreenOrientation()
    }

    val isLock = false
    fun lockScreenOrientation(v: View) {
        if (isLock) unlockScreenOrientation() else lockScreenOrientation()
    }

    fun isFullScreen(v: View) {
        toast { if (isFullScreen) "是全屏" else "不是全屏" }
    }

    fun setFullScreen(v: View) {
        isFullScreen = true
    }

    fun statusBarColor(v: View) {
        scope {
            toast {
                statusBarColorRes(R.color.app_green_0)
                "设置绿色，2秒后设置蓝色"
            }
            delay(2000)
            toast {
                statusBarColor(getResColor(R.color.app_blue_1))
                "设置蓝色"
            }
        }
    }

    fun immersive(v: View) {
        scope {
            toast {
                immersive(v, darkMode = true)
                "使用按钮背景色设置状态栏颜色 -- 暗黑模式true"
            }
            delay(2000)
            toast {
                immersiveRes((R.color.app_blue_1), darkMode = false)
                "设置颜色状态栏 -- 暗黑模式false"
            }
        }

    }

    fun exitImmersive(v: View) {
        immersiveExit()
        toast { "更多扩展函数看代码" }
    }

}