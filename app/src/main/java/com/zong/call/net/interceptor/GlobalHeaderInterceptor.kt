package com.zong.call.net.interceptor

import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.request.BaseRequest


/** 演示添加全局请求头/参数 */
class GlobalHeaderInterceptor : RequestInterceptor {

    /** 本方法每次请求发起都会调用, 这里添加的参数可以是动态参数 */
    override fun interceptor(request: BaseRequest) {
        request.setHeader("client", "Android")
//        request.setHeader("token", UserConfig.token)
    }
}