package com.hinacle.base.util.rxbus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Lifecycle
import com.hinacle.base.util.logcat.logcat
import com.trello.lifecycle4.android.lifecycle.AndroidLifecycle
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlin.jvm.Volatile
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import java.util.concurrent.ConcurrentHashMap


class RxBus private constructor() {
    val mBus: Subject<Any> = PublishSubject.create<Any>().toSerialized()
    val mStickyEventMap: MutableMap<Class<*>, Any> by lazy {
        ConcurrentHashMap()
    }

    /**
     * 发送事件
     */
    fun post(event: Any) {
        mBus.onNext(event)
    }

    inline fun <reified T : Any>  toObservable(owner: LifecycleOwner? ): Observable<T> {
        return toObservable(owner,  Lifecycle.Event.ON_DESTROY)
    }

    /**
     * 使用RxLifecycle解决RxJava引起的内存泄漏
     */
    inline fun <reified T : Any> toObservable(owner: LifecycleOwner? , event: Lifecycle.Event): Observable<T> {
        val provider = AndroidLifecycle.createLifecycleProvider(owner)
        return mBus.ofType(T::class.java)
            .doOnDispose { logcat { "RxBus取消订阅" } }
            .compose(provider.bindUntilEvent(event))
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 判断是否有订阅者
     */
    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }

    @Suppress("unUsed")
    fun reset() {
        mDefaultInstance = null
    }
    /**
     * Sticky 相关
     */

    /**
     * 发送一个新Sticky事件
     */
    fun postSticky(event: Any) {
        synchronized(mStickyEventMap) {
            mStickyEventMap.put(event.javaClass, event)
        }
        post(event)
    }

    inline fun <reified T : Any> toObservableSticky(owner: LifecycleOwner?): Observable<T> {
        return toObservableSticky(owner, Lifecycle.Event.ON_DESTROY)
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     * 使用RxLifecycle解决RxJava引起的内存泄漏
     */
    inline fun <reified T : Any> toObservableSticky(owner: LifecycleOwner?, e: Lifecycle.Event): Observable<T> {
        synchronized(mStickyEventMap) {
            val provider = AndroidLifecycle.createLifecycleProvider(owner)
            val observable = mBus.ofType(T::class.java)
                .doOnDispose { logcat { "RxBus取消订阅" } }
                .compose(provider.bindUntilEvent(e))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
            val event = mStickyEventMap[T::class.java]
            return if (event != null) {
                observable.mergeWith(Observable.create { subscriber -> (event as? T)?.let { subscriber.onNext(it) } })
            } else {
                observable
            }
        }
    }

    /**
     * 根据eventType获取Sticky事件
     */
    @Suppress("unUsed")
    inline fun <reified T : Any> getStickyEvent(): T? {
        synchronized(mStickyEventMap) {
            return mStickyEventMap[T::class.java] as? T
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    inline fun <reified T : Any> removeStickyEvent(){
        synchronized(mStickyEventMap) {
            mStickyEventMap.remove(T::class.java)
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    fun removeAllStickyEvents() {
        synchronized(mStickyEventMap) { mStickyEventMap.clear() }
    }

    companion object {
        @Volatile
        private var mDefaultInstance: RxBus? = null

        @JvmStatic
        fun getInstance(): RxBus {
            return mDefaultInstance ?: synchronized(RxBus::class.java) {
                mDefaultInstance ?: RxBus().also { mDefaultInstance = it }
            }
        }
    }


}