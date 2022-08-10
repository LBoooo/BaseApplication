package com.hinacle.base.widget.album

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.ImageLoader
import coil.request.ImageRequest
import com.hinacle.base.R
import com.hinacle.base.app.AppActivity
import com.hinacle.base.app.AppConstant
import com.hinacle.base.databinding.WidgetAlbumClipBinding
import com.hinacle.base.util.currentTimeMillis
import com.hinacle.base.util.logcat.logcat
import com.hinacle.base.util.md5
import com.hinacle.base.util.toast.toast
import com.hinacle.base.util.writeToFile

class ClipActivity : AppActivity(R.layout.widget_album_clip) {
    private val viewBinding by viewBinding(WidgetAlbumClipBinding::bind)
    private var originalPath = ""
    override fun superOnCreate() {
        intent?.takeIf {
            it.hasExtra(AppConstant.clipPath)
        }?.apply {
            originalPath = getStringExtra(AppConstant.clipPath) ?: ""
        }
    }

    override fun initView() {
        if (originalPath.isEmpty()) {
            toast { "图片有误" }
            return
        }
        load(originalPath)
        applySettings()
    }

    private fun load(path: String) {
        val request = ImageRequest.Builder(this)
            .data(path)
            .allowHardware(false)
            .allowConversionToBitmap(true)
            .allowRgb565(true)
            .target(onSuccess = { result ->
                viewBinding.kropView.setBitmap((result as BitmapDrawable).bitmap)
                viewBinding.kropView.setZoom(1.0f)
            })
            .build()
        ImageLoader.Builder(this).build().enqueue(request)
    }

    private fun applySettings() {
        try {
            val offset = 0
            val aspectX = 1
            val aspectY = 1

            viewBinding.kropView.apply {
                applyAspectRatio(aspectX, aspectY)
                applyOffset(offset)
            }
        } catch (ignored: Throwable) {

        }
    }

    override fun onTitleButtonClick(v: View) {
        val name = "${cacheDir.absolutePath}/clip/${md5((currentTimeMillis).toString())}.jpg"
        val path = getResult()?.writeToFile(name)
        if (path.isNullOrEmpty()) logcat { "图片保存失败" }
        path?.apply {
            setResult(RESULT_OK, Intent().apply {
                putExtra("PATH", path)
            })
            ClipListener.clipListener?.invoke(path)
        }
        finish()
    }

    private fun getResult(): Bitmap? = viewBinding.kropView.getCroppedBitmap()
}