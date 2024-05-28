package com.jmbon.middleware.utils

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.arouter.MiniProgramService

/**
 * @author : leimg
 * time   : 2022/4/8
 * desc   :网页href跳转判断
 * version: 1.0
 */
object H5ArouterUtils {
    private val miniProgram by lazy {
        ARouter.getInstance().build("/miniprogram/start/service").navigation() as MiniProgramService
    }

    fun urlProcessing(baseUrl: String) {
        val url = baseUrl.replace(BuildConfig.H5_URL, "")
        when {
            url.contains(Regex("^/question/[1-9]")) -> {
                val data = url.split("/")
                val id = data[data.size - 1].toInt()
                ARouter.getInstance().build("/question/activity/ask_detail")
                    .withInt(TagConstant.QUESTION_ID, id)
                    .navigation()
            }
            url.contains(Regex("^/article/[1-9]")) -> {
                val data = url.split("/")
                val id = data[data.size - 1].toInt()
                ArouterUtils.toArticleDetailsActivity(id)
            }

            url.contains(Regex("^/topic/[1-9]")) -> {
                val data = url.split("/")
                val id = data[data.size - 1].toInt()
                ARouter.getInstance().build("/home/activity/topic_details")
                    .withInt(TagConstant.TOPIC_ID, id)
                    .navigation()
            }
            url.contains(Regex("^/c/[1-9]")) -> {
                val data = url.split("/")
                val id = data[data.size - 1].toInt()
                ARouter.getInstance().build("/home/activity/column_details")
                    .withInt(TagConstant.COLUMN_ID, id)
                    .navigation()
            }
            url.contains(Regex("^/doctor/[1-9]")) -> {
                val data = url.split("/")
                val id = data[data.size - 1].toInt()
                val param = Bundle()
                param.putString(
                    "page",
                    "pages/doctordetail/doctordetail?id=${id}"
                ) //设置路径
                miniProgram.startMiniProgram(
                    com.jmbon.middleware.BuildConfig.HOSPITAL_DOCTOR_ID,
                    param
                )

            }
            url.contains(Regex("^/hospital/[1-9]")) -> {
                val data = url.split("/")
                val id = data[data.size - 1].toInt()
                val param = Bundle()
                param.putString(
                    "page",
                    "pages/hospitaldetail/hospitaldetail?id=${id}"
                ) //设置路径
                miniProgram.startMiniProgram(
                    com.jmbon.middleware.BuildConfig.HOSPITAL_DOCTOR_ID,
                    param
                )
            }
            else -> {
                "该链接不支持跳转".showToast()
            }
        }
    }
}