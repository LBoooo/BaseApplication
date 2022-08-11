package com.hinacle.base.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.shimmer.Shimmer
import com.hinacle.base.R
import com.hinacle.base.app.AppApplication
import com.hinacle.base.datastore.DataStoreUtils
import com.hinacle.base.widget.statelayout.StateConfig
import com.hinacle.base.widget.statelayout.handler.FadeStateChangedHandler
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import koleton.SkeletonLoader
import koleton.SkeletonLoaderFactory
import net.mikaelzero.coilimageloader.CoilImageLoader
import net.mikaelzero.mojito.Mojito
import net.mikaelzero.mojito.view.sketch.SketchImageLoadFactory

// 整个model的配置依赖工具类 全局context
object AppUtil : SkeletonLoaderFactory {

    val application: AppApplication by lazy {
        AppApplication.instance()
    }

    fun init(context: Context) {
        // 初始化datastore
        DataStoreUtils.init(context)
        // 初始化图片/视频 查看器
        Mojito.initialize(
            CoilImageLoader.with(context),
            SketchImageLoadFactory()
        )

        StateConfig.apply {
            emptyLayout = R.layout.layout_empty
            errorLayout = R.layout.layout_error
            loadingLayout = R.layout.layout_loading
            stateChangedHandler = FadeStateChangedHandler()
            setRetryIds(R.id.retryId)
            onError {

            }
        }
    }

    // 骨架屏 初始化 如果不用可删除 删除后使用默认
    override fun newSkeletonLoader(): SkeletonLoader {
        return SkeletonLoader.Builder(application)
            .color(R.color.app_skeleton)
            .cornerRadius(getResDimenDp(R.dimen.app_skelet_radius).toFloat())
            .lineSpacing(getResDimenDp(R.dimen.app_skelet_line_spacing).toFloat())
            .itemCount(10)
            .shimmer(true)
            .shimmer(getCustomShimmer())
            .build()
    }

    private fun getCustomShimmer(): Shimmer {
        return Shimmer.AlphaHighlightBuilder()
            .setDuration(800)
            .setBaseAlpha(0.5f)
            .setHighlightAlpha(0.9f)
            .setWidthRatio(1f)
            .setHeightRatio(1f)
            .setDropoff(1f)
            .build()
    }

    init {
        //启用矢量图兼容 配置刷新加载库
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer { _, layout ->
            //全局设置（优先级最低）
            with(layout) {
                setEnableAutoLoadMore(true)
                setEnableOverScrollDrag(false)
                setEnableOverScrollBounce(true)
                setEnableLoadMoreWhenContentNotFull(true)
                setEnableScrollContentWhenRefreshed(true)
//                setPrimaryColorsId(R.color.app_blue, R.color.app_white)
                setFooterMaxDragRate(4f)
                setFooterHeight(45f)
            }
        }

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            //全局设置主题颜色（优先级第二低，可以覆盖 DefaultRefreshInitializer 的配置，与下面的ClassicsHeader绑定）
            layout.setEnableHeaderTranslationContent(true)
            ClassicsHeader(context) //.setTimeFormat(DynamicTimeFormat("更新于 %s"))
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }
    }
}