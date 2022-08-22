package com.hinacle.base.util


//inline val density get() = AppUtil.application.resources.displayMetrics.density
////////////////////////////////////////////////////////////////////////////////////////////////////
//// dp 转 px 扩展名为dp为了方便使用 以下同 正常的做法
inline val Float.dp get() = this * screenDensity + 0.5F
inline val Int.dp get() = (this * screenDensity + 0.5F).toInt()
//
//// px 转 dp
inline val Float.px get() = this / screenDensity + 0.5F
inline val Int.px get() =  (this / screenDensity + 0.5F).toInt()
//
//// sp 转 px
inline val Float.sp get() = this * screenDensity + 0.5F
inline val Int.sp get() = (this * screenDensity + 0.5F).toInt()
//
//// px 转 sp
inline val Float.spx get() = this / screenDensity + 0.5F
inline val Int.spx get() = (this / screenDensity + 0.5F).toInt()
////////////////////////////////////////////////////////////////////////////////////////////////////

// 适配方案使用 配合适配使用的单位转换 详情请看AutoSizeUtils
//inline val Float.dp get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, AppUtil.application.resources.displayMetrics) + 0.5f
//inline val Int.dp get() = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), AppUtil.application.resources.displayMetrics) + 0.5f).toInt()

//inline val Float.sp get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, AppUtil.application.resources.displayMetrics) + 0.5f
//inline val Int.sp get() = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), AppUtil.application.resources.displayMetrics) + 0.5f).toInt()

//inline val Float.px get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this, AppUtil.application.resources.displayMetrics) + 0.5f
//inline val Int.px get() = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this.toFloat(), AppUtil.application.resources.displayMetrics) + 0.5f).toInt()

//inline val Float.toPx get() = this / (Resources.getSystem().displayMetrics.density + 0.5F)
//inline val Int.toPx get() = (this / (Resources.getSystem().displayMetrics.density + 0.5F)).toInt()