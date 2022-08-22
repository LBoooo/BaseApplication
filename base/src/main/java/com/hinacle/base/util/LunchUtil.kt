package com.hinacle.base.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


/**
 * 从Activity启动Activity 不携带参数 推荐携带参数方法
 */
inline fun <reified T : Activity> Activity.onStart() {
    val mIntent = Intent(this, T::class.java)
    startActivity(mIntent)
}

/**
 * 从Activity启动Activity 携带参数
 */
inline fun <reified T : Activity> Activity.onStart(block: Intent.() -> Unit) {
    val mIntent = Intent(this, T::class.java)
    mIntent.block()
    startActivity(mIntent)
}

/**
 * 从Context启动Activity 不携带参数 推荐携带参数方法
 */
inline fun <reified T : Activity> Context.onStart() {
    val mIntent = Intent(this, T::class.java)
    mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(mIntent)
}

/**
 * 从Context启动Activity 携带参数
 */
inline fun <reified T : Activity> Context.onStart(block: Intent.() -> Unit) {
    val mIntent = Intent(this, T::class.java)
    mIntent.block()
    mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(mIntent)
}

/**
 * 带返回值的launch
 */
inline fun Fragment.onStartForResultLaunch(crossinline resultListener: (code: Int, intent: Intent?) -> Unit): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        result?.let {
            resultListener.invoke(result.resultCode, result.data)
        }
    }
}

/**
 * 带返回值的launch
 */
inline fun AppCompatActivity.onStartForResultLaunch(crossinline resultListener: (code: Int, intent: Intent?) -> Unit): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        result?.let {
            resultListener.invoke(result.resultCode, result.data)
        }
    }
}

/**
 * 使用launch启动Activity 不携带参数
 */
inline fun <reified T : Activity> ActivityResultLauncher<Intent>.launch(
    activity: Context
) {
    this.launch(Intent(activity, T::class.java))
}

/**
 * 使用launch启动Activity 携带参数
 */
inline fun <reified T : Activity> ActivityResultLauncher<Intent>.launch(
    activity: Context,
    block: Intent.() -> Unit
) {
    this.launch(Intent(activity, T::class.java).apply {
        this.block()
    })
}

