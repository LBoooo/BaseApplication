package com.hinacle.base.widget.corner

import android.content.res.ColorStateList
import android.graphics.Color

data class RoundCornerAttrs(
    var halfSizeRadius: Boolean = false,
    var radius: Int = 0,
    var topLeftRadius: Int = 0,
    var topRightRadius: Int = 0,
    var bottomRightRadius: Int = 0,
    var bottomLeftRadius: Int = 0,
    var strokeColorStateList: ColorStateList = ColorStateList.valueOf(Color.TRANSPARENT),
    var strokeWidth: Int = 0,
)