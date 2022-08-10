package com.hinacle.baseapplication.simple

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hinacle.base.app.AppActivity
import com.hinacle.base.util.*
import com.hinacle.base.util.logcat.logcat
import com.hinacle.baseapplication.R
import com.hinacle.baseapplication.databinding.ActivityResourceBinding

class ResourceActivity : AppActivity(R.layout.activity_resource) {
    private val viewBinding by viewBinding(ActivityResourceBinding::bind)

    override fun initView() {

        with(viewBinding) {
            codeTv.apply {
                layoutParams.also {
                    it as LinearLayout.LayoutParams
                    it.setMargins(15.dp, 10.dp, 15.dp, 10.dp)
                    it.height = 30.dp
                    it.width = 150.dp
                }
                gravity = Gravity.CENTER
                setBackgroundColor(Color.parseColor("#f1c40f"))
                @SuppressLint("SetTextI18n")
                text = "code setting"
                textSize = 15f
                setTextColor(Color.parseColor("#ffffff"))
            }

            resourceTv.apply {
                layoutParams.also {
                    it as LinearLayout.LayoutParams
                    it.setMargins(15.dp, 10.dp, 15.dp, 10.dp)
                    it.height = getResDimenDp(R.dimen.resource_dimen_h)
                    it.width = getResDimenDp(R.dimen.resource_dimen_w)
                }
                gravity = Gravity.CENTER
                setBackgroundColor(getResColor(R.color.app_blue_1))
                text = getResString(R.string.resource_text)
                textSize = getResDimenSp(com.hinacle.base.R.dimen.app_text_14).toFloat()
                setTextColor(getResColor(R.color.app_grey_0))
            }

        }
    }

    override fun initData() {

        viewBinding.codeTv.onShakeClickListener {
            logcat {
                        "resDp:${getResDimenDp(R.dimen.resource_dimen_w)}" +
                        "resPx:${getResDimenPx(R.dimen.resource_dimen_w)}" +
                        "resSp:${getResDimenSp(com.hinacle.base.R.dimen.app_text_15)}" +
                        "codeDp:${150.dp}" +
                        "codePx:${432.px}" +
                        "codeSP:${15.sp}"
            }
        }
    }
}