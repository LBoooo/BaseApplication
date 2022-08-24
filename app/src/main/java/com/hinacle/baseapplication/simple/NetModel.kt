package com.hinacle.baseapplication.simple

import com.hinacle.base.util.logcat.logcat
import com.hinacle.base.vm.AppModel
import com.hinacle.baseapplication.ApiServices
import kotlin.math.min


class NetModel : AppModel() {

    // get 请求 param:参数
    suspend fun getRequest(param: String) = getMapper<String>(ApiServices.requestUrl) {
        // 添加请求参数
        param("param", param)

    }

    // post 请求更多功能查看
    suspend fun postRequest() = postMapper<String>(ApiServices.requestUrl) {

//        this.json()
//        this.param()
//        this.body
    }


    suspend fun getPagingData(page: Int, step: Int = 2): List<String> {
        logcat { "获取第${page}页的数据" }
        if (page * step >= data.size) {
            return emptyList()
        }
        if (page == 3) {
            throw Exception("抛出异常")
        }
        return data.slice(page * step until min((page + 1) * step, data.size))
    }

    val data = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
}