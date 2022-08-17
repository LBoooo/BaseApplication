package com.hinacle.baseapplication

import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.location.*
import com.hinacle.base.util.AppUtil
import com.hinacle.base.util.logcat.logcat
import java.io.IOException

/**
 * @see com.hinacle.base.widget.location
 */
@Deprecated("实验code")
object LocationHelper {

    private val mLocationListener: LocationListener = object : LocationListener {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
//        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
//            Log.d(TAG, "onStatusChanged")
//        }

        // Provider被enable时触发此函数，比如GPS被打开
        override fun onProviderEnabled(provider: String) {
            logcat { "onProviderEnabled" }
        }

        // Provider被disable时触发此函数，比如GPS被关闭
        override fun onProviderDisabled(provider: String) {
            logcat { "onProviderDisabled" }
        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        override fun onLocationChanged(location: Location) {
            logcat { "location: longitude: ${location.latitude}, latitude: ${location.longitude}" }

            //更新位置信息
        }
    }

    /**
     * 监听位置变化
     */
    @SuppressLint("MissingPermission")
    fun initLocationListener() {
        val locationManager = AppUtil.application.getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10f, mLocationListener)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f, mLocationListener)
    }

    @SuppressLint("MissingPermission")
      fun getLastLocation(): Location? {
        var location: Location? = null
        val locationManager = AppUtil.application
            .getSystemService(LOCATION_SERVICE) as LocationManager ?: return null
        val providers = locationManager.getProviders(true)
        for (provider in providers) {
            val l = locationManager.getLastKnownLocation(provider) ?: continue
            if (location == null || l.accuracy < location.accuracy) {
                location = l
            }
        }
        return location
    }

    fun getAddress(latitude: Double, longitude: Double) {
        var addressList: List<Address?>? = null
        val geocoder = Geocoder(AppUtil.application)
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (addressList != null) {
            for (address in addressList) {
                logcat { "address:${address}" }
            }
        }
    }
}