package com.hinacle.base.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import java.io.File
import java.io.FileOutputStream

// 保存bitmap 为image
fun Bitmap.writeToFile(targetPath: String): String? {
    return try {
//            val parentPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
//            val targetFile = File(targetPath.replaceBeforeLast("/",parentPath))
        val targetFile = File(targetPath)
        if (!targetFile.exists()) {
            targetFile.parentFile?.apply {
                if (!exists()) {
                    mkdirs()
                }
            }
            if (!targetFile.exists()) {
                targetFile.createNewFile()
            }
        }
        val fos = FileOutputStream(targetPath)
        //通过io流的方式来压缩保存图片
        if (hasAlpha()) {
            compress(Bitmap.CompressFormat.PNG, 100, fos)
        } else {
            compress(Bitmap.CompressFormat.JPEG, 90, fos)
        }
        fos.flush()
        fos.close()
        if (targetFile.exists()) {
            targetPath
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


/**
 * 安全创建Bitmap，如果产生了OOM，可以主动GC后再尝试。
 * https://github.com/QMUI/QMUI_Android
 * @param width : Bitmap宽度
 * @param height : Bitmap高度度
 * @param retryCount : 重试次数，默认一次
 * @param config : Bitmap.Config，默认为ARGB_8888
 * @return 创建Bitmap成功返回Bitmap，否则返回null。
 */
fun createBitmapSafely(
    width: Int,
    height: Int,
    retryCount: Int = 1,
    config: Bitmap.Config = Bitmap.Config.ARGB_8888
): Bitmap? = try {
    Bitmap.createBitmap(width, height, config)
} catch (e: OutOfMemoryError) {
    e.printStackTrace()
    when (retryCount > 0) {
        true -> {
            System.gc()
            createBitmapSafely(width, height, retryCount, config)
        }
        false -> null
    }
}

/**
 * 将View转换为Bitmap
 * @param  scale: 生成的Bitmap相对于原View的大小比例，范围为0~1.0
 */
fun viewToBitmap(view: View, scale: Float = 1.0F): Bitmap? = when (view) {
    is ImageView -> {
        val drawable: Drawable = view.drawable
        (drawable as BitmapDrawable).bitmap
    }
    else -> {
        view.clearFocus()
        val bitmap =
            createBitmapSafely((view.width * scale).toInt(), (view.height * scale).toInt())
        if (bitmap != null) {
            val canvas = Canvas()
            synchronized(canvas) {
                with(canvas) {
                    setBitmap(bitmap)
                    save()
                    drawColor(Color.WHITE)
                    scale(scale, scale)
                    view.draw(canvas)
                    restore()
                    setBitmap(null)
                }
            }
        }
        bitmap
    }
}

// 5. Drawable----> Bitmap
fun drawableToBitmap(drawable: Drawable): Bitmap? {

    // 获取 drawable 长宽
    val width = drawable.intrinsicWidth
    val heigh = drawable.intrinsicHeight
    drawable.setBounds(0, 0, width, heigh)

    // 获取drawable的颜色格式
    val config =
        if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
    // 创建bitmap
    val bitmap = Bitmap.createBitmap(width, heigh, config)
    // 创建bitmap画布
    val canvas = Canvas(bitmap)
    // 将drawable 内容画到画布中
    drawable.draw(canvas)
    return bitmap
}
