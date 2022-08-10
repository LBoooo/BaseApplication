package com.hinacle.base.widget.toggle

import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout


internal fun ThemedButton.setMargin(leftMargin: Int? = null, topMargin: Int? = null,
                                    rightMargin: Int? = null, bottomMargin: Int? = null) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(
        leftMargin ?: params.leftMargin,
        topMargin ?: params.topMargin,
        rightMargin ?: params.rightMargin,
        bottomMargin ?: params.bottomMargin)
    layoutParams = params
}

/**
 * Makes the button scale down slightly.
 */
fun ThemedButton.bounceDown() {
    val animScaleDown = ScaleAnimation(1f, 0.9f, 1f, 0.9f, (width/2).toFloat(), (height/2).toFloat())
    animScaleDown.duration = 200
    animScaleDown.fillAfter = true
    animScaleDown.interpolator = DecelerateInterpolator()
    startAnimation(animScaleDown)
}

/**
 * Adds a touch listener that also triggers a cancel event if
 * action up is triggered outside the touched view
 */
internal fun View.setOnBoundedTouchListener(
    callback: (
        isActionDown: Boolean,
        isActionUp: Boolean,
        isActionCancel: Boolean,
        event: MotionEvent?
    ) -> Unit
) {
    var rect: Rect? = null
    var cancelled = false
    setOnTouchListener { v: View?, event: MotionEvent? ->
        performClick()
        val viewLeft = v?.left ?: 0
        val viewTop = v?.top ?: 0
        val viewright = v?.right ?: 0
        val viewBottom = v?.bottom ?: 0
        val eventX = event?.x?.toInt() ?: 0
        val eventY = event?.y?.toInt() ?: 0
        val action = event?.action
        when {
            action == MotionEvent.ACTION_DOWN -> {
                cancelled = false
                callback.invoke(true, false, false, event)
                rect = Rect(viewLeft, viewTop, viewright, viewBottom)
            }
            action == MotionEvent.ACTION_UP && !cancelled -> {
                if (rect?.contains(viewLeft + eventX, viewTop + eventY) == false) {
                    cancelled = true
                    callback.invoke(false, false, true, event)
                } else {
                    callback.invoke(false, true, false, event)
                }
            }
            action == MotionEvent.ACTION_MOVE -> {
                if (rect?.contains(viewLeft + eventX, viewTop + eventY) == false && !cancelled) {
                    cancelled = true
                    callback.invoke(false, false, true, event)
                }
            }
            action == MotionEvent.ACTION_CANCEL && !cancelled -> {
                cancelled = true
                callback.invoke(false, false, true, event)
            }
        }
        true
    }
}

/**
 * Makes the button scale up slightly (with overshoot).
 */
fun ThemedButton.bounceUp() {
    val animScaleUp = ScaleAnimation(0.9f, 1f, 0.9f, 1f, (width/2).toFloat(), (height/2).toFloat())
    animScaleUp.duration = 200
    animScaleUp.startOffset = 100
    animScaleUp.interpolator = OvershootInterpolator(3f)
    startAnimation(animScaleUp)
}

/** Utility function for setting the padding */
internal fun View.setViewPadding(
    left: Float? = null, top: Float? = null,
    right: Float? = null, bottom: Float? = null,
    horizontal: Float? = null, vertical: Float? = null,
    all: Float? = null
) {
    if (listOfNotNull(left, top, right, bottom, horizontal, vertical, all).any { it < 0f }) return
    all?.let { setPadding(it.toInt(), it.toInt(), it.toInt(), it.toInt()) }
    horizontal?.let { setPadding(it.toInt(), paddingTop, it.toInt(), paddingBottom) }
    vertical?.let { setPadding(paddingLeft, it.toInt(), paddingRight, it.toInt()) }
    setPadding(
        left?.toInt() ?: paddingLeft,
        top?.toInt() ?: paddingTop,
        right?.toInt() ?: paddingRight,
        bottom?.toInt() ?: paddingBottom
    )
}

internal var View.layoutGravity
    get() = (layoutParams as FrameLayout.LayoutParams).gravity
    set(value) {
        layoutParams = FrameLayout.LayoutParams(
            layoutParams.width,
            layoutParams.height,
            value
        )
    }

internal fun <K, V> MutableMap<K, V>.addIfAbsent(key: K, value: V): V? {
    var v: V? = get(key)
    if (v == null) v = put(key, value)
    return v
}

internal fun <T> MutableList<T>.enqueue(item: T) = if (!this.contains(item)) this.add(this.count(), item) else null

internal fun <T> MutableList<T>.dequeue(): T? = if (this.count() > 0) this.removeAt(0) else null

internal val View.name: String get() =
    if (this.id == -0x1) "no-id"
    else resources.getResourceEntryName(id) ?: "error-getting-name"

internal val ThemedButton.centerX: Float get() = (width / 2).toFloat()

internal val ThemedButton.centerY: Float get() = (height / 2).toFloat()
