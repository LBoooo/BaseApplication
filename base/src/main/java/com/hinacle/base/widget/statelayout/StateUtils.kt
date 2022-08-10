@file:Suppress("unused")

package com.hinacle.base.widget.statelayout

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager

/**
 * 创建一个缺省页来包裹Activity
 * 但是更建议在XML布局中创建, 可保持代码可读性且避免不必要的问题发生, 性能也更优
 */
fun Activity.state(): StateLayout {
    val view = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
    return view.state()
}

/**
 * 创建一个缺省页来包裹Fragment
 * 但是更建议在XML布局中创建, 可保持代码可读性且避免不必要的问题发生, 性能也更优
 */
fun Fragment.state(): StateLayout {
    val stateLayout = requireView().state()

    viewLifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {

//        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//        fun removeState() {
//            val parent = stateLayout.parent as ViewGroup
//            parent.removeView(stateLayout)
//            lifecycle.removeObserver(this)
//        }

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when(event) {
                Lifecycle.Event.ON_DESTROY -> {
                    fun removeState() {
                        val parent = stateLayout.parent as ViewGroup
                        parent.removeView(stateLayout)
                        lifecycle.removeObserver(this)
                    }
                }
                else->{}
            }
        }
    })

    return stateLayout
}

/**
 * 创建一个缺省页来包裹视图
 * 但是更建议在XML布局中创建, 可保持代码可读性且避免不必要的问题发生, 性能也更优
 */
fun View.state(): StateLayout {
    val parent = parent as ViewGroup
    if (parent is ViewPager || parent is RecyclerView) {
        throw UnsupportedOperationException("You should using StateLayout wrap [ $this ] in layout when parent is ViewPager or RecyclerView")
    }
    val stateLayout = StateLayout(context)
    stateLayout.id = id
    val index = parent.indexOfChild(this)
    val layoutParams = layoutParams
    parent.removeView(this)
    parent.addView(stateLayout, index, layoutParams)

    when (this) {
        is ConstraintLayout -> {
            val contentViewLayoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            stateLayout.addView(this, contentViewLayoutParams)
        }
        else -> {
            stateLayout.addView(this)
        }
    }

    stateLayout.setContent(this)
    return stateLayout
}

