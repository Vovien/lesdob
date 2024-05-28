package com.jmbon.middleware.oss

import com.alibaba.sdk.android.vod.upload.VODUploadCallback
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl
import com.alibaba.sdk.android.vod.upload.model.VodInfo
import com.blankj.utilcode.util.Utils
import com.jmbon.middleware.bean.VodEntity
import com.jmbon.middleware.utils.DateFormatUtil
import java.io.File

class VODManager {
    private val uploader by lazy { VODUploadClientImpl(Utils.getApp()) }
    lateinit var vodBean: VodEntity
    fun init(vodBean: VodEntity, callback: VODUploadCallback) {
        this.vodBean = vodBean
        val sts = vodBean.sts.credentials
        uploader.setRegion("cn-shanghai")
        uploader.setRecordUploadProgressEnabled(true)


        uploader.init(
            sts.accessKeyId,
            sts.accessKeySecret,
            sts.securityToken,
            sts.expiration,
            callback
        )
        uploader.setPartSize(1024 * 1024 * 5)
//        uploader.setRegion(stsBean.region)
        uploader.setWorkflowId(vodBean.config.workflowId)
    }

    /**
     * 上传视频到指定视频流
     */
    fun uploadFileToVOD(filePath: String) {
        val strArray: List<String> = filePath.split(".")
        val fileSuffix = strArray[strArray.size - 1]

        val vodInfo = VodInfo()
        val fileName =
            "${DateFormatUtil.getDateStringNow()}/tubewiki_${System.currentTimeMillis()}.${fileSuffix}"
        vodInfo.title = fileName
        vodInfo.fileName = "${fileName}.${fileSuffix}"
        uploader.addFile(filePath, vodInfo)
    }

    fun start() {
        uploader.start()
    }
}