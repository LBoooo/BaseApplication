package com.hinacle.baseapplication.simple

import by.kirich1409.viewbindingdelegate.viewBinding
import com.hinacle.base.app.AppActivity
import com.hinacle.base.datastore.getDataStore
import com.hinacle.base.datastore.saveDataStore
import com.hinacle.base.util.*
import com.hinacle.base.util.logcat.logcat
import com.hinacle.base.util.rxbus.receiveSticky
import com.hinacle.base.util.toast.toast
import com.hinacle.baseapplication.R
import com.hinacle.baseapplication.databinding.ActivityOtherBinding

class OtherActivity : AppActivity(R.layout.activity_other) {
    private val viewBinding by viewBinding(ActivityOtherBinding::bind)
    override fun initView() {
        super.initView()
        // 获取资源文件 ResourceUtil
        // 正则校验 RegexUtilx
        // 防止重复点击 ViewUtil
        // 启动activity LunchUtil onStart ,带返回参数 onStartForResultLaunch
        // 数值尺寸转换 DensityUtil
        // 键盘工具 KeyboardUtil
        // 事件总线 RxBus
        // 圆角layout和view widget-> corner
        // dialog 使用newAppDialog扩展函数
        // 加载图片 imageview.load扩展函数 入参Any 详情可见coil官网
        // 适配方案 manifests配置基础尺寸自动适配
        // 图片选择


        // 可选用组件
        // 导航组件 Kotlin
        // api("androidx.navigation:navigation-fragment-ktx:2.5.1")
        // api("androidx.navigation:navigation-ui-ktx:2.5.1")


        with(viewBinding) {
            // 防止重复点击 interval 间隔时间
            copyBtn.onShakeClickListener(500) {
                // 复制
                val isCopy = clipPlainText(exampleTv.text, "kotlin")
                toast { "copy text : $isCopy" }
            }

            intentBtn.onShakeClickListener {
                // 启动一个新的activity ResultActivity:目标activity
//                onStart<ResultActivity>()
//                onStart<ResultActivity>{
//                    putExtra("id","123")
//                    putExtra("asd",false)
//                    putExtra("type",0)
//                }

                // 启动一个activity 带返回值 ResultActivity:目标activity 可携带参数跳转
                launch.launch<ResultActivity>(this@OtherActivity)

            }
            dataStoreBtn.onShakeClickListener {
                getDataStore<String>("name"){
                    logcat { "name-->>$this" }
                }
                getDataStore<Int>("id"){
                    logcat { "id-->>$this" }
                }
                getDataStore<Boolean>("isLogin"){
                    logcat { "isLogin-->>$this" }
                }
                getDataStore<Long>("time"){
                    logcat { "time-->>$this" }
                }
                getDataStore<Float>("money"){
                    logcat { "money-->>$this" }
                }
                getDataStore<Set<String>>("tag"){
                    logcat { "tag-->>${this.toString()}" }
                }
            }
        }
    }

    // 注册一个带返回的跳转事件 必须在开始之前调用
    private val launch = onStartForResultLaunch { code, data ->
        toast { "$code -->> $data" }
    }

    override fun superOnCreate() {
//        RxBus.getInstance().toObservableSticky<String>(this).subscribe {
//            toast { it }
//        }
        receiveSticky<String> {
            toast { this }
            saveDataStore {
                mapOf(
                    "name" to "asdasd",
                    "id" to 1234,
                    "isLogin" to true,
                    "time" to currentTimeMillis,
                    "money" to 123.55f,
                    "tag" to setOf<String>("java", "kotlin", "android")
                )
            }
        }
    }


}