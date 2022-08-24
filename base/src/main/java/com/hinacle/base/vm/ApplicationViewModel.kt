package com.hinacle.base.vm

import androidx.lifecycle.AndroidViewModel
import com.hinacle.base.app.AppApplication

open class ApplicationViewModel (val application: AppApplication = AppApplication.instance()):
    AndroidViewModel(application){
}