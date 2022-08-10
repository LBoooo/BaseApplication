package com.hinacle.base.vm

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeLife
import com.hinacle.base.app.AppApplication

open class AppViewModel : ViewModel() {
// 可以获取application的vm 具体差异查看google介绍
//(val application: AppApplication = AppApplication.instance()):AndroidViewModel(application){

}