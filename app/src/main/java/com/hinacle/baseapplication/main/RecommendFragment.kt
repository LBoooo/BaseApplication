package com.hinacle.baseapplication.main

import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.drake.net.utils.scope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.hinacle.appdialog.AppDialog
import com.hinacle.appdialog.extensions.addShowDismissListener
import com.hinacle.appdialog.extensions.bindingListenerFun
import com.hinacle.appdialog.extensions.newAppDialog
import com.hinacle.appdialog.other.DialogGravity
import com.hinacle.base.app.AppFragment
import com.hinacle.base.util.dp
import com.hinacle.base.util.onShakeClickListener
import com.hinacle.base.util.toast.toast
import com.hinacle.base.widget.banner.bindBanner
import com.hinacle.base.widget.banner.loadData
import com.hinacle.base.widget.banner.loadImage
import com.hinacle.base.widget.dialog.bottomsheet.BottomSheetDialog
import com.hinacle.base.widget.statelayout.state
import com.hinacle.baseapplication.R
import com.hinacle.baseapplication.databinding.DialogBottomSheetTest0Binding
import com.hinacle.baseapplication.databinding.FragmentRecommendBinding
import com.hinacle.baseapplication.simple.LoginDialogHelper
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import net.mikaelzero.mojito.ext.mojito

@AndroidEntryPoint
class RecommendFragment : AppFragment(R.layout.fragment_recommend) {
    private val viewBinding by viewBinding(FragmentRecommendBinding::bind)

    private val images = ArrayList<String>().apply {
        add(("https://upload-bbs.mihoyo.com/upload/2019/08/21/73766616/4d09b6b94491d3921344be906aa7971a_4136353673894269217.png"))
        add(("https://upload-bbs.mihoyo.com/upload/2019/08/12/50600998/1543e13e5cba414a1e4d396d8e6bdbb0_4959259236143228277.jpg"))
        add(("https://upload-bbs.mihoyo.com/upload/2019/02/03/74582189/ede10255b2a99cfcf33868d1afd81a92_6059341049122226062.png"))
        add(("https://upload-bbs.mihoyo.com/upload/2019/08/06/75158229/53c6eb0e1c4bb8db97cbd9c8db631423_3306524819178217982.jpg"))
        add(("https://upload-bbs.mihoyo.com/upload/2019/08/08/10982654/fe2e9243c4e6ea7e489f81ae3814ed08_3279663480817048245.jpg"))
        add(("https://upload-bbs.mihayo.com/upload/2019/03/01/73565430/82a40083d95800c553d036b8c0689323_4849126433310918291.png"))
    }


    private fun showImages(i: Int) {

//        Mojito.start(requireContext()) {
//            urls(images)
//            views(arrayOf(viewBinding.mImagesIv1, viewBinding.mImagesIv2, viewBinding.mImagesIv3))
//            position(i)
//            progressLoader {
//                DefaultPercentProgress()
//            }
//            setIndicator(NumIndicator())
//        }

        if (i == 0)

            bd.showOnWindow(
                childFragmentManager, DialogGravity.CENTER_BOTTOM,
                com.hinacle.appdialog.R.style.BottomTransAlphaADAnimation
            )

        if (i == 1)
//            scope {
//                withIO {
//                    LoginDialogHelper.loginDialog?.show()
//                }
//            }

//            BottomDialog.builder(requireActivity()) {
//
//            }.show()
            LoginDialogHelper.loginDialog?.show()
        if (i == 2){
//            com.hinacle.base.widget.dialog.BottomDialog(requireContext()).apply {
//
//            }.show()
            bsd.show()
        }

    }


    private fun initDialog(){
        bd = newAppDialog {
            layoutId = R.layout.dialog_bottom_sheet_test_0
            isFullHorizontal = true
            unLeak = true
            val adapter = ItemAdapter<MessageItem>()
            lateinit var bsb : BottomSheetBehavior<*>
            bindingListenerFun(DialogBottomSheetTest0Binding::class) { binding, dialog ->
                binding.recyclerView.apply {
                    this.adapter = FastAdapter.with(adapter)
                }

                  bsb = BottomSheetBehavior.from(binding.bottomSheetLayout).apply {
                    peekHeight = 350.dp
//                    state = BottomSheetBehavior.STATE_COLLAPSED
                }

                bsb.addBottomSheetCallback(object :BottomSheetCallback(){
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                         if (newState == BottomSheetBehavior.STATE_HIDDEN){
                             dialog.dismiss()
                         }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {

                    }
                })
                bsb.isHideable = true
                binding.root.onShakeClickListener { bsb.state = BottomSheetBehavior.STATE_HIDDEN }
                adapter.set((0..50).map {
                    MessageItem("扩展。。。。。。。${it}")
                })
            }
            addShowDismissListener("key"){
                onDialogShow {
                    bsb.state = BottomSheetBehavior.STATE_COLLAPSED
                }
                onDialogDismiss {

                }
            }
        }
    }

    private val bsd : BottomSheetDialog by lazy {
        BottomSheetDialog(childFragmentManager , isUseFullScreen = true).apply {
            header(AnimHeadBuilder1(this))
//            header(HeaderBuilder())
            content(ContentBuilder())
            footer(FooterBuilder())

        }

    }

    private lateinit var bd : AppDialog

    override fun lateInit() {
        initDialog()
        with(viewBinding) {
            mImagesIv1.load(images[0])
            mImagesIv2.load(images[1])
            mImagesIv3.load(images[2])
            mImagesIv1.onShakeClickListener {
                showImages(0)
            }

            mImagesIv2.onShakeClickListener {
                showImages(1)
            }

            mImagesIv3.onShakeClickListener {
                showImages(2)
            }

            // banner扩展函数 具体查看扩展源码
            banner.bindBanner {
                loadImage { imageView, any, i ->
                    imageView.load(any)
                    imageView.onShakeClickListener {
                        imageView.mojito(any.toString())
                    }

                }
                setLifecycleRegistry(lifecycle)
                loadData { images }
            }

        }

        state().apply {
            onRefresh {
                toast { "重试" }
            }
            scope {
                showLoading()
                delay(2000)
                when ((0..2).random()) {
                    0 -> showContent()
                    1 -> showError(NullPointerException())
                    2 -> showEmpty()
                }
                delay(1000)
                showContent()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = RecommendFragment()
    }
}