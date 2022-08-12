package com.hinacle.baseapplication.main

import by.kirich1409.viewbindingdelegate.viewBinding
import com.drake.net.utils.scope
import com.hinacle.base.app.AppFragment
import com.hinacle.base.util.rxbus.transmitSticky
import com.hinacle.baseapplication.R
import dagger.hilt.android.AndroidEntryPoint
import com.hinacle.baseapplication.databinding.FragmentMessageBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.adapters.ModelAdapter
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton

@AndroidEntryPoint
class MessageFragment : AppFragment(R.layout.fragment_message) {
    companion object {
        @JvmStatic
        fun newInstance() = MessageFragment()
    }

    private val viewBinding by viewBinding(FragmentMessageBinding::bind)

    private val itemAdapter = ItemAdapter<MessageItem>()

    private val modelAdapter = ModelAdapter { type: Int ->
        when (type) {
            0 -> MessageModelItem(type)
            else -> MessageLoadingItem(type)
        }
    }


    override fun initView() {
        super.initView()

        with(viewBinding) {
            recyclerView.adapter = FastAdapter.with(itemAdapter).apply {
                addEventHook(MessageItem.ItemClickEvent())
            }
        }
    }

    override fun lateInit() {
        super.lateInit()

        with(viewBinding) {

            recyclerView.loadSkeleton(R.layout.item_message_loading) {
                itemCount(10)
            }
            itemAdapter.add(MessageItem("屏幕相关工具"))
            itemAdapter.add(MessageItem("日志相关工具"))
            itemAdapter.add(MessageItem("图片相关工具"))
            itemAdapter.add(MessageItem("标题布局测试"))
            itemAdapter.add(MessageItem("网络相关APi"))
            itemAdapter.add(MessageItem("权限相关Api"))
            itemAdapter.add(MessageItem("更多扩展API"))
            itemAdapter.add(MessageItem("资源相关APi"))
//            itemAdapter.set((0..20).map { MessageItem(it.toString()) })
            scope {
                recyclerView.hideSkeleton()

//                RxBus.getInstance().postSticky("发送粘性事件")

                transmitSticky { "发送粘性事件" }
            }

        }

    }

}