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
    noinline clipListener: ((String) -> Unit)? = null,
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

    album(this, builder, clipListener, cancelListener, resultListener)
}

/**
 * 到开相册选取图片 如果只选择一张则可以裁剪
 */
inline fun Fragment.openAlbum(
    isShowCamera: Boolean = false,
    isUseWidth: Boolean = false,
    noinline builderListener: ((builder: AlbumBuilder) -> Unit)? = null,
    noinline cancelListener: (() -> Unit)? = null,
    noinline clipListener: ((String) -> Unit)? = null,
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

    album(requireContext(), builder, clipListener, cancelListener, resultListener)
}

/**
 * 拍照
 */
inline fun Activity.takePhoto(
    noinline builderListener: ((builder: AlbumBuilder) -> Unit)? = null,
    noinline cancelListener: (() -> Unit)? = null,
    noinline clipListener: ((String) -> Unit)? = null,
    crossinline resultListener: (path: Photo) -> Unit
) {
    val builder = EasyPhotos.createCamera(this, false)
    builderListener?.invoke(builder)
    camera(this, builder, clipListener, cancelListener, resultListener)
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
    camera(requireContext(), builder, clipListener, cancelListener, resultListener)
}

inline fun album(
    context: Context,
    builder: AlbumBuilder,
    noinline clipListener: ((String) -> Unit)? = null,
    noinline cancelListener: (() -> Unit)? = null,
    crossinline resultListener: (photos: List<Photo>) -> Unit
) {
    builder.setFileProviderAuthority(AppConstant.providerPath).start(
        object : SelectCallback() {
            override fun onResult(photos: ArrayList<Photo>?, isOriginal: Boolean) {
                if (!photos.isNullOrEmpty()) {
                    if (photos.size == 1) {
                        if (clipListener != null) {
                            ClipListener.clipListener = { path ->
                                clipListener.invoke(path)
                                ClipListener.clipListener = null
                            }
                            context.onStart<ClipActivity> {
                                putExtra(AppConstant.clipPath, photos[0].path)
                            }
                        }
                    } else {
                        resultListener.invoke(photos)
                    }
                }
            }

            override fun onCancel() {
                cancelListener?.invoke()
            }
        })
}

inline fun camera(
    context: Context,
    builder: AlbumBuilder,
    noinline clipListener: ((String) -> Unit)? = null,
    noinline cancelListener: (() -> Unit)? = null,
    crossinline resultListener: (path: Photo) -> Unit
) {
    builder.setFileProviderAuthority(AppConstant.providerPath).start(
        object : SelectCallback() {
            override fun onResult(photos: ArrayList<Photo>?, isOriginal: Boolean) {
                if (!photos.isNullOrEmpty()) {
                    if (clipListener != null) {
                        ClipListener.clipListener = { path ->
                            clipListener.invoke(path)
                            ClipListener.clipListener = null
                        }
                        context.onStart<ClipActivity> {
                            putExtra(AppConstant.clipPath, photos[0].path)
                        }
                    } else {
                        resultListener.invoke(photos[0])
                    }
                }
            }

            override fun onCancel() {
                cancelListener?.invoke()
            }
        }
    )
}