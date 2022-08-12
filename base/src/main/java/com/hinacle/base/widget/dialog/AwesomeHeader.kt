package com.hinacle.base.widget.dialog

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import androidx.core.view.ViewCompat
import androidx.databinding.ViewDataBinding
import com.hinacle.appdialog.AppDialog
import com.hinacle.base.util.logcat.logcat

//fun BottomSheetDialog.awesomeHeader(
//    isDark: Boolean = false,
//    roundRadius: Float = 0f,
//    color: Int = Color.WHITE
//) {
//    header(AwesomeHeaderBuilder()) {
//        round = roundRadius != 0f
//        bgRadius = roundRadius
//        bgColor = color
//        isDarkHeader = isDark
//        dialog = appDialog
//        this@awesomeHeader.onSideListener = {
//            onSideListener?.invoke(it)
//        }
//    }
//}

abstract class AwesomeHeaderBuilder<VB : ViewDataBinding>(
    private val bsDialog: BottomSheetDialog,
    private val isDark: Boolean = false,
    private val roundRadius: Float = 0f,
    private val color: Int = Color.WHITE
) : ContentBuilder<VB>() {
    private var dialog: AppDialog = bsDialog.appDialog
    private var round: Boolean = false
    private var isDarkHeader: Boolean = false
    private var bgColor: Int = Color.WHITE
    private var bgRadius: Float = 0f
    private var status = 0
    private var lastOff = 0f

    abstract val fillLayout :View
    abstract val roundAnimationView :View
    abstract val fillView:View

    override fun init() {
        with(bsDialog) {
            round = roundRadius != 0f
            bgRadius = roundRadius
            bgColor = color
            isDarkHeader = isDark
            onSideListener = { slideOffset ->
                if (slideOffset >= 0.99f && (lastOff < slideOffset) && status != 1) {
                    status = 1
                    onExpend()
                    fill()
                    setStatusBarColor()
                } else if (slideOffset < 0.99f && (lastOff > slideOffset) && status != 0) {
                    status = 0
                    onClose()
                    unFill()
                    setStatusBarColor()
                }
                lastOff = slideOffset
            }
        }

        fillLayout.layoutParams = fillLayout.layoutParams.also {
            it.height = 50
        }
        if (round) {
            ViewCompat.setBackground(roundAnimationView, buildRoundBgWithRadius(bgRadius))
        }

    }

    open fun onExpend(){}
    open fun onClose(){}

    override fun onShow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            dialog.dialog?.apply {
                val lp = window?.attributes
                lp?.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                window?.attributes = lp
            }
        }
    }

    private fun fill() {
        if (round) {
            heightAnimator?.cancel()
            startRoundAnimation(currentRound ?: bgRadius, 0f) {
                startHeightAnimation(fillLayout.height, 300)
            }
        } else {
            startHeightAnimation(fillLayout.height, 400)
        }
    }

    var currentRound: Float? = null

    private var roundAnimator: Animator? = null

    private val roundBg by lazy {
        GradientDrawable().apply {
            setColor(bgColor)
        }
    }

    private fun buildRoundBgWithRadius(radius: Float): Drawable {
        currentRound = radius
        return roundBg.apply {
            cornerRadii = floatArrayOf(bgRadius, radius, bgRadius, radius, 0f, 0f, 0f, 0f)
        }
    }

    //圆角动画
    private fun startRoundAnimation(from: Float, to: Float, onFinish: (() -> Unit)? = null) {
        roundAnimator?.cancel()
        roundAnimator = ValueAnimator.ofFloat(from, to).apply {
            duration = 150
            addUpdateListener {
                ViewCompat.setBackground(roundAnimationView, buildRoundBgWithRadius(it.animatedValue as Float))
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    roundAnimator = null
                    logcat { "RoundAnimation End: $from -> $to" }
                    onFinish?.invoke()
                }

                override fun onAnimationCancel(animation: Animator?) {
                    logcat { "RoundAnimation Cancel: $from -> $to" }
                }

                override fun onAnimationStart(animation: Animator?) {
                    logcat { ("RoundAnimation Start: $from -> $to") }
                }
            })
            start()
        }
    }

    private fun unFill() {
        roundAnimator?.cancel()
        startHeightAnimation(0, if (round) 300 else 400) {
            if (round) {
                startRoundAnimation(currentRound ?: 0f, bgRadius)
            }
        }
    }

    private var heightAnimator: Animator? = null

    private fun startHeightAnimation(to: Int, dur: Long, onFinish: (() -> Unit)? = null) {
        val begin = fillView.height
        heightAnimator?.cancel()
        heightAnimator = ValueAnimator.ofInt(begin, to).apply {
            duration = dur
            interpolator = DecelerateInterpolator()
            var canceled = false
            addUpdateListener {
                fillView.layoutParams = fillView.layoutParams.also { g ->
                    g.height = it.animatedValue as Int
                }
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    logcat { "HeightAnimation End  $begin -> $to" }
                    if (!canceled) {
                        heightAnimator = null
                        onFinish?.invoke()
                    }
                }

                override fun onAnimationCancel(animation: Animator) {
                    logcat { "HeightAnimation Cancel  $begin -> $to" }
                    canceled = true
                }

                override fun onAnimationStart(animation: Animator) {
                    logcat { "HeightAnimation Start  $begin -> $to" }
                }
            })
            start()
        }
    }

    /**
     * 改变状态栏主题
     * 暂不支持6.0以下系统
     */
    /**
     * 改变状态栏主题
     * 暂不支持6.0以下系统
     */
    @Suppress("DEPRECATION")
    private fun setStatusBarColor() {
        if (isDarkHeader) return
        val navFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR else 0
        if (status == 0) {
            dialog.dialog?.window?.decorView?.systemUiVisibility =
                SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or navFlag
        } else {
            dialog.dialog?.window?.decorView?.systemUiVisibility =
                SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or navFlag
        }
    }

    override fun updateContent(type: Int, data: Any?) {

    }
}