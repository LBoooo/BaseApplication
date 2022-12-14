package com.hinacle.base.util

import com.hinacle.base.util.logcat.logcat
import okhttp3.internal.and
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**获取当前年份**/
@Suppress("unUsed")
inline val currentYear: Int get() = Calendar.getInstance().get(Calendar.YEAR)

/**获取当前月份**/
@Suppress("unUsed")
inline val currentMonth get() = Calendar.getInstance().get(Calendar.MONTH) + 1

/**获取当前日**/
@Suppress("unUsed")
inline val currentDay: Int get() = Calendar.getInstance().get(Calendar.DATE)

/**获取当前时间戳**/
inline val currentTimeMillis: Long get() = System.currentTimeMillis()

/**
 * md5 加密当前时间戳
 */
@Suppress("unUsed")
fun String.md5(): String {
    if (isEmpty()) return ""
    val md5: MessageDigest
    try {
        md5 = MessageDigest.getInstance("MD5")
        val bytes: ByteArray = md5.digest(toByteArray())
        var result = ""
        for (b in bytes) {
            var temp = Integer.toHexString(b and 0xff)
            if (temp.length == 1) {
                temp = "0$temp"
            }
            result += temp
        }
        return result
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        logcat(tag = "MD5") { "MD5加密失败" }
    }
    return ""
}