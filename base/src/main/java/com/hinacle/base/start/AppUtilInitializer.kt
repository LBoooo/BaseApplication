package com.hinacle.base.start

import android.content.Context
import androidx.startup.Initializer
import com.hinacle.base.util.AppUtil

class AppUtilInitializer : Initializer<AppUtil>{
    override fun create(context: Context): AppUtil {
        return AppUtil.also {
            AppUtil.init(context)
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return emptyList<Class<out Initializer<*>>>().toMutableList()
    }


}