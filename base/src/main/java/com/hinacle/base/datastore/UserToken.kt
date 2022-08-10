package com.hinacle.base.datastore

import kotlinx.serialization.Serializable

@Serializable
open class UserToken() {
    var token: String = ""
}



