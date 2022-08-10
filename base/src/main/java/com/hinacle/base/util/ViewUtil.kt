package com.hinacle.base.util

import android.view.View
import java.util.concurrent.TimeUnit

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