package com.hinacle.base.datastore

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

inline fun <reified U : UserToken> editUserInfo(block: (U?) -> U?) {
    val u = json.encodeToString(block.invoke(queryUserInfo<U>()))
    DataStoreUtils.putString("USER", u)
}

inline fun <reified U : UserToken> queryUserInfo(): U? {
    val u = DataStoreUtils.getString("USER")
    if (u.isNullOrEmpty())
        return null
    return json.decodeFromString(u)
}