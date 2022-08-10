package com.hinacle.baseapplication.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hinacle.base.util.onStart
import com.hinacle.baseapplication.R
import com.hinacle.baseapplication.databinding.ItemMessageBinding
import com.hinacle.baseapplication.simple.*
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.mikepenz.fastadapter.binding.BindingViewHolder
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import com.mikepenz.fastadapter.items.ModelAbstractItem
import com.mikepenz.fastadapter.listeners.ClickEventHook
import koleton.api.generateSkeleton
import koleton.custom.KoletonView

class MessageItem(val name: String) : AbstractBindingItem<ItemMessageBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemMessageBinding {
        return ItemMessageBinding.inflate(inflater, parent, false)
    }

    override val type: Int = 0

    override fun bindView(binding: ItemMessageBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.name = name
    }

    override fun getViewHolder(viewBinding: ItemMessageBinding): BindingViewHolder<ItemMessageBinding> {

        return super.getViewHolder(viewBinding)
    }

    class ItemClickEvent : ClickEventHook<MessageItem>() {
        override fun onBind(viewHolder: RecyclerView.ViewHolder): View {
            return viewHolder.itemView
        }

        override fun onClick(
            v: View,
            position: Int,
            fastAdapter: FastAdapter<MessageItem>,
            item: MessageItem
        ) {
            when (position) {
                0 -> v.context.onStart<ScreenActivity>()
                1 -> v.context.onStart<LogcatActivity>()
                2 -> v.context.onStart<PhotoActivity>()
                3 -> v.context.onStart<TitleAndViewActivity>()
                4 -> v.context.onStart<NetActivity>()
                5 -> v.context.onStart<PermissionsActivity>()
                6 -> v.context.onStart<OtherActivity>()
                7 -> v.context.onStart<ResourceActivity>()
            }
        }
    }
}


class MessageModelItem(val state: Int) : ModelAbstractBindingItem<Int, ItemMessageBinding>(state) {
    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemMessageBinding {
        return ItemMessageBinding.inflate(inflater, parent, false)
    }

    override val type: Int = 0

    var name = ""

    override fun bindView(binding: ItemMessageBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.name = name
    }
}

class MessageLoadingItem(val state: Int) :
    ModelAbstractItem<Int, MessageLoadingItem.SkeletonViewHolder>(state) {


    override val type: Int = 1


    class SkeletonViewHolder(val koletonView: KoletonView) : RecyclerView.ViewHolder(koletonView)

    override val layoutRes: Int = R.layout.item_message

    override fun getViewHolder(v: View): SkeletonViewHolder {
        return SkeletonViewHolder(v.generateSkeleton())
    }

    override fun bindView(holder: SkeletonViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)
        holder.koletonView.showSkeleton()
    }
}







