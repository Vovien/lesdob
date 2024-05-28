package com.jmbon.middleware.utils.vodupload

import com.alibaba.sdk.android.vod.upload.VODUploadCallback
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl
import com.alibaba.sdk.android.vod.upload.model.VodInfo
import com.blankj.utilcode.util.Utils
import com.jmbon.middleware.bean.StsEntity

object VODUpload {
    private val uploader by lazy { VODUploadClientImpl(Utils.getApp()) }
    fun uploadFileToVOD(
        filePath: String,
        stsBean: StsEntity,
        callback: VODUploadCallback,
    ) {
        uploader.init(
            stsBean.accessKeyId,
            stsBean.accessKeySecret,
            stsBean.securityToken,
            stsBean.expiration,
            callback);
        val vodInfo = VodInfo()
        uploader.addFile(filePath, vodInfo)
    }
}