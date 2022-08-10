package com.hinacle.base.widget.title

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.hinacle.base.R
import com.hinacle.base.databinding.WidgetTitleNormalBinding

class TitleLayout:ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var viewBinding : WidgetTitleNormalBinding
    init {
        val root = LayoutInflater.from(context).inflate(R.layout.widget_title_normal,this , false)
        viewBinding = WidgetTitleNormalBinding.bind(root)
        addView(root)
    }


}