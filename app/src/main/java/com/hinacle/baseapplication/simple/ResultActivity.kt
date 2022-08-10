package com.hinacle.baseapplication.simple

import android.content.Intent
import com.hinacle.base.app.AppActivity
import com.hinacle.baseapplication.R

class ResultActivity :AppActivity(R.layout.activity_result) {


    override fun initData() {
        setResult(RESULT_OK, Intent().putExtra("value","I am back !"))
    }

}