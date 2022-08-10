package com.hinacle.base.widget.toggle

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.widget.FrameLayout

class RoundedCornerLayout(context: Context) : FrameLayout(context) {

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    var cornerRadius = 0f
    var borderWidth = 0f
    var borderColor = darkGray

    override fun draw(canvas: Canvas) {
        val halfBorderWidth = borderWidth / 2
        val count = canvas.save()
        val path = Path()
        if (borderWidth > 0f) {
            paint.style = Paint.Style.STROKE
            paint.color = borderColor
            paint.strokeWidth = borderWidth
        } else {
            // this prevents the border from removing anti-aliassing
            paint.color = (background as ColorDrawable).color
        }
        path.addRoundRect(
            RectF(
                halfBorderWidth,
                halfBorderWidth,
                width.toFloat() - halfBorderWidth,
                height.toFloat() - halfBorderWidth
            ),
            cornerRadius,
            cornerRadius,
            Path.Direction.CW
        )
        canvas.drawPath(path, paint)
        canvas.clipPath(path)
        super.draw(canvas)
        canvas.restoreToCount(count)
    }
}