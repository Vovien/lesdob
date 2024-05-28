package com.tubewiki.mine.view.login.model

import com.tubewiki.mine.api.MineApi
import com.tubewiki.mine.base.MineViewModel

class OneKeyLoginViewModel : MineViewModel() {

    fun quickLogin(token: String) = launchWithFlow({
        MineApi.quickLogin(token)
    })


    /**
     * 微信登录
     */
    fun wxLoginCheck( openid: String, username: String, avatar: String) = launchWithFlow({
        MineApi.auth("", "3", "",openid,username,avatar)
    })

}