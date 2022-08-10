package com.hinacle.baseapplication.data


import com.hinacle.base.datastore.UserToken
import com.hinacle.base.datastore.editUserInfo
import com.hinacle.base.datastore.queryUserInfo
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class User : UserToken() {
    var id: String = ""
    var name: String = ""
}


val currentUser get() = queryUserInfo<User>()
fun logOut() = editUserInfo<User> { null }