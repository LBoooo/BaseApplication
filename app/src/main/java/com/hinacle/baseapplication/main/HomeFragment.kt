package com.hinacle.baseapplication.main

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hinacle.base.app.AppFragment
import com.hinacle.baseapplication.R
import com.hinacle.baseapplication.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment private constructor() : AppFragment(R.layout.fragment_home) {

    private val viewBinding by viewBinding(FragmentHomeBinding::bind)

    override fun initView() {
        super.initView()
//        viewBinding.webview.loadUrl()

    }


    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}