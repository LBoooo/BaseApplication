package com.hinacle.base.data

@kotlinx.serialization.Serializable
data class Response<T>(
    val date: Long = 0,
    var data: T? = null,
    val message: String = ""
)