package com.hinacle.baseapplication.main

import android.view.View
import com.hinacle.base.databinding.HeaderAwesomeBinding
import com.hinacle.base.databinding.WidgetTitleNormalBinding
import com.hinacle.base.util.dp
import com.hinacle.base.util.fadeIn
import com.hinacle.base.util.fadeOut
import com.hinacle.base.util.getResColor
import com.hinacle.base.widget.dialog.bottomsheet.ExFullHeaderBuilder
import com.hinacle.base.widget.dialog.bottomsheet.BottomSheetDialog
import com.hinacle.base.widget.dialog.bottomsheet.ContentBuilder
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

class AnimHeadBuilder(bsDialog: BottomSheetDialog) :
    ExFullHeaderBuilder<DialogHeaderAnimBinding>(bsDialog) {
    override val layoutId: Int = R.layout.dialog_header_anim

    override fun onExpanded() {
        viewBinding.title.fadeOut(400, endStatus = View.INVISIBLE)
        viewBinding.titleBackBtn.fadeIn(400)
    }

    override fun onCollapsed() {
        viewBinding.titleBackBtn.fadeOut(400, endStatus = View.INVISIBLE)
        viewBinding.title.fadeIn(400)
    }

}

class AnimHeadBuilder1(bsDialog: BottomSheetDialog) :
    ExFullHeaderBuilder<HeaderAwesomeBinding>(bsDialog, true, 15f.dp, getResColor(com.hinacle.base.R.color.app_white)) {
    override val layoutId: Int = com.hinacle.base.R.layout.header_awesome

    override fun onExpanded() {
        super.onExpanded()
        viewBinding.closeBtn.fadeIn(400 )
        viewBinding.title.fadeOut(400)
    }

    override fun onCollapsed() {
        super.onCollapsed()
        viewBinding.closeBtn.fadeOut(400)
        viewBinding.title.fadeIn(400)
    }
}