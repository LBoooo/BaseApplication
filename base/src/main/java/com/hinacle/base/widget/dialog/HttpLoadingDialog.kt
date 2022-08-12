package com.hinacle.base.widget.dialog

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.RotateDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StyleRes
import com.drake.net.utils.runMain
import com.hinacle.base.R

/**
 * 网络加载loading 需要在app model内 新建dialog_loading（layout） 并定义loadingTv（textview）loadingIv（imageview）
 * 配合scopeDialog使用 如不需要可以删除 并在网络初始化时取消初始化
 * @see com.hinacle.base.start.AppNetInitializer
 */
class HttpLoadingDialog constructor(
    context: Context,
    @StyleRes themeResId: Int = R.style.DialogStyle,
) : Dialog(context, themeResId) {
    private var title: String = "加载中..."
    private lateinit var titleTv: TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        setContentView(view)

        titleTv = view.findViewById(R.id.loadingTv)
        val loadingIv = view.findViewById<ImageView>(R.id.loadingIv)
        val rotateDrawable = loadingIv.background as RotateDrawable
        ObjectAnimator.ofInt(rotateDrawable, "level", 0, 10000).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }
    }

    override fun show() {
        runMain {
            super.show()
        }
    }

    /**
     * 更新标题文本
     */
    fun updateTitle(text: String) {
        if (isShowing) {
            runMain {
                titleTv.text = text
            }
        } else {
            title = text
        }
    }

}