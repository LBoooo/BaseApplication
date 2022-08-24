package com.hinacle.base.widget.dialog.loading

import com.hinacle.base.app.AppActivity
import com.hinacle.base.app.AppFragment

fun AppActivity.showLoading() {
    loadingDialog.showLoading()
}

fun AppActivity.dismissLoading() {
    loadingDialog.dismissLoading()
}

fun AppFragment.showLoading() {
    loadingDialog.showLoading()
}

fun AppFragment.dismissLoading() {
    loadingDialog.dismissLoading()
}