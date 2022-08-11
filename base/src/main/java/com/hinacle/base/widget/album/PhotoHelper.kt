package com.hinacle.base.widget.album

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.hinacle.base.app.AppConstant
import com.hinacle.base.util.onStart
import com.huantansheng.easyphotos.Builder.AlbumBuilder
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.callback.SelectCallback
import com.huantansheng.easyphotos.models.album.entity.Photo

/**
 * 到开相册选取图片 如果只选择一张则可以裁剪
 */
inline fun Activity.openAlbum(
    isShowCamera: Boolean = false,
    isUseWidth: Boolean = false,
    noinline builderListener: ((builder: AlbumBuilder) -> Unit)? = null,
    noinline cancelListener: (() -> Unit)? = null,
    crossinline resultListener: (photos: List<Photo>) -> Unit
) {
    val builder = EasyPhotos.createAlbum(this, isShowCamera, isUseWidth, ImageEngine)
    // 使用默认构造配置
    if (builderListener == null) {
        builder.setOriginalMenu(true, true, null) // 现实原图
            .setVideo(false)
            .setGif(true)
            .complexSelector(true, 0, 1)
    } else {
        builderListener.invoke(builder)
    }

    album( builder, cancelListener, resultListener)
}

/**
 * 到开相册选取图片 如果只选择一张则可以裁剪
 */
inline fun Fragment.openAlbum(
    isShowCamera: Boolean = false,
    isUseWidth: Boolean = false,
    noinline builderListener: ((builder: AlbumBuilder) -> Unit)? = null,
    noinline cancelListener: (() -> Unit)? = null,
    crossinline resultListener: (photos: List<Photo>) -> Unit
) {
    val builder = EasyPhotos.createAlbum(this, isShowCamera, isUseWidth, ImageEngine)
    // 使用默认构造配置
    if (builderListener == null) {
        builder.setOriginalMenu(true, true, null) // 现实原图
            .setVideo(false)
            .setGif(true)
            .complexSelector(true, 0, 1)
    } else {
        builderListener.invoke(builder)
    }

    album(builder, cancelListener, resultListener)
}

/**
 * 拍照
 */
inline fun Activity.takePhoto(
    noinline builderListener: ((builder: AlbumBuilder) -> Unit)? = null,
    noinline cancelListener: (() -> Unit)? = null,
    crossinline resultListener: (path: Photo) -> Unit
) {
    val builder = EasyPhotos.createCamera(this, false)
    builderListener?.invoke(builder)
    camera( builder, cancelListener, resultListener)
}

/**
 * 拍照
 */
inline fun Fragment.takePhoto(
    noinline clipListener: ((String) -> Unit)? = null,
    noinline builderListener: ((builder: AlbumBuilder) -> Unit)? = null,
    noinline cancelListener: (() -> Unit)? = null,
    crossinline resultListener: (path: Photo) -> Unit
) {
    val builder = EasyPhotos.createCamera(this, false)
    builderListener?.invoke(builder)
    camera( builder, cancelListener, resultListener)
}

inline fun album(
    builder: AlbumBuilder,
    noinline cancelListener: (() -> Unit)? = null,
    crossinline resultListener: (photos: List<Photo>) -> Unit
) {
    builder.setFileProviderAuthority(AppConstant.providerPath).start(
        object : SelectCallback() {
            override fun onResult(photos: ArrayList<Photo>?, isOriginal: Boolean) {
                if (!photos.isNullOrEmpty()) {
                    resultListener.invoke(photos)
                }
            }

            override fun onCancel() {
                cancelListener?.invoke()
            }
        })
}

inline fun camera(
    builder: AlbumBuilder,
    noinline cancelListener: (() -> Unit)? = null,
    crossinline resultListener: (path: Photo) -> Unit
) {
    builder.setFileProviderAuthority(AppConstant.providerPath).start(
        object : SelectCallback() {
            override fun onResult(photos: ArrayList<Photo>?, isOriginal: Boolean) {
                if (!photos.isNullOrEmpty()) {
                    resultListener.invoke(photos[0])
                }
            }

            override fun onCancel() {
                cancelListener?.invoke()
            }
        }
    )
}

inline fun Activity.clipPhoto(path: String, crossinline block: String.() -> Unit) {
    ClipListener.clipListener = {
        it.block()
        ClipListener.clipListener = null
    }
    onStart<ClipActivity> {
        putExtra(AppConstant.clipPath, path)
    }
}

inline fun Fragment.clipPhoto(path: String, crossinline block: String.() -> Unit) {
    ClipListener.clipListener = {
        it.block()
        ClipListener.clipListener = null
    }
    requireContext().onStart<ClipActivity> {
        putExtra(AppConstant.clipPath, path)
    }
}