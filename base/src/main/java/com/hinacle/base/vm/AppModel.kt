package com.hinacle.base.vm

import com.drake.net.Get
import com.drake.net.Post
import com.drake.net.request.BodyRequest
import com.drake.net.request.UrlRequest
import kotlinx.coroutines.coroutineScope

open class AppModel : IModel {

    suspend inline fun <reified T> getMapper(
        path: String,
        tag: Any? = null,
        noinline block: (UrlRequest.() -> Unit)? = null
    ) = coroutineScope {
        Get<T>(path, tag, block).await()
    }

    suspend inline fun <reified T> postMapper(
        path: String,
        tag: Any? = null,
        noinline block: (BodyRequest.() -> Unit)? = null
    ) = coroutineScope {
        Post<T>(path, tag, block).await()
    }


    override fun onCleared() {

    }
}