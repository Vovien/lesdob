package com.jmbon.middleware.config

import com.blankj.utilcode.util.Utils
import com.mob.MobSDK


class ShareSDKInit {
    init {
        MobSDK.submitPolicyGrantResult(true,null)
        MobSDK.init(Utils.getApp(),"28aa50d979349","38de8ab0c8ff2af8678a0ea421639eee")
    }

}