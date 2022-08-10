package com.hinacle.base.widget.corner

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import androidx.annotation.Size

interface IRoundCorner {

    fun setHalfSizeRadius(halfSizeRadius: Boolean)

    fun setRadius(radius: Int)

    fun getRadius(): Int


    /**
     * top left, top right, bottom right, bottom let
     */
    @Size(4)
    fun getRealRadius(): IntArray

    fun setTopLeftRadius(radius: Int)

    fun getTopLeftRadius(): Int

    fun setTopRightRadius(radius: Int)

    fun getTopRightRadius(): Int

    fun setBottomRightRadius(radius: Int)

    fun getBottomRightRadius() :Int

    fun setBottomLeftRadius(radius: Int)

    fun getBottomLeftRadius():Int

    fun setStrokeColor(@ColorInt color: Int)

    fun setStrokeColors(colors:ColorStateList)

    fun setStrokeWidth(strokeWidth: Int)

    fun drawableStateChanged()

}