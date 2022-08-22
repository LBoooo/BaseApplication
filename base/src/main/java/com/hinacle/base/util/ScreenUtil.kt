package com.hinacle.base.util

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.annotation.RequiresPermission


// 真实的屏幕尺寸 todo 待完善 api30以下
fun Activity.screenSize(): Pair<Int, Int> {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val wm = this.windowManager.currentWindowMetrics
        val rect = wm.bounds
        val width = rect.right
        val height = rect.bottom
        return Pair(width, height)
    } else {
        return Pair(0, 0)
    }
}

/**
 * 获取屏幕宽高 不包含状态栏
 */
val screenSize
    get() = Pair(
        AppUtil.application.resources.displayMetrics.widthPixels,
        AppUtil.application.resources.displayMetrics.heightPixels
    )

/**
 * 获取屏幕密度
 */
//val screenDensity
//    get() = Resources.getSystem().displayMetrics.density
inline val screenDensity get() = AppUtil.application.resources.displayMetrics.density
/**
 * 获取屏幕DPI
 */
val screenDPI
    get() = AppUtil.application.resources.displayMetrics.densityDpi

/**
 * 获取屏幕方向
 **/
val screenOrientation
    get() = AppUtil.application.resources.configuration.orientation

/**
 * 设置横屏
 * 横屏有两个方向，在某个横屏方向下再次设置横屏会选择180°，故需要先判断当前是否已经横屏。
 */
fun Activity.setScreenLandscape() {
    if (isPortrait) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}

/**
 * 设置竖屏
 */
fun Activity.setScreenPortrait() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}

/**
 * 是否是横屏
 */
val isLandscape: Boolean
    get() = screenOrientation == Configuration.ORIENTATION_LANDSCAPE


/**是否是竖屏**/
val isPortrait: Boolean
    get() = screenOrientation == Configuration.ORIENTATION_PORTRAIT


/**
 * 横竖屏切换
 */
fun Activity.toggleScreenOrientation() {
    requestedOrientation = if (isLandscape) {
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    } else {
        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}

/**
 * 锁定屏幕方向
 */
fun Activity.lockScreenOrientation() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
}

/**
 * 取消锁定屏幕方向
 */
fun Activity.unlockScreenOrientation() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}


/**
 * 判断和设置是否全屏，赋值为true设置成全屏
 */
@Suppress("DEPRECATION")
var Activity.isFullScreen: Boolean
    get() {
        val flag = FLAG_FULLSCREEN
        return (window.attributes.flags and flag) == flag
    }
    set(value) {
        if (value) {
            window.addFlags(FLAG_FULLSCREEN)
        } else {
            window.clearFlags(FLAG_FULLSCREEN)
        }
    }

/**
 * 设置全屏
 */
@Suppress("unUsed")
fun Activity.setFullScreen() {
    if (!isFullScreen) {
        isFullScreen = true
    }
}

/**
 * 设置非全屏
 */
@Suppress("unUsed")
fun Activity.setNonFullScreen() {
    if (isFullScreen) {
        isFullScreen = false
    }
}

/**
 * 屏幕是否亮屏
 */
@Suppress("unUsed")
val isScreenOn: Boolean
    get() {
        val powerManager =
            AppUtil.application.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isInteractive
    }

/**
 * 屏幕是否熄灭
 */
@Suppress("unUsed")
val isScreenOff get() = !isScreenOn

/**
 * 屏幕是否锁屏
 */
@Suppress("unUsed")
val isScreenLocked
    get() = (AppUtil.application.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager).isKeyguardLocked

/**
 * 屏幕是否解锁
 */
@Suppress("unUsed")
val isScreenUnlocked get() = !isScreenLocked

/**
 * 判断和设置是否保持屏幕常亮，只作用于当前窗口
 */
@Suppress("unUsed")
var Activity.isKeepScreenOn: Boolean
    get() {
        val flag = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        return (window.attributes.flags and flag) == flag
    }
    set(value) {
        when (value) {
            true -> window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            false -> window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

/**
 * 保持屏幕常亮，只作用于当前窗口
 */
@Suppress("unUsed")
fun Activity.setKeepScreenOn() {
    if (!isKeepScreenOn) {
        isKeepScreenOn = true
    }
}

/**
 * 取消保持屏幕常亮，只作用于当前窗口
 */
@Suppress("unUsed")
fun Activity.setNonKeepScreenOn() {
    if (isKeepScreenOn) {
        isKeepScreenOn = false
    }
}

/**
 * 获取自动锁屏时间
 * @throws Settings.SettingNotFoundException
 */
@Suppress("unUsed")
fun getScreenAutoLockTime() = try {
    Settings.System.getInt(AppUtil.application.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT)
} catch (e: Settings.SettingNotFoundException) {
    e.printStackTrace()
    -1
}

/**
 * 设置自动锁屏时间
 * @return 设置成功返回true
 */
@Suppress("unUsed")
@RequiresPermission(android.Manifest.permission.WRITE_SETTINGS)
fun setScreenAutoLockTime(time: Int): Boolean =
    Settings.System.putInt(
        AppUtil.application.contentResolver,
        Settings.System.SCREEN_OFF_TIMEOUT,
        time
    )

/**
 * 设置永不自动锁屏，即自动锁屏时间为Int.MAX_VALUE
 * @return 设置成功返回true
 */
@Suppress("unUsed")
@RequiresPermission(android.Manifest.permission.WRITE_SETTINGS)
fun setScreenAutoLockNever(): Boolean = setScreenAutoLockTime(Int.MAX_VALUE)




