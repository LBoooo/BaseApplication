package com.hinacle.baseapplication.simple

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hinacle.base.app.AppActivity
import com.hinacle.base.util.location.getAddress
import com.hinacle.base.util.location.getLocation
import com.hinacle.base.util.location.registerLocation
import com.hinacle.base.util.logcat.logcat
import com.hinacle.base.util.onShakeClickListener
import com.hinacle.base.util.toast.toast
import com.hinacle.base.widget.dialog.PermissionsRationaleDialog
import com.hinacle.baseapplication.R
import com.hinacle.baseapplication.databinding.ActivityPermissionsBinding
import permissions.dispatcher.*


@RuntimePermissions
class PermissionsActivity : AppActivity(R.layout.activity_permissions) {

    private val viewBinding by viewBinding(ActivityPermissionsBinding::bind)

    override fun initView() {
        with(viewBinding) {
            requestPermissionBtn.onShakeClickListener {
                //   PermissionsRationaleDialog(this,"定位权限使用说明","申请位置权限是为了向您提供基于您的位置信息附近的优质内容").show(supportFragmentManager)
                havePermissionsWithPermissionCheck()
            }
        }
    }


    // 注册监听 持续回调
    private val locationRegister = registerLocation {
        logcat { "=============${longitude} -- ${latitude}" }
    }



    //注解	必填	描述
    //@RuntimePermissions	✓	注册 或 以处理权限ActivityFragment
    //@NeedsPermission	✓	注释执行需要一个或多个权限的操作的方法
    //@OnShowRationale		注释一个方法，该方法解释了为什么需要权限。它传入一个对象，该对象可用于在用户输入时继续或中止当前权限请求。如果不为方法指定任何参数，编译器将生成 和 。您可以使用这些方法来代替（例如：使用PermissionRequestprocess${NeedsPermissionMethodName}ProcessRequestcancel${NeedsPermissionMethodName}ProcessRequestPermissionRequestDialogFragment)
    //@OnPermissionDenied		注释在用户未授予权限时调用的方法
    //@OnNeverAskAgain		注释一个方法，如果用户选择让设备“不再询问”有关权限，则会调用该方法

    @SuppressLint("MissingPermission")
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun havePermissions() {
        toast { "已获取定位权限" }


        // 开始监听
//        locationRegister.startListening(this)
        // 停止监听
//        locationRegister.stopListening()

        // 只监听一次
        getLocation {
             logcat { "-=-=-=-=-=-=- ${longitude} -- ${latitude}" }
             getAddress{
                 toast { it.getAddressLine(0) }
             }
        }
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showRationaleForPermissions() {
        toast { "再次弹窗说明为什么获取定位权限，并显示确定和取消按钮" }
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun unHavePermissions() {
        toast { "已禁止获取定位权限" }
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    fun neverAskAgain() {
        toast { "已禁止获取定位权限,并不再询问" }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}