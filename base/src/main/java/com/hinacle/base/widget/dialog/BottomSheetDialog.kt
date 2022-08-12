package com.hinacle.base.widget.dialog

import android.view.*
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hinacle.appdialog.extensions.addShowDismissListener
import com.hinacle.appdialog.extensions.bindingListenerFun
import com.hinacle.appdialog.extensions.newAppDialog
import com.hinacle.appdialog.other.DialogGravity
import com.hinacle.appdialog.other.DialogInitMode
import com.hinacle.base.R
import com.hinacle.base.databinding.DialogBottomSheetBinding
import com.hinacle.base.util.dp
import com.hinacle.base.util.onShakeClickListener

class BottomSheetDialog(
    private val fragmentManager: FragmentManager,
    private val peekHeight: Int = 500.dp,
    private val maxHeight: Int = 0,
    private val isUseFullScreen: Boolean = false
) {
    val appDialog by lazy {
        newAppDialog {
            initMode = DialogInitMode.LAZY
            layoutId = R.layout.dialog_bottom_sheet
            if (maxHeight == 0) isFullVerticalOverStatusBar = true else height = maxHeight
            isFullHorizontal = true
            unLeak = true

            if (isUseFullScreen) {
                setStatusBarModeFun = { dialog ->
                    dialog.dialog?.window?.let { window ->
                        window.decorView.fitsSystemWindows = true
                        @Suppress("DEPRECATION")
                        window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE //View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    }
                }
                dialogThemeFun = {
                    com.hinacle.appdialog.R.style.AppDialogFullScreen
                }
            }
            lateinit var behavior: BottomSheetBehavior<*>
            bindingListenerFun(DialogBottomSheetBinding::class) { binding, dialog ->
                behavior = BottomSheetBehavior.from(binding.bottomSheetLayout).apply {
                    peekHeight = this@BottomSheetDialog.peekHeight
                }
                behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            BottomSheetBehavior.STATE_HIDDEN -> {
                                dialog.dismiss()
                            }
                            BottomSheetBehavior.STATE_COLLAPSED -> {
                                if (isFirstShow) {
                                    isFirstShow = false
                                    headerBuilder?.onAfterShow()
                                    contentBuilder?.onAfterShow()
                                    footerBuilder?.onAfterShow()
                                }
                                onCollapsed()
                            }
                            BottomSheetBehavior.STATE_EXPANDED -> {
                                if (isFirstShow) {
                                    isFirstShow = false
                                    headerBuilder?.onAfterShow()
                                    contentBuilder?.onAfterShow()
                                    footerBuilder?.onAfterShow()
                                }
                                onExpanded()
                            }
                            BottomSheetBehavior.STATE_DRAGGING -> {}
                            BottomSheetBehavior.STATE_HALF_EXPANDED -> {}
                            BottomSheetBehavior.STATE_SETTLING -> {}
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        onSideListener.invoke(slideOffset)
                        footerBuilder ?: return
                        if (slideOffset < 0) {
                            binding.footerContainer.scrollY = (slideOffset * bottomHeight).toInt()
                        } else {
                            binding.footerContainer.scrollY = 0
                        }
                    }
                })
                behavior.isHideable = true
                binding.rootLayout.onShakeClickListener {
                    behavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
                addShowDismissListener("key") {
                    onDialogShow {
                        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }

                headerBuilder?.let {
                    binding.headerContainer.addView(dialog.context?.let { ctx -> it.build(ctx) })
                }
                contentBuilder?.let {
                    binding.content.addView(dialog.context?.let { ctx -> it.build(ctx) })
                }
                footerBuilder?.let {
                    binding.footerContent.addView(dialog.context?.let { ctx ->
                        it.build(ctx).apply {
                            post {
                                bottomHeight = binding.footerContainer.height
                                binding.container.layoutParams =
                                    (binding.container.layoutParams as ViewGroup.MarginLayoutParams).also { p ->
                                        p.setMargins(0, 0, 0, bottom)
                                    }
                            }
                        }
                    })
                }
            }
        }
    }

    private var isFirstShow = true
    var onSideListener: ((Float) -> Unit) = {}
    fun onCollapsed() {}
    fun onExpanded() {}
    fun <T : ContentBuilder<*>> header(headerBuilder: T, action: (T.() -> Unit)? = null) {
        action?.also { it.invoke(headerBuilder) }
        this.headerBuilder = headerBuilder
    }

    fun <T : ContentBuilder<*>> content(contentBuilder: T, action: (T.() -> Unit)? = null) {
        action?.also { it.invoke(contentBuilder) }
        this.contentBuilder = contentBuilder
    }

    fun <T : ContentBuilder<*>> footer(footerBuilder: T, action: (T.() -> Unit)? = null) {
        action?.also { it.invoke(footerBuilder) }
        this.footerBuilder = footerBuilder
    }

    /**
     * 底部布局高度
     */
    private var bottomHeight = 0

    /**
     * 头部布局
     */
    private var headerBuilder: ContentBuilder<*>? = null

    /**
     * 内容布局
     */
    private var contentBuilder: ContentBuilder<*>? = null

    /**
     * 底部布局（悬浮）
     */
    private var footerBuilder: ContentBuilder<*>? = null

    fun show() {
        appDialog.showOnWindow(
            fragmentManager, DialogGravity.CENTER_BOTTOM,
            com.hinacle.appdialog.R.style.BottomTransAlphaADAnimation
        )
        headerBuilder?.onShow()
        contentBuilder?.onShow()
        footerBuilder?.onShow()
    }
}