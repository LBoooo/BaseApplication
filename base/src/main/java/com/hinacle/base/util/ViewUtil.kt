package com.hinacle.base.util

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
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