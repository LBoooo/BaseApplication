package com.hinacle.base.util.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.annotation.RequiresPermission

/**
 * @param minTimeBetweenUpdates - 位置更新之间的最小时间间隔，默认以毫秒为单位，其值为 5 分钟
 * @param minDistanceBetweenUpdates - 位置更新之间的最小距离，以米为单位，默认值为 100m
 * @param shouldUseGPS - 指定跟踪器是否应该使用 GPS（默认为 true）
 * @param shouldUseNetwork - 指定跟踪器是否应该使用网络（默认为 true）
 * @param shouldUsePassive - 指定跟踪器是否应该使用被动提供者（默认为 true
 */
class LocationTracker constructor(
    private val minTimeBetweenUpdates: Long = 5 * 60 * 1000.toLong(),
    private val minDistanceBetweenUpdates: Float = 100f,
    private val shouldUseGPS: Boolean = true,
    private val shouldUseNetwork: Boolean = true,
    private val shouldUsePassive: Boolean = true
) {
    // LocationManager
    private lateinit var locationManager: LocationManager
    // 最后已知位置
    private var lastKnownLocation: Location? = null
    // LocationManager 的自定义监听器
    private val listener = object : LocationListener {
        override fun onLocationChanged(p0: Location) {
            Location(p0).let { currentLocation ->
                lastKnownLocation = currentLocation
                hasLocationFound = true
                listeners.forEach { l -> l.onLocationFound(currentLocation) }
            }
        }

        override fun onProviderDisabled(provider: String) {
            behaviorListener.forEach { l -> l.onProviderDisabled(provider) }
        }

        override fun onProviderEnabled(provider: String) {
            behaviorListener.forEach { l -> l.onProviderEnabled(provider) }
        }

        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            behaviorListener.forEach { l -> l.onStatusChanged(provider, status, extras) }
        }

    }

    // 用于注册要通知的监听器的列表
    private val listeners: MutableSet<Listener> = mutableSetOf()

    // 用于注册要通知的行为监听器的列表
    private val behaviorListener: MutableSet<BehaviorListener> = mutableSetOf()

    /**
     * 指示 Tracker 是否正在监听更新
     */
    var isListening = false
        private set

    /**
     * 指示 Tracker 是否已找到该位置
     */
    var hasLocationFound = false
        private set

    /**
     * 向堆栈添加一个监听器，以便在找到新位置后通知它
     * @param listener 要添加到列表中的监听器。
     * @return 如果已添加监听器，则为 true，否则为 false
     */
    fun addListener(listener: Listener): Boolean = listeners.add(listener)

    /**
     * 从堆栈中移除一个监听器
     * @param listener 要从列表中删除的监听器。
     * @return 如果监听器已被删除，则为 true，否则为 false
     */
    fun removeListener(listener: Listener): Boolean = listeners.remove(listener)

    /**
     * 向堆栈添加行为监听器，以便在更新提供程序时通知它
     * @param listener 要添加到列表中的监听器。
     * @return 如果已添加监听器，则为 true，否则为 false
     */
    fun addBehaviorListener(listener: BehaviorListener): Boolean = behaviorListener.add(listener)

    /**
     * 从堆栈中删除行为监听器
     * @param listener 要从列表中删除的监听器。
     * @return true 如果监听器已被删除，否则为 false
     */
    fun removeBehaviorListener(listener: BehaviorListener): Boolean = behaviorListener.remove(listener)


    /**
     * 让追踪器监听位置更新
     * @param context  
     */
    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    fun startListening(context: Context) {
        initManagerAndUpdateLastKnownLocation(context)
        if (!isListening) {
            // 收听 GPS 更新
            if (shouldUseGPS) {
                registerForLocationUpdates(LocationManager.GPS_PROVIDER)
            }
            // 监听网络更新
            if (shouldUseNetwork) {
                registerForLocationUpdates(LocationManager.NETWORK_PROVIDER)
            }
            // 监听被动更新
            if (shouldUseNetwork) {
                registerForLocationUpdates(LocationManager.PASSIVE_PROVIDER)
            }
            isListening = true
        }
    }

    /**
     * 使跟踪器停止监听位置更新
     * @param clearListeners 可选（默认为 false）如果设置为 true，则删除所有监听器
     */
    fun stopListening(clearListeners: Boolean = false) {
        if (isListening) {
            locationManager.removeUpdates(listener)
            isListening = false
            if (clearListeners) {
                listeners.clear()
            }
        }
    }

    /**
     * 尽力这么做，如果它不为空，会使用名为 [.lastKnownLocation] 的静态字段调用 [.onLocationChanged]
     * @param context Context
     */
    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    fun quickFix(context: Context) {
        initManagerAndUpdateLastKnownLocation(context)
        lastKnownLocation?.let { lastLocation ->
            listeners.forEach { l -> l.onLocationFound(lastLocation) }
        }
    }

    @SuppressLint("MissingPermission")
    private fun initManagerAndUpdateLastKnownLocation(context: Context) {
        // 初始化管理器
        locationManager =
            context.getSystemService(LocationManager::class.java)
        // 更新 lastKnownLocation
        if (lastKnownLocation == null && shouldUseGPS) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }
        if (lastKnownLocation == null && shouldUseNetwork) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
        if (lastKnownLocation == null && shouldUsePassive) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
        }
    }

    @SuppressLint("MissingPermission")
    private fun registerForLocationUpdates(provider: String) {
        if (locationManager.isProviderEnabled(provider)) {
            locationManager.requestLocationUpdates(provider, minTimeBetweenUpdates, minDistanceBetweenUpdates, listener)
        } else {
            listeners.forEach { l -> l.onProviderError(ProviderError("Provider `$provider` is not enabled")) }
        }
    }

    interface Listener {
        /**
         * 当跟踪器找到位置时调用
         *
         * @param location 找到的位置
         */
        fun onLocationFound(location: Location)

        /**
         * 当特定提供程序出现错误时调用
         * @param providerError 发送的错误
         */
        fun onProviderError(providerError: ProviderError)
    }

    interface BehaviorListener {
        /**
         * @See android.location.LocationListener#onProviderDisabled
         */
        fun onProviderDisabled(provider: String)

        /**
         * @See android.location.LocationListener#onProviderEnabled
         */
        fun onProviderEnabled(provider: String)

        /**
         * @See android.location.LocationListener#onStatusChanged
         */
        fun onStatusChanged(provider: String, status: Int, extras: Bundle)
    }

}
