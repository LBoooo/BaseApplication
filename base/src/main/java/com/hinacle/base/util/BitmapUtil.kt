package com.hinacle.base.util

import android.graphics.Bitmap
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