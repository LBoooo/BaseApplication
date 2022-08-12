package com.hinacle.baseapplication.main

import android.view.View
import androidx.viewbinding.ViewBinding
import com.hinacle.base.databinding.HeaderAwesomeBinding
import com.hinacle.base.databinding.WidgetTitleNormalBinding
import com.hinacle.base.util.dp
import com.hinacle.base.util.fadeIn
import com.hinacle.base.util.fadeOut
import com.hinacle.base.util.logcat.logcat
import com.hinacle.base.widget.dialog.AwesomeHeaderBuilder
import com.hinacle.base.widget.dialog.BottomSheetDialog
import com.hinacle.base.widget.dialog.ContentBuilder
import com.hinacle.baseapplication.R
import com.hinacle.baseapplication.databinding.DialogBottomBinding
import com.hinacle.baseapplication.databinding.DialogBottomFootBinding
import com.hinacle.baseapplication.databinding.DialogHeaderAnimBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter


class HeaderBuilder : ContentBuilder<WidgetTitleNormalBinding>() {
    override val layoutId: Int = com.hinacle.base.R.layout.widget_title_normal

    override fun init() {

    }

    override fun updateContent(type: Int, data: Any?) {
    }
}

class ContentBuilder : ContentBuilder<DialogBottomBinding>() {
    override val layoutId: Int = R.layout.dialog_bottom_

    val itemAdapter = ItemAdapter<MessageItem>()
    override fun init() {
        viewBinding.recyclerView.apply {
            this.adapter = FastAdapter.with(itemAdapter)
        }
    }

    override fun updateContent(type: Int, data: Any?) {
        itemAdapter.set((0..50).map {
            MessageItem("扩展。。。。。。。${it}")
        })
    }

}

class FooterBuilder : ContentBuilder<DialogBottomFootBinding>() {
    override val layoutId: Int = R.layout.dialog_bottom_foot
    override fun init() {

    }

    override fun updateContent(type: Int, data: Any?) {

    }
}

class AnimHeadBuilder(bsDialog:BottomSheetDialog) : AwesomeHeaderBuilder<DialogHeaderAnimBinding>(bsDialog) {
    override val layoutId: Int = R.layout.dialog_header_anim

    override  fun onExpend (){
        viewBinding.title.fadeOut(400, endStatus = View.INVISIBLE)
        viewBinding.titleBackBtn.fadeIn(400)
    }

    override fun onClose(){
        viewBinding.titleBackBtn.fadeOut(400, endStatus = View.INVISIBLE)
        viewBinding.title.fadeIn(400)
    }

    override val roundAnimationView = viewBinding.titleLayout
    override val fillLayout: View by lazy {
        viewBinding.fillView
    }
    override val fillView: View
        get() = viewBinding.fillView

}

class AnimHeadBuilder1(bsDialog:BottomSheetDialog) : AwesomeHeaderBuilder<HeaderAwesomeBinding>(bsDialog,false,15f.dp,0xffffff) {
    override val layoutId: Int = com.hinacle.base.R.layout.header_awesome

    override  fun onExpend (){
        viewBinding.littleTitle.fadeOut(400, endStatus = View.INVISIBLE)
        viewBinding.titleLay.fadeIn(400)
    }

    override fun onClose(){
        viewBinding.titleLay.fadeOut(400, endStatus = View.INVISIBLE)
        viewBinding.littleTitle.fadeIn(400)
    }

    override val roundAnimationView by lazy {
        viewBinding.hideTitle
    }
    override val fillLayout: View by lazy {
        viewBinding.fillLayout
    }
    override val fillView: View by lazy {
        viewBinding.fillView
    }

}