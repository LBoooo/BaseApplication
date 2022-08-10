package com.hinacle.base.widget.corner

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class RoundCornerConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), IRoundCorner {

    private val roundCornerHelper: RoundCornerHelper =
        RoundCornerHelper(this, context, attrs, defStyleAttr)

    override fun draw(canvas: Canvas) {
        roundCornerHelper.onPreDraw(canvas)
        super.draw(canvas)
        roundCornerHelper.onAfterDraw(canvas)
    }

    override fun invalidate() {
        roundCornerHelper.invalidate()
        super.invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        roundCornerHelper.invalidate()
    }

    override fun setHalfSizeRadius(halfSizeRadius: Boolean) {
        roundCornerHelper.setHalfSizeRadius(halfSizeRadius)
    }

    override fun setRadius(radius: Int) {
        roundCornerHelper.setRadius(radius)
    }

    override fun getRadius(): Int {
        return roundCornerHelper.getRadius()
    }

    override fun getRealRadius(): IntArray {
        return roundCornerHelper.getRealRadius()
    }

    override fun setTopLeftRadius(radius: Int) {
        roundCornerHelper.setTopLeftRadius(radius)
    }

    override fun getTopLeftRadius(): Int {
        return roundCornerHelper.getTopLeftRadius()
    }

    override fun setTopRightRadius(radius: Int) {
        roundCornerHelper.setTopRightRadius(radius)
    }

    override fun getTopRightRadius(): Int {
        return roundCornerHelper.getTopRightRadius()
    }

    override fun setBottomRightRadius(radius: Int) {
        roundCornerHelper.setBottomRightRadius(radius)
    }

    override fun getBottomRightRadius(): Int {
        return roundCornerHelper.getBottomRightRadius()
    }

    override fun setBottomLeftRadius(radius: Int) {
        roundCornerHelper.setBottomLeftRadius(radius)
    }

    override fun getBottomLeftRadius(): Int {
        return roundCornerHelper.getBottomLeftRadius()
    }

    override fun setStrokeColor(color: Int) {
        roundCornerHelper.setStrokeColor(color)
    }

    override fun setStrokeColors(colors: ColorStateList) {
        roundCornerHelper.setStrokeColors(colors)
    }

    override fun setStrokeWidth(strokeWidth: Int) {
        roundCornerHelper.setStrokeWidth(strokeWidth)
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        roundCornerHelper.drawableStateChanged()
    }

}