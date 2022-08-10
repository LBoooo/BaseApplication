package com.hinacle.base.util

import androidx.annotation.*
import androidx.core.content.ContextCompat
 
/**
 * 获取颜色
 */
fun getResColor(@ColorRes colorRes: Int) = ContextCompat.getColor(AppUtil.application, colorRes)

/**
 * 获取图片资源
 */
fun getResDrawable(@DrawableRes drawableRes: Int) = ContextCompat.getDrawable(AppUtil.application, drawableRes)

/**
 * 获取字符资源
 */
fun getResString(@StringRes stringId: Int, vararg formatArgs: Any) = AppUtil.application.getString(stringId, *formatArgs)

/**
 * 获取String数组
 */
fun getResStringArray(@ArrayRes arrayId: Int): Array<String> = AppUtil.application.resources.getStringArray(arrayId)

/**
 * 获取Int数组
 */
fun getResIntArray(@ArrayRes arrayId: Int) = AppUtil.application.resources.getIntArray(arrayId)

/**
 * 获取Char数组
 */
fun getResTextArray(@ArrayRes arrayId: Int): Array<CharSequence> = AppUtil.application.resources.getTextArray(arrayId)

/**
 * 获取dimens资源
 * 单位为px
 */
fun getResDimenPx(@DimenRes dimenRes: Int) = AppUtil.application.resources.getDimensionPixelSize(dimenRes).px

/**
 * 获取dimens中单位为dp的资源
 */
fun getResDimenDp(@DimenRes dimenRes: Int) = AppUtil.application.resources.getDimensionPixelSize(dimenRes)

/**
 * 获取dimens中单位为Sp的资源
 */
fun getResDimenSp(@DimenRes dimenRes: Int) = AppUtil.application.resources.getDimensionPixelSize(dimenRes).spx
