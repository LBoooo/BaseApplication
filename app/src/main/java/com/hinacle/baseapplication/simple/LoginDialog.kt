package com.hinacle.baseapplication.simple

import android.view.View
import androidx.fragment.app.FragmentManager
import com.drake.net.utils.runMain
import com.hinacle.base.util.dp
import com.hinacle.base.util.fadeIn
import com.hinacle.base.util.fadeOut
import com.hinacle.base.widget.dialog.bottomsheet.BottomSheetDialog
import com.hinacle.base.widget.dialog.bottomsheet.ContentBuilder
import com.hinacle.base.widget.dialog.bottomsheet.ExFullHeaderBuilder
import com.hinacle.baseapplication.R
import com.hinacle.baseapplication.databinding.DialogContentLoginBinding
import com.hinacle.baseapplication.databinding.DialogHeaderLoginBinding


class LoginDialog {

    companion object {
        fun builder(fragmentManager: FragmentManager): LoginDialog {
            return LoginDialog().apply {
                this.bottomSheetDialog =
                    BottomSheetDialog(fragmentManager, peekHeight = 300.dp, isUseFullScreen = true)
            }
        }
    }

    lateinit var bottomSheetDialog: BottomSheetDialog

    fun init() {
        with(bottomSheetDialog) {
            header(LoginHeader(this))
            content(LoginContent())

        }
    }

    fun show() {
        runMain {
            bottomSheetDialog.show()
        }
    }

    class LoginHeader(dialog: BottomSheetDialog) : ExFullHeaderBuilder<DialogHeaderLoginBinding>(dialog , titleRoundRadius = 15f.dp) {

        override val layoutId: Int = R.layout.dialog_header_login

        override fun onExpanded() {
            super.onExpanded()
            viewBinding.backIv.fadeIn(400)
            viewBinding.titleTv.fadeIn(400)
            viewBinding.topBar.fadeOut(400 , View.INVISIBLE)
        }

        override fun onCollapsed() {
            super.onCollapsed()
            viewBinding.backIv.fadeOut(400 , View.INVISIBLE)
            viewBinding.titleTv.fadeOut(400 , View.INVISIBLE)
            viewBinding.topBar.fadeIn(400)
        }

    }

    class LoginContent(): ContentBuilder<DialogContentLoginBinding>() {
        override val layoutId: Int = R.layout.dialog_content_login

        override fun init() {

        }

        override fun updateContent(type: Int, data: Any?) {

        }

    }
}