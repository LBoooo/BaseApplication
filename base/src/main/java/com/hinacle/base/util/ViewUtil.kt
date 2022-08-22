package com.hinacle.base.util

import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.TextView
import java.util.concurrent.TimeUnit

/**
 * 防止重复点击
 */
fun <T : View> T.onShakeClickListener(
    interval: Long = 300,
    unit: TimeUnit = TimeUnit.MILLISECONDS,
    block: T.() -> Unit
) {
    setOnClickListener(OnShakeClickListener(interval, unit, block))
}

@Suppress("UNCHECKED_CAST")
internal class OnShakeClickListener<T : View>(
    private val interval: Long = 300,
    private val unit: TimeUnit = TimeUnit.MILLISECONDS,
    private var block: T.() -> Unit
) : View.OnClickListener {
    private var lastTime: Long = 0
    override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > unit.toMillis(interval)) {
            lastTime = currentTime
            block(v as T)
        }
    }
}

// 缩放动画 点击效果
@Suppress("unUsed")
fun View.scaleClickView(startScale: Float = 0.95f, endScale: Float = 1f) {
    val animation = ScaleAnimation(
        startScale, endScale,
        startScale, endScale,
        Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f
    )
    animation.fillAfter = true
    animation.duration = 300L
    startAnimation(animation)
}

@Suppress("unUsed")
fun View.fadeOut(duration: Long = 800, endStatus: Int = View.GONE) {
    visibility = endStatus
    startAnimation(AlphaAnimation(1f, 0f).apply {
        setDuration(duration)
    })
}

@Suppress("unUsed")
fun View.fadeIn(duration: Long = 500) {
    visibility = View.VISIBLE
    startAnimation(AlphaAnimation(0f, 1f).apply {
        setDuration(duration)
    })
}


/**
 * 当前View是否可见
 */
@Suppress("unUsed")
inline val View.isVisible get() = visibility == View.VISIBLE

/**
 * 当前View是否不可见
 */
@Suppress("unUsed")
inline val View.isInvisible get() = visibility == View.INVISIBLE

/**
 * 当前View是否隐藏
 */
@Suppress("unUsed")
val View.isGone get() = visibility == View.GONE

/**
 * 将View设置为隐藏
 */
@Suppress("unUsed")
fun View.setGone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

/**
 * 将View设置为可见
 */
@Suppress("unUsed")
fun View.setVisible() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

/**
 * 将View设置为不可见
 */
@Suppress("unUsed")
fun View.setInvisible() {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
}

/**
 * 设置View的宽度
 * @param width: 宽度值，单位为px
 */
@Suppress("unUsed")
fun View.setWidth(width: Int) {
    layoutParams = layoutParams.apply {
        this.width = width
    }
}

/**
 * 设置View的高度
 * @param height: 高度值，单位为px
 */
@Suppress("unUsed")
fun View.setHeight(height: Int) {
    layoutParams = layoutParams.apply {
        this.height = height
    }
}

/**
 * 设置View的宽度和高度
 * @param width: 宽度值，单位为px
 * @param height: 高度值，单位为px
 */
@Suppress("unUsed")
fun View.setWidthAndHeight(width: Int, height: Int) {
    layoutParams = layoutParams.apply {
        this.width = width
        this.height = height
    }
}

/**
 * 将View转换为Bitmap
 * @param  scale: 生成的Bitmap相对于原View的大小比例，范围为0~1.0
 */
@Suppress("unUsed")
fun View.toBitmap(scale: Float = 1.0F): Bitmap? = viewToBitmap(this, scale)

/**
 * 设置View的padding
 */
@Suppress("unUsed")
fun View.setNewPadding(
    left: Int = paddingLeft,
    top: Int = paddingTop,
    right: Int = paddingRight,
    bottom: Int = paddingBottom
) {
    setPadding(left, top, right, bottom)
}

/**
 * 测量View
 */
private fun View.measureView() {
    var params = layoutParams
    if (params == null) {
        params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
    val widthSpec = ViewGroup.getChildMeasureSpec(0, 0, params.width)
    val heightSpec = if (params.height > 0) {
        View.MeasureSpec.makeMeasureSpec(params.height, View.MeasureSpec.EXACTLY)
    } else {
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    }
    measure(widthSpec, heightSpec)
}

/**
 * 获取View的高度
 * 如果是“math_parent”属性则无法获取，值为0。
 */
@Suppress("unUsed")
val View.viewHeight: Int
    get() {
        measureView()
        return measuredHeight
    }

/**
 * 获取View的宽度
 * 如果是“math_parent”属性则无法获取，值为0。
 */
@Suppress("unUsed")
val View.viewWidth: Int
    get() {
        measureView()
        return measuredWidth
    }

/**
 * 获取TextView的String内容
 */
@Suppress("unUsed")
inline val TextView.textString: String get() = text.toString()

/**
 * 获取TextView的String内容长度
 */
@Suppress("unUsed")
inline val TextView.textLength: Int get() = text.length

/**
 * 判断TextView的内容是否为空
 */
@Suppress("unUsed")
inline val TextView.isTextEmpty: Boolean get() = text.isEmpty()

/**
 * 判断TextView的内容是否为null或空
 */
@Suppress("unUsed")
inline val TextView.isTextNullOrEmpty: Boolean get() = text.isNullOrEmpty()

/**
 * 判断TextView的内容是否为非空
 */
@Suppress("unUsed")
val TextView.isTextNotEmpty: Boolean get() = !isTextEmpty

/**
 * 判断TextView的内容是否为空白
 */
@Suppress("unUsed")
val TextView.isTextBlank: Boolean get() = text.isBlank()

/**
 * 判断TextView的内容是否为null或空白
 */
@Suppress("unUsed")
val TextView.isTextNullOrBlank: Boolean get() = text.isNullOrBlank()

/**
 * 判断TextView的内容是否为非空白
 */
@Suppress("unUsed")
val TextView.isTextNotBlank: Boolean get() = text.isNotBlank()
