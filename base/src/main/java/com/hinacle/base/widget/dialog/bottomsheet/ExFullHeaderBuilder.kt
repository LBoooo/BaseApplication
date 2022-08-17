package com.hinacle.base.widget.dialog.bottomsheet

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.databinding.ViewDataBinding
import com.drake.statusbar.statusBarHeight
import com.hinacle.appdialog.AppDialog
import com.hinacle.base.R
import com.hinacle.base.util.logcat.logcat

/**
 * 展开后扩展成全屏的Header
 */
abstract class ExFullHeaderBuilder<VB : ViewDataBinding>(
    private val bsDialog: BottomSheetDialog,
    private val isNeedDark: Boolean = false,
    private val titleRoundRadius: Float = 0f,
    private val titleColor: Int = Color.WHITE
) : ContentBuilder<VB>() {
    private val duration = 400L
    private val roundedDuration = 100L
    private var status = 0
    private var dialog: AppDialog = bsDialog.appDialog
    private var isNeedDarkHeader: Boolean = true
    private var round: Boolean = false
    private var bgColor: Int = Color.WHITE
    private var bgRadius: Float = 0f
    private var lastOff = 0f
    private var currentRound: Float? = null
    private var roundAnimator: Animator? = null
    private var heightAnimator: Animator? = null
    private val roundBg by lazy {
        GradientDrawable().apply {
            setColor(bgColor)
        }
    }
    private val spaceView by lazy {
        View(bsDialog.appDialog.context).also {
            it.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, 0)
            it.setBackgroundColor(bgColor)
            it.fitsSystemWindows = true
        }
    }
    private val fakeStatusBarView by lazy {
        LinearLayout(bsDialog.appDialog.context).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, context.statusBarHeight)
            gravity = Gravity.BOTTOM
            addView(spaceView)
            setBackgroundResource(R.color.app_transparent)
        }
    }
    private val titleLayout by lazy {
        val layout = viewBinding.root
        if (layout !is LinearLayout) {
            throw ExceptionInInitializerError("标题栏需要是LinearLayout")
        }
        layout
    }

    override fun init() {
        if (titleLayout.childCount < 1) {
            throw ExceptionInInitializerError("标题栏至少需要一个child")
        }
        with(bsDialog) {
            round = titleRoundRadius != 0f
            bgRadius = titleRoundRadius
            bgColor = titleColor
            isNeedDarkHeader = isNeedDark
            onSideListener = { slideOffset ->
                if (slideOffset >= 0.99f && (lastOff < slideOffset) && status != 1) {
                    status = 1
                    this@ExFullHeaderBuilder.onExpanded()
                    fill()
                    setStatusBarColor()
                } else if (slideOffset < 0.99f && (lastOff > slideOffset) && status != 0) {
                    status = 0
                    this@ExFullHeaderBuilder.onCollapsed()
                    unFill()
                    setStatusBarColor()
                }
                lastOff = slideOffset
            }
        }
        titleLayout.addView(fakeStatusBarView, 0)
        if (round) {
            ViewCompat.setBackground(titleLayout.getChildAt(1), buildRoundBgWithRadius(bgRadius))
        }
    }

    open fun onExpanded() {}
    open fun onCollapsed() {}

//    override fun onShow() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            dialog.dialog?.apply {
//                val lp = window?.attributes
//                lp?.layoutInDisplayCutoutMode =
//                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
//                window?.attributes = lp
//            }
//        }
//    }

    private fun fill() {
        if (round) {
            heightAnimator?.cancel()
            startRoundAnimation(currentRound ?: bgRadius, 0f) {
                startHeightAnimation(fakeStatusBarView.height, duration - roundedDuration)
            }
        } else {
            startHeightAnimation(fakeStatusBarView.height, duration)
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
            duration = roundedDuration
            addUpdateListener {
                ViewCompat.setBackground(
                    titleLayout.getChildAt(1),
                    buildRoundBgWithRadius(it.animatedValue as Float)
                )
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    roundAnimator = null
                    logcat { "RoundAnimation End: $from -> $to" }
                    onFinish?.invoke()
                }

                override fun onAnimationCancel(animation: Animator) {
                    logcat { "RoundAnimation Cancel: $from -> $to" }
                }

                override fun onAnimationStart(animation: Animator) {
                    logcat { ("RoundAnimation Start: $from -> $to") }
                }
            })
            start()
        }
    }

    private fun unFill() {
        roundAnimator?.cancel()
        startHeightAnimation(0, if (round) duration - roundedDuration else duration) {
            if (round) {
                startRoundAnimation(currentRound ?: 0f, bgRadius)
            }
        }
    }

    private fun startHeightAnimation(to: Int, dur: Long, onFinish: (() -> Unit)? = null) {
        val begin = spaceView.height
        heightAnimator?.cancel()
        heightAnimator = ValueAnimator.ofInt(begin, to).apply {
            duration = dur
            interpolator = DecelerateInterpolator()
            var canceled = false
            addUpdateListener {
                spaceView.layoutParams = spaceView.layoutParams.also { g ->
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
    @Suppress("DEPRECATION")
    private fun setStatusBarColor() {
//        if (isNeedDarkHeader) return
//        val navFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR else 0
//        if (status == 0) {
//            dialog.dialog?.window?.decorView?.systemUiVisibility =
//                SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or navFlag
//        } else {
//            dialog.dialog?.window?.decorView?.systemUiVisibility =
//                SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or navFlag
//        }

//        dialog.dialog?.window?.apply {
//
//            statusBarColor = getResColor(R.color.app_translucent)


//            val wic = WindowInsetsControllerCompat(this, decorView)
//            wic.isAppearanceLightStatusBars = true // true or false as desired.
//            // And then you can set any background color to the status bar.
//            // And then you can set any background color to the status bar.
//            statusBarColor = getResColor(R.color.app_transparent)


//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                val wic = decorView.windowInsetsController
//                wic!!.setSystemBarsAppearance(
//                    APPEARANCE_LIGHT_STATUS_BARS,
//                    APPEARANCE_LIGHT_STATUS_BARS
//                )
//            } else
//                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//
//             setStatusBarColor(Color.WHITE)
//        }

//        dialog.dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//        dialog.dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        if (isNeedDarkHeader) {
//            dialog.dialog?.window?.decorView?.systemUiVisibility =
//                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        } else {
//            dialog.dialog?.window?.decorView?.systemUiVisibility =
//                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        }

        // todo 根据需求修改状态栏字体颜色 DialogFragment 目前无法修改
    }

    override fun updateContent(type: Int, data: Any?) {

    }
}