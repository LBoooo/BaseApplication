package com.hinacle.base.widget.banner

import android.view.View.VISIBLE
import android.widget.ImageView
import com.hinacle.base.R
import com.hinacle.base.databinding.ItemBannerImageBinding
import com.hinacle.base.util.dp
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import com.zhpan.bannerview.constants.IndicatorGravity
import com.zhpan.indicator.enums.IndicatorStyle


internal var loadImageListener: ((ImageView, Any?, Int) -> Unit)? = null
internal var loadDataListener: (() -> List<Any>?)? = null

fun <T> BannerViewPager<T>.loadImage(listener: ((ImageView, Any?, Int) -> Unit)?) {
    loadImageListener = listener
}

fun <T> BannerViewPager<T>.loadData(listener: (() -> List<Any>?)?) {
    loadDataListener = listener
}

// banner扩展函数 这里方便使用
@Suppress("UNCHECKED_CAST")
fun <T> BannerViewPager<T>.bindBanner(block: BannerViewPager<T>.() -> Unit) {
    setCanLoop(true)  // default = true
    setAutoPlay(true) // default = true
    setInterval(5000) // default = 3000
    setScrollDuration(500) // default value is equals ViewPager2 item scroll time
    setRoundCorner(8.dp)  // default = 0
    setIndicatorVisibility(VISIBLE) // default is VISIBLE
    setIndicatorStyle(IndicatorStyle.ROUND_RECT) // enum(CIRCLE, DASH、ROUND_RECT) default value is CIRCLE
    setIndicatorGravity(IndicatorGravity.CENTER) // enum(CENTER、START、END),default value is CENTER
    // ...更多用法 https://github.com/zhpanvip/BannerViewPager/wiki/02.Docment
    block()
    adapter = object : BaseBannerAdapter<T>() {
        override fun bindData(
            holder: BaseViewHolder<T>,
            data: T?,
            position: Int,
            pageSize: Int
        ) {
//            //viewBinding 有问题 先不用
//            val binding = ItemBannerImageBinding.bind(holder.itemView)
//            loadImageListener?.invoke(binding.bannerImageView, data, position)

            val imageView = holder.findViewById<ImageView>(R.id.bannerImageView)
            loadImageListener?.invoke(imageView, data, position)

        }

        override fun getLayoutId(viewType: Int): Int {
            return R.layout.item_banner_image
        }
    }

    create(loadDataListener?.invoke() as List<T>)


}