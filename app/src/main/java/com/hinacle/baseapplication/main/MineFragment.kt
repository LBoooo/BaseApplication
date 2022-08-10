package com.hinacle.baseapplication.main

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.drake.net.utils.scopeLife
import com.hinacle.base.app.AppFragment
import com.hinacle.base.util.logcat.logcat
import com.hinacle.base.util.onShakeClickListener
import com.hinacle.base.util.onStart
import com.hinacle.baseapplication.R
import com.hinacle.baseapplication.databinding.FragmentMineBinding
import com.hinacle.baseapplication.simple.UserActivity
import dagger.hilt.android.AndroidEntryPoint
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MineFragment private constructor(): AppFragment(R.layout.fragment_mine) {
    companion object {
        @JvmStatic
        fun newInstance() =
            MineFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    private val viewBinding by viewBinding(FragmentMineBinding::bind)
    override fun initView() {
        super.initView()
        logcat {
            "initView"
        }

    }

    override fun initData() {
        super.initData()
        logcat {
            "initData"
        }
    }

    override fun superOnCreate() {
        super.superOnCreate()
        logcat {
            "superOnCreate"
        }
    }

    override fun superOnResume() {
        super.superOnResume()
        logcat {
            "superOnResume"
        }
    }

    override fun lateInit() {
        super.lateInit()
        logcat {
            "lateInit"
        }
        with(viewBinding) {
            shineLayout.loadSkeleton {
                userAvatarImage.load("https://upload-bbs.mihoyo.com/upload/2019/08/21/73766616/4d09b6b94491d3921344be906aa7971a_4136353673894269217.png")
                userNameTv.text = "为美好的世界献上祝福"
                userIntoTv.text = "为美好的世界献上祝福"
                scopeLife {
                    delay(1000)
                    shineLayout.hideSkeleton()
                }
                userAvatarImage.onShakeClickListener {
                    requireContext().onStart<UserActivity>()
                }
            }
        }
    }
}