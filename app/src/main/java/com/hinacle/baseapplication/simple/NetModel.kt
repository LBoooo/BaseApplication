package com.hinacle.baseapplication.simple

import com.hinacle.base.vm.AppModel
import com.hinacle.baseapplication.ApiServices

class NetModel : AppModel() {

    // get 请求 param:参数
    suspend fun getRequest(param: String) = getMapper<String>(ApiServices.requestUrl) {
        // 添加请求参数
        param("param", param)

    }

    // post 请求更多功能查看
    suspend fun postRequest() = postMapper<String>(ApiServices.requestUrl){

//        this.json()
//        this.param()
//        this.body
    }

}