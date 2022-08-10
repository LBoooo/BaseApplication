package com.hinacle.base.widget.toggle

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.ImageView
import androidx.core.content.ContextCompat

internal val lightGray: Int = Color.parseColor("#ebebeb")

internal val darkGray: Int = Color.parseColor("#5e5e5e")

internal val denim: Int = Color.parseColor("#5e6fed")

internal fun ImageView.setTintColor(
    color: Int,
    blendMode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN
) = this.setColorFilter(color, blendMode)

internal fun Context.color(id: Int): Int = ContextCompat.getColor(this, id)