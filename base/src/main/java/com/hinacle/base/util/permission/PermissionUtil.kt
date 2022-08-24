package com.hinacle.base.util

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hinacle.base.util.permission.Permission


const val DENIED = "DENIED"
const val EXPLAINED = "EXPLAINED"

/**
 * [permission] 权限名称
 * [granted] 申请成功
 * [denied] 被拒绝且未勾选不再询问
 * [explained] 被拒绝且勾选不再询问
 */
//inline fun AppCompatActivity.requestPermission(
//    permission: String,
//    crossinline denied: (permission: String) -> Unit = {},
//    crossinline explained: (permission: String) -> Unit = {},
//    crossinline granted: (permission: String) -> Unit = {},
//) {
//    registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
//        when {
//            result -> granted.invoke(permission)
//            shouldShowRequestPermissionRationale(permission) -> denied.invoke(permission)
//            else -> explained.invoke(permission)
//        }
//    }.launch(permission)
//}

internal var _denied: (permission: String) -> Unit = {}
  var _explained: (permission: String) -> Unit = {}
  var _granted: (permission: String) -> Unit = {}

fun AppCompatActivity.permissionLaunch(permission: String): ActivityResultLauncher<String> {
    return registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        when {
            result -> _granted.invoke(permission)
            shouldShowRequestPermissionRationale(permission) -> _denied.invoke(permission)
            else -> _explained.invoke(permission)
        }
    }
}

val AppCompatActivity.permissionLaunch
    get() = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->

        val annotation = this.javaClass.annotations
        val ap = annotation.find { it is Permission } as? Permission
        val permission = ap?.permission?:return@registerForActivityResult

        when {
            result -> _granted.invoke(permission)
            shouldShowRequestPermissionRationale(permission) -> _denied.invoke(permission)
            else -> _explained.invoke(permission)
        }
    }

@MainThread
inline fun AppCompatActivity.requestPermission(
    noinline denied: (permission: String) -> Unit = {},
    explained: (permission: String) -> Unit = {},
    granted: (permission: String) -> Unit = {},
) {
    val annotation = this.javaClass.annotations
    val ap = annotation.find { it is Permission } as? Permission
    val permission = ap?.permission?:return
    permissionLaunch.launch(permission)
}


/**
 * [permissions] 权限数组
 * [allGranted] 所有权限均申请成功
 * [denied] 被拒绝且未勾选不再询问，同时被拒绝且未勾选不再询问的权限列表
 * [explained] 被拒绝且勾选不再询问，同时被拒绝且勾选不再询问的权限列表
 */
inline fun AppCompatActivity.requestMultiplePermissions(
    permissions: Array<String>,
    crossinline allGranted: () -> Unit = {},
    crossinline denied: (List<String>) -> Unit = {},
    crossinline explained: (List<String>) -> Unit = {}
) {
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result: Map<String, Boolean> ->
        //过滤 value 为 false 的元素并转换为 list
        val deniedList = result.filter { !it.value }.map { it.key }
        when {
            deniedList.isNotEmpty() -> {
                //对被拒绝全选列表进行分组，分组条件为是否勾选不再询问
                val map = deniedList.groupBy { permission ->
                    if (shouldShowRequestPermissionRationale(permission)) DENIED else EXPLAINED
                }
                //被拒接且没勾选不再询问
                map[DENIED]?.let { denied.invoke(it) }
                //被拒接且勾选不再询问
                map[EXPLAINED]?.let { explained.invoke(it) }
            }
            else -> allGranted.invoke()
        }
    }.launch(permissions)
}


/**
 * [permission] 权限名称
 * [granted] 申请成功
 * [denied] 被拒绝且未勾选不再询问
 * [explained] 被拒绝且勾选不再询问
 */
inline fun Fragment.requestPermission(
    permission: String,
    crossinline granted: (permission: String) -> Unit = {},
    crossinline denied: (permission: String) -> Unit = {},
    crossinline explained: (permission: String) -> Unit = {}

) {
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        when {
            result -> granted.invoke(permission)
            shouldShowRequestPermissionRationale(permission) -> denied.invoke(permission)
            else -> explained.invoke(permission)
        }
    }.launch(permission)
}

/**
 * [permissions] 权限数组
 * [allGranted] 所有权限均申请成功
 * [denied] 被拒绝且未勾选不再询问，同时被拒绝且未勾选不再询问的权限列表
 * [explained] 被拒绝且勾选不再询问，同时被拒绝且勾选不再询问的权限列表
 */
inline fun Fragment.requestMultiplePermissions(
    permissions: Array<String>,
    crossinline allGranted: () -> Unit = {},
    crossinline denied: (List<String>) -> Unit = {},
    crossinline explained: (List<String>) -> Unit = {}
) {
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result: Map<String, Boolean> ->
        //过滤 value 为 false 的元素并转换为 list
        val deniedList = result.filter { !it.value }.map { it.key }
        when {
            deniedList.isNotEmpty() -> {
                //对被拒绝全选列表进行分组，分组条件为是否勾选不再询问
                val map = deniedList.groupBy { permission ->
                    if (shouldShowRequestPermissionRationale(permission)) DENIED else EXPLAINED
                }
                //被拒接且没勾选不再询问
                map[DENIED]?.let { denied.invoke(it) }
                //被拒接且勾选不再询问
                map[EXPLAINED]?.let { explained.invoke(it) }
            }
            else -> allGranted.invoke()
        }
    }.launch(permissions)
}