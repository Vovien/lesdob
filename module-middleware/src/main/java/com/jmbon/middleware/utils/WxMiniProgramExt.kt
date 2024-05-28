package com.jmbon.middleware.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.apkdv.mvvmfast.ktx.launchWithFlow
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.api.API
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

const val WX_API_ID: String = "wx3dca91f34e5559d9"
const val MINI_PROGRAM_ID = "gh_2354c3b240a0"

/**
 * 跳转到微信小程序
 * @date 2023/8/25 17:12
 * @param context
 * @type 类型[1: 邀您入群，2: 获取案例，3: 获取生育方案，4:预约医生，5: 预约医院]
 */
fun toWxMiniProgram(context: Context, type: Int = 2, groupName: String = "", pregnantStatus: Int = 0) {
    toWxMiniProgram(context, type.toString(), groupName, pregnantStatus)
}

fun toWxMiniProgram(context: Context, type: String = "", groupName: String = "", pregnantStatus: Int = 0) {
    MainScope().launch {
        launchWithFlow({
            API.getMinProgramConfig()
        }, {
            realToWxMiniProgram(context, type, groupName)
        }).next {
            realToWxMiniProgram(context, type, groupName, wechat_mini_program_id, path, pregnantStatus)
        }
    }
}

private fun realToWxMiniProgram(context: Context, type: String = "", groupName: String = "", minProgramId: String = MINI_PROGRAM_ID, path: String = "pages/conversion/conversion", pregnantStatus: Int = 0) {
    val newGroupName = groupName.addLastChar("群")
    val uriString = if (newGroupName.isNotEmpty()) Uri.encode(newGroupName) else ""
    val realPath = (if (path.contains("?")) {
        "&"
    } else {
        "?"
    }).run {
        // pregnancy_type: 0:默认，1：我要试管，2：我要怀孕，3：我要保胎，4：我要育儿, 当前暂无状态, 故默认为0
        path + "$this" + "app_type=5&pregnancy_type=${pregnantStatus}&type=${type}&group_name=${uriString}"
    }
    val req = WXLaunchMiniProgram.Req()
    req.userName = minProgramId
    Log.e("MiniProgramPath", realPath)
    req.path = realPath //拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE // 可选打开 开发版，体验版和正式版
    WxMiniProgram.createWx(context)?.sendReq(req)
}

fun String.addLastChar(newStr: String): String {
    return if (this.isNotEmpty()) {
        if (this.last().toString() == newStr) {
            this
        } else {
            this + newStr
        }
    } else ""
}


object WxMiniProgram {
    private var wxAPI: IWXAPI? = null
    fun createWx(context: Context): IWXAPI? {
        if (wxAPI != null) {
            return wxAPI!!
        }
        val createWXAPI = WXAPIFactory.createWXAPI(context, WX_API_ID)
        return if (createWXAPI.isWXAppInstalled) {
            wxAPI = createWXAPI
            wxAPI
        } else {
            "请您先安装微信".showToast()
            null
        }
    }
}