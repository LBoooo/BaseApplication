package com.hinacle.base.widget.dialog

import com.hinacle.appdialog.AppDialog
import com.hinacle.appdialog.other.DialogOptions

class BottomSheetDialog :AppDialog() {


}

inline fun newBottomSheetDialog(options: DialogOptions.(dialog: AppDialog) -> Unit): BottomSheetDialog {
    val appDialog = BottomSheetDialog()
    appDialog.getDialogOptions().options(appDialog)
    return appDialog
}