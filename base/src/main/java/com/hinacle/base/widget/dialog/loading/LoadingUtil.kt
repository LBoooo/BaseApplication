package com.hinacle.base.widget.dialog.loading

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.hinacle.appdialog.AppDialog
import com.hinacle.appdialog.extensions.addShowDismissListener
import com.hinacle.appdialog.extensions.bindingListenerFun
import com.hinacle.appdialog.extensions.newAppDialog
import com.hinacle.base.R
import com.hinacle.base.app.AppActivity
import com.hinacle.base.app.AppFragment
import com.hinacle.base.databinding.DialogLoadingBinding
import com.hinacle.base.util.dp
import com.hinacle.base.util.rotation
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface ActivityLoadingProperty : ReadOnlyProperty<AppActivity, LoadingDialogHelper>
interface FragmentLoadingProperty : ReadOnlyProperty<AppFragment, LoadingDialogHelper>

class ActivityLazyLoadingProperty : ActivityLoadingProperty {
    var helper: LoadingDialogHelper? = null
    override fun getValue(thisRef: AppActivity, property: KProperty<*>): LoadingDialogHelper {
        if (helper == null) {
            helper = LoadingDialogHelper(thisRef.supportFragmentManager)
            thisRef.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    thisRef.lifecycle.removeObserver(this)
                    helper?.releaseLoading()
                    helper = null
                }
            })
        }
        return helper!!
    }
}

class FragmentLazyLoadingProperty : FragmentLoadingProperty {
    override fun getValue(thisRef: AppFragment, property: KProperty<*>): LoadingDialogHelper {
        return LoadingDialogHelper(thisRef.childFragmentManager)
    }

}

fun AppActivity.loadingDialog(): ActivityLoadingProperty {
    return ActivityLazyLoadingProperty()
}

fun AppFragment.loadingDialog(): FragmentLoadingProperty {
    return FragmentLazyLoadingProperty()
}

class LoadingDialogHelper(private val fm: FragmentManager) {

    var loadingDialog: AppDialog? = null

    fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = newAppDialog {
                layoutId = R.layout.dialog_loading
                width = 80.dp
                height = 80.dp
                unLeak = true
                var animator: ValueAnimator? = null
                bindingListenerFun(DialogLoadingBinding::class) { vb, dialog ->
                    animator = ObjectAnimator.ofFloat(vb.loadingIv, "rotation", 0f, 360f).apply {
                        this.duration = 2000L
                        repeatCount = ValueAnimator.INFINITE
                        interpolator = LinearInterpolator()
                    }
                }

                addShowDismissListener("") {
                    onAddDialogDismiss {
                        animator?.cancel()
                    }
                    onAddDialogShow {
                        animator?.start()
                    }
                }
            }
        }
        loadingDialog?.showOnWindow(fm)
    }

    fun dismissLoading() {
        loadingDialog?.dismiss()
    }

    fun releaseLoading() {

    }
}