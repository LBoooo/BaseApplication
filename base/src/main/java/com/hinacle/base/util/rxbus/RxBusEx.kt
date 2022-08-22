package com.hinacle.base.util.rxbus

import androidx.lifecycle.LifecycleOwner

/**
 * RxBus发送普通事件
 */
inline fun <reified T : Any> post(block: () -> T) {
    RxBus.getInstance().post(block.invoke())
}

/**
 * RxBus发送粘性事件
 */
inline fun <reified T : Any> postSticky(block: () -> T) {
    RxBus.getInstance().postSticky(block.invoke())
}

/**
 * RxBus接收事件
 */
inline fun <reified T : Any> LifecycleOwner.receive(crossinline block: T.() -> Unit) {
    RxBus.getInstance().toObservable<T>(this).subscribe {
        it.block()
    }
}

/**
 * RxBus接收粘性事件
 */
inline fun <reified T : Any> LifecycleOwner.receiveSticky(crossinline block: T.() -> Unit) {
    RxBus.getInstance().toObservableSticky<T>(this).subscribe {
        it.block()
    }
}

/**
 * 移除指定的Sticky事件
 */
inline fun <reified T : Any> removeSticky() {
    RxBus.getInstance().removeStickyEvent<T>()
}

/**
 * 移除所有Sticky事件
 */
fun removeAllSticky() {
    RxBus.getInstance().removeAllStickyEvents()
}

/**
 * 是否有接收者
 */
inline val isHaveReceiver get() = RxBus.getInstance().hasObservers()

/**
 * 释放rxBus
 */
fun resetRxbus(){
    RxBus.getInstance().reset()
}