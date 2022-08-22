package com.hinacle.base.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context


/**
 * 复制纯文本
 * @param text: 需要复制的文本
 * @param label: 用户可见的对复制数据的描述
 * @return 是否复制成功
 */
@Suppress("unUsed")
fun clipPlainText(text: CharSequence, label: CharSequence = ""): Boolean {
    val cm = AppUtil.application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(label, text)
    cm.setPrimaryClip(clipData)
    return cm.hasPrimaryClip()
}
