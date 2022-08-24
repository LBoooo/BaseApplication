package com.hinacle.base.util.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hinacle.base.util.AppUtil
import com.hinacle.base.util.requestPermission
import java.io.IOException

/**
 * 只获取一次 自动开始 自动停止
 */
@SuppressLint("MissingPermission")
inline fun AppCompatActivity.getLocation(crossinline block: Location.() -> Unit) {

    with(LocationTracker()) {
        addListener(object : LocationTracker.Listener {
            override fun onLocationFound(location: Location) {
                location.block()
                stopListening()
            }

            override fun onProviderError(providerError: ProviderError) {}
        })
        startListening(this@getLocation)
    }

}


/**
 * 注册监听
 * startListening(this) 开始
 * stopListening() 停止
 */
inline fun registerLocation(crossinline block: Location.() -> Unit): LocationTracker {
    return with(LocationTracker()) {
        addListener(object : LocationTracker.Listener {
            override fun onLocationFound(location: Location) {
                location.block()
            }

            override fun onProviderError(providerError: ProviderError) {
            }
        })
        this
    }
}


/**
 * 只获取一次 自动开始 自动停止
 */
@SuppressLint("MissingPermission")
inline fun Fragment.getLocation(crossinline block: Location.() -> Unit) {
    with(LocationTracker()) {
        addListener(object : LocationTracker.Listener {
            override fun onLocationFound(location: Location) {
                location.block()
                stopListening()
            }

            override fun onProviderError(providerError: ProviderError) {}
        })
        startListening(requireContext())
    }
}


inline fun Location.getAddress(crossinline block: (Address) -> Unit) {
    val geocoder = Geocoder(AppUtil.application)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // api 33 以上使用 GeocodeListener 接口回调
        geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
            addresses.forEach {
                block.invoke(it)
            }
        }
    } else {
        // api 33 以下使用 使用网络获取地址 可阻塞线程最高60秒 需要异步使用
        var addressList: List<Address>? = null
        try {
            @Suppress("DEPRECATION")
            addressList = geocoder.getFromLocation(latitude, longitude, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (addressList != null) {
            for (address in addressList) {
                block.invoke(address)
            }
        }
    }
}