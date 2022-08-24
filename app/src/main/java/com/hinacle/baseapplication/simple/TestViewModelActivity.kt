package com.hinacle.baseapplication.simple

import androidx.activity.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hinacle.base.app.AppActivity
import com.hinacle.base.util.toast.toast
import com.hinacle.baseapplication.R
import com.hinacle.baseapplication.databinding.ActivityTestViewModelBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestViewModelActivity :AppActivity(R.layout.activity_test_view_model) {

    val vm by viewModels<TestViewModel>()
    val vb by viewBinding(ActivityTestViewModelBinding::bind)

    override fun superOnCreate() {
        super.superOnCreate()

    }

    override fun initView() {
        with(vb) {

        }
    }

    override fun initData() {
        vm.getLiveData.observe(this){
            toast { it }
        }
        vm.getData()
    }
}