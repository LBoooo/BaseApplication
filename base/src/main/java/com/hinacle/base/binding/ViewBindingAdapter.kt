package com.hinacle.base.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.hinacle.base.R
import com.hinacle.base.widget.title.TitleLayout
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder


fun ViewPager2.bindPager(fm: FragmentManager, lifecycle: Lifecycle, vararg fragments: Fragment) {
    offscreenPageLimit = fragments.size
    adapter = object : FragmentStateAdapter(fm, lifecycle) {
        override fun getItemCount() = fragments.size
        override fun createFragment(position: Int) = fragments[position]
    }
}



@BindingAdapter("setAppTitle")
fun setTitle(tv: TitleLayout, title: CharSequence) {
    tv.viewBinding.title = title.toString()
}

@BindingAdapter(value = ["bindSrc", "scaleType"])
fun bindImage(imageView: ImageView, data: Any, type: Int = 0) {
    imageView.load(data) {
        when (type) {
            0 -> {
                error(R.color.app_red)
                placeholder(R.color.app_red)
            }
            1 -> {
                error(R.color.app_red)
                placeholder(R.color.app_green)
            }
            2 -> {
                error(R.color.app_red)
                placeholder(R.color.app_blue)
            }
        }
    }
}


