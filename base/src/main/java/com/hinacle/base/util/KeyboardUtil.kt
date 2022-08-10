package com.hinacle.base.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * 打开软键盘
 */
fun View.showKeyboard(): Boolean {
    val imm = context
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    requestFocus()
    return imm.showSoftInput(this, InputMethodManager.RESULT_UNCHANGED_SHOWN)
}

/**
 * 关闭软键盘
 */
fun View.hideKeyboard(): Boolean {
    val imm = context
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return imm.hideSoftInputFromWindow(this.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
}


