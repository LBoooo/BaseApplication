package com.hinacle.baseapplication.simple

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.hinacle.base.app.AppActivity
import com.hinacle.base.util.dp
import com.hinacle.base.util.onShakeClickListener
import com.hinacle.base.util.toast.toast
import com.hinacle.base.widget.album.clipPhoto
import com.hinacle.base.widget.album.openAlbum
import com.hinacle.base.widget.album.takePhoto
import com.hinacle.baseapplication.R
import com.hinacle.baseapplication.databinding.ActivityPhotoBinding
import com.hinacle.baseapplication.databinding.ItemPhotoBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import net.mikaelzero.mojito.ext.mojito
import net.mikaelzero.mojito.impl.DefaultPercentProgress
import net.mikaelzero.mojito.impl.NumIndicator
import net.mikaelzero.mojito.interfaces.ActivityCoverLoader
import net.mikaelzero.mojito.interfaces.IMojitoActivity
import kotlin.math.roundToInt

class PhotoActivity : AppActivity(R.layout.activity_photo) {

    private val viewBinding by viewBinding(ActivityPhotoBinding::bind)

    override fun initView() {
        with(viewBinding) {
            val adapter = ItemAdapter<PhotoItem>()
            selectImg.onShakeClickListener {
                openAlbum(builderListener = {
                    it.complexSelector(false, 1, 2)
                }) {
                    if (it.isNotEmpty()) {
                        adapter.add(it.map {
                            PhotoItem(it.path).apply {
                                isVideo = it.type == "video"
                            }
                        })
                    }
                }
            }
            takeImage.onShakeClickListener {
                takePhoto {
                    clipPhoto(it.path){
                        takeImage.load(this)
                    }
                }
            }
            photos.apply {
                this.adapter = FastAdapter.with(adapter).apply {
                    onClickListener = { _, adapter, _, position ->
                        mojito(R.id.image) {
                            urls(adapter.adapterItems.map { it.path })
                            position(position)
                            mojitoListener(
                                onClick = { _, _, _, _ ->
                                    toast { "tap click" }
                                }
                            )
                            setActivityCoverLoader(MaskPhoto(this@PhotoActivity))
                            progressLoader {
                                DefaultPercentProgress()
                            }
                            setIndicator(NumIndicator())
                        }
                        false
                    }
                }
            }
        }
    }

    class MaskPhoto(val context: Context) : ActivityCoverLoader {
        val v = LayoutInflater.from(context).inflate(R.layout.part_mask_photo, null)

        override fun attach(context: IMojitoActivity) {
            v.findViewById<View>(R.id.downloadBtn).onShakeClickListener {
                toast { "点击下载！！！" }
            }
        }

        override fun fingerRelease(isToMax: Boolean, isToMin: Boolean) {

        }

        override fun move(moveX: Float, moveY: Float) {

            val indexLp = v.layoutParams as FrameLayout.LayoutParams
            var currentBottomMargin = (10.dp - moveY / 6f).roundToInt()
            if (currentBottomMargin > 10.dp) {
                currentBottomMargin = 10.dp
            }
            indexLp.bottomMargin = currentBottomMargin
            v.layoutParams = indexLp
        }

        override fun pageChange(totalSize: Int, position: Int) {

        }

        override fun providerView(): View {
            return v
        }

    }

    class PhotoItem(val path: String) : AbstractBindingItem<ItemPhotoBinding>() {
        override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemPhotoBinding {
            return ItemPhotoBinding.inflate(inflater, parent, false)
        }

        var isVideo = false
        override val type: Int = 0

        override fun bindView(binding: ItemPhotoBinding, payloads: List<Any>) {
            binding.path = path

        }
    }
}