package com.jmbon.middleware.oss

import com.alibaba.sdk.android.vod.upload.VODUploadCallback
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl
import com.blankj.utilcode.util.Utils
import com.jmbon.middleware.api.API
import com.jmbon.middleware.bean.VodEntity
import com.jmbon.middleware.utils.DateFormatUtil
import kotlinx.coroutines.*

suspend fun initOss(): OssManager {
    val stsBean = API.getSts()
    val ossManager = OssManager(
        Utils.getApp(),
        stsBean.accessKeyId,
        stsBean.accessKeySecret,
        stsBean.securityToken,
        "jmbon"
    )
    ossManager.initOSSClient()
    return ossManager
}

fun initVOD(vodBean: VodEntity, callback: VODUploadCallback): VODManager {
    val vodManager = VODManager()
    vodManager.init(vodBean,callback)
    return vodManager
}

/**
 * 上传单个文件
 */
suspend fun uploadSingleFileAsync(
    path: String,
    ossManager: OssManager, ossPath: String = "uploads/avatar"
) = withContext(Dispatchers.IO) {
    if (path.contains("https://image.jmbon.com/uploads/"))
        return@withContext path
    val strArray: List<String> = path.split(".")
    val fileSuffix = strArray[strArray.size - 1]
    val objectFileName =
        "$ossPath/${DateFormatUtil.getDateStringNow()}/tubewiki_${System.currentTimeMillis()}${path.hashCode()}.${fileSuffix}"
    val fileOssPath = "https://image.jmbon.com/${objectFileName}"
    ossManager.syncUploadFile(objectFileName, path)
    return@withContext fileOssPath
}

/**
 * 上传单个文件,带水印
 */
suspend fun uploadSingleFileAsyncWithWaterMark(
    path: String,
    ossManager: OssManager, ossPath: String = "uploads/avatar"
) = withContext(Dispatchers.IO) {
    if (path.contains("https://image.jmbon.com/uploads/"))
        return@withContext path
    val strArray: List<String> = path.split(".")
    val fileSuffix = strArray[strArray.size - 1]
    val objectFileName =
        "$ossPath/${DateFormatUtil.getDateStringNow()}/tubewiki_${System.currentTimeMillis()}${path.hashCode()}.${fileSuffix}"
    val fileOssPath = "https://image.jmbon.com/${objectFileName}"
//    ossManager.syncUploadFileWithWaterMark(objectFileName, path)
    ossManager.beginSyncUploadWithWaterMark(objectFileName, path)
    return@withContext fileOssPath
}
