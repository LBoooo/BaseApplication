package com.hinacle.baseapplication.simple

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.hinacle.base.app.AppActivity
import com.hinacle.base.util.getResColor
import com.hinacle.base.util.onShakeClickListener
import com.hinacle.base.util.toast.toast
import com.hinacle.baseapplication.R
import com.hinacle.baseapplication.databinding.ActivityTitleAndViewBinding
import com.hinacle.baseapplication.databinding.ItemFlexBoxBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.AbstractBindingItem


class TitleAndViewActivity : AppActivity(R.layout.activity_title_and_view) {

    private val vb by viewBinding(ActivityTitleAndViewBinding::bind)

    private val colors = arrayOf(
        R.color.app_blue_0, R.color.app_blue_1,
        R.color.app_green_0, R.color.app_green_1, R.color.app_green_2, R.color.app_green_3,
        R.color.app_orange_0, R.color.app_orange_1, R.color.app_orange_2,
    )
    private val names = arrayOf("java","kotlin","android","A","FlexboxLayoutManager")

    override fun initView() {
        val itemAdapter = ItemAdapter<FlexboxItem>()
        vb.recyclerView.apply {
            adapter = FastAdapter.with(itemAdapter)
            with(layoutManager as FlexboxLayoutManager){
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
        }
        itemAdapter.set(colors.map {
            FlexboxItem(names.random() , getResColor(it))
        })

        vb.themedButtonGroup.setOnSelectListener { button ->
            vb. tvText.text = button.text
        }
        vb.time.setOnSelectListener { button ->
            vb. tvText.text = button.text
        }
        vb.switchBtn.onShakeClickListener {
          switchState(true)
        }


    }

    override fun onBackClick(v: View) {
        toast { "拦截返回" }
    }

    override fun onTitleButtonClick(v: View) {
        toast { "标题按钮" }
    }

    class FlexboxItem(val name: String , val color:Int) : AbstractBindingItem<ItemFlexBoxBinding>() {
        override fun createBinding(
            inflater: LayoutInflater,
            parent: ViewGroup?
        ): ItemFlexBoxBinding {
            return ItemFlexBoxBinding.inflate(inflater, parent, false)
        }

        override val type: Int = 0

        override fun bindView(binding: ItemFlexBoxBinding, payloads: List<Any>) {
            binding.name = name
            binding.color = color
        }
    }
}