package com.hinacle.baseapplication.simple

import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hinacle.base.app.AppActivity
import com.hinacle.base.datastore.editUserInfo
import com.hinacle.base.util.currentTimeMillis
import com.hinacle.base.util.logcat.logcat
import com.hinacle.baseapplication.R
import com.hinacle.baseapplication.data.User
import com.hinacle.baseapplication.data.logOut
import com.hinacle.baseapplication.databinding.ActivityUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : AppActivity(R.layout.activity_user) {

    private val viewBinding by viewBinding(ActivityUserBinding::bind)

    override fun initView() {

    }
    fun login(v: View) {

        logcat { "click------------>>>${currentTimeMillis}" }
        editUserInfo<User> {
            logcat { "query------------>>>${currentTimeMillis}" }
            if (it == null) {
                logcat { "未找到用户信息" }
                User().apply {
                    token = "$currentTimeMillis"
                    id = "123"
                    name = "哈哈哈"
                }
            } else {
                logcat { "找到用户信息" }
                it.apply {
                    token = "$currentTimeMillis"
                }
            }
        }
        logcat { "save------------>>>${currentTimeMillis}" }
    }

    fun logout(v: View) {
//        editUserInfo<User> { null }
        logOut()
    }
}