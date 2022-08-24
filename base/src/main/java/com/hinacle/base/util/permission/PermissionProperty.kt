package com.hinacle.base.util.permission

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import com.hinacle.base.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


interface PermissionProperty : ReadOnlyProperty<AppCompatActivity, PermissionOperator>

class LazyPermissionProperty(private val permissions: Array<String>) : PermissionProperty {
    override fun getValue(
        thisRef: AppCompatActivity,
        property: KProperty<*>
    ): PermissionOperator {
        return PermissionOperator(thisRef, permissions)
    }
}

fun permission(vararg permissions: String): PermissionProperty {
    return LazyPermissionProperty(permissions.asList().toTypedArray())
}

class PermissionOperator(
    private val ac: AppCompatActivity,
    private val permissions: Array<String>
) {

    private val DENIED = "DENIED"
    private val EXPLAINED = "EXPLAINED"
    private val permissionLaunch =
        ac.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result: Map<String, Boolean> ->
            //过滤 value 为 false 的元素并转换为 list
            val deniedList = result.filter { !it.value }.map { it.key }
            when {
                deniedList.isNotEmpty() -> {
                    //对被拒绝全选列表进行分组，分组条件为是否勾选不再询问
                    val map = deniedList.groupBy { permission ->
                        if (ac.shouldShowRequestPermissionRationale(permission)) DENIED else EXPLAINED
                    }
                    //被拒接且没勾选不再询问
                    map[DENIED]?.let { denied?.invoke(it) }
                    //被拒接且勾选不再询问
                    map[EXPLAINED]?.let { explained?.invoke(it) }
                }
                else -> allGranted.invoke()
            }
        }
    private var allGranted: () -> Unit = {}
    var denied: ((List<String>) -> Unit)? = null
    var explained: ((List<String>) -> Unit)? = null

    fun requestPermission(granted: () -> Unit) {
        allGranted = granted
        permissionLaunch.launch(permissions)
    }
}
