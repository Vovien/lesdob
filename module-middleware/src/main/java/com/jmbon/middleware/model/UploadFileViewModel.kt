package com.jmbon.middleware.model

import android.content.Context
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.bean.ResultTwoData
import com.apkdv.mvvmfast.event.SingleLiveEvent
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.LogUtils
import com.jmbon.middleware.oss.OssManager
import com.jmbon.middleware.api.API
import com.jmbon.middleware.utils.BitmapUtils
import com.jmbon.middleware.utils.DateFormatUtil
import com.luck.picture.lib.entity.LocalMedia
import java.io.File

class UploadFileViewModel : BaseViewModel() {

    val fileUploadResult = SingleLiveEvent<String?>()

    fun uploadFile(context: Context, fileUrl: String?) {
        if (fileUrl.isNullOrEmpty())
            return
        val file = File(fileUrl)
        if (!file.exists() || !file.isFile) {
            //获取文件后缀
            return
        }
        //先获取oss sts参数再上传文件
        launchOnlyResult({
            // 获取文件后缀名
            val fileSuffix = file.name.substring(file.name.lastIndexOf("."))
            // 上传后的文件路径+名称
            val objectFileName =
                "uploads/avatar/${DateFormatUtil.getDateStringNow()}/tubewiki_${System.currentTimeMillis()}${fileSuffix}"
            val fileOssPath = "https://image.jmbon.com/${objectFileName}"
            val stsBean = API.getSts()
            //请求拿到sts数据后上传oss文件
            val ossManager = OssManager(
                context,
                stsBean.accessKeyId,
                stsBean.accessKeySecret,
                stsBean.securityToken,
                "jmbon"
            )
            ossManager.initOSSClient()
            ossManager.beginAsyncUpload(context, objectFileName, fileUrl)

            return@launchOnlyResult ResultTwoData(ossManager, fileOssPath)
        }, {
            it.data1.progressCallback = object : OssManager.ProgressCallback {
                override fun onUploadFail(serviceException: ServiceException?) {
                    fileUploadResult.postValue(null)
                }

                override fun onProgress(progress: Double, currentSize: Long, totalSize: Long) {

                }


                override fun onUploadSuccess(result: PutObjectResult?) {
                    //文件上传完成结果回调 UI
                    fileUploadResult.postValue(it.data2)
                }
            }
        }, {
            it.message.showToast()
            fileUploadResult.postValue(null)
        })
    }

    val multiFileUploadResult = MutableLiveData<MutableList<String>>()
    val progressResult = SingleLiveEvent<Double>()

    var ossUrls = mutableListOf<String>()

    /***
     * 提问多文件上传,递归
     */
    fun uploadFiles(context: Context, files: MutableList<LocalMedia>,ossPath:String = "notify") {
        ossUrls.clear()
        if (files.isEmpty()) {
            multiFileUploadResult.postValue(ossUrls)
            return
        }
        //获取oss sts参数，再上传文件
        launchOnlyResult({

            val fileUrls = mutableListOf<String>()
            //总文件大小
            var totalFileSize = 0L
            //有图片发布问题，先上传图片再发布
            for (item in files) {
                val path =
                    if (TextUtils.isEmpty(item.compressPath)) item.realPath else item.compressPath
                fileUrls.add(path)
                totalFileSize += item.size
            }

            val stsBean = API.getSts()
            //请求拿到sts数据后上传oss文件
            //ossUpload(context, stsBean, objectFileName, fileUrl, fileUrls, fileOssPath)

            val ossManager = OssManager(
                context,
                stsBean.accessKeyId,
                stsBean.accessKeySecret,
                stsBean.securityToken,
                "jmbon"
            )
            ossManager.initOSSClient()

            var currUploadFileSize = 0L
            var pos = 0
            for (fileUrl in fileUrls) {
                var currFileSize = files.get(pos).size
                //如果文件是已经上传过的就跳过，上传未上传的文件
                if (fileUrl.contains("https://image.jmbon.com/uploads/") || fileUrl.contains("https://video.jmbon.com/")) {
                    pos++
                    ossUrls.add(fileUrl)
                    continue
                }

                if (fileUrl.endsWith(".png")
                    || fileUrl.endsWith(".jpg")
                    || fileUrl.endsWith(".jpeg")
                    || fileUrl.endsWith(".PNG")
                    || fileUrl.endsWith(".JPEG")
                    || fileUrl.endsWith(".JPG")
                )  {
                    //获取图片旋转角度
                    val degree = BitmapUtils.getExifOrientation(fileUrl)
                    //纠正图片旋转角度，否则oss图片是旋转了的
                    val bitmap = BitmapUtils.rotaingImageView(
                        degree,
                        BitmapFactory.decodeFile(fileUrl)
                    )
                    //保存旋转过来的图片角度
                    BitmapUtils.savePhotoToSD(fileUrl, bitmap, context)
                    //回收bitmap
                    bitmap.recycle()
                }
                if (TextUtils.isEmpty(fileUrl)) {
                    pos++
                    continue
                }

                val file = File(fileUrl)
                if (!file.exists() || !file.isFile) {
                    pos++
                    // 文件不存在或者非文件也没必要上传，跳过它继续上传的逻辑
                    continue
                }
                var fileSuffix = ""
                // 获取文件后缀名
                fileSuffix = file.name.substring(file.name.lastIndexOf("."))
                // 上传后的文件路径+名称
                val objectFileName =
                    "uploads/${ossPath}/${DateFormatUtil.getDateStringNow()}/tubewiki_${System.currentTimeMillis()}${fileSuffix}"

                val fileOssPath = "https://image.jmbon.com/${objectFileName}"
                LogUtils.d("上传文件结果地址：${fileOssPath}")
                ossManager.progressCallback = object : OssManager.ProgressCallback {

                    override fun onProgress(progress: Double, currentSize: Long, totalSize: Long) {
                        val toatalProgress =
                            (currUploadFileSize + (progress / 100 * currFileSize)) / totalFileSize * 100
                        progressResult.postValue(toatalProgress)
                    }

                    override fun onUploadSuccess(result: PutObjectResult?) {
                        currUploadFileSize += currFileSize
                    }

                    override fun onUploadFail(serviceException: ServiceException?) {

                    }

                }
                ossManager.beginSyncUpload(context, objectFileName, fileUrl)


                ossUrls.add(fileOssPath)
                pos++

            }
            return@launchOnlyResult ossUrls

        }, {
            //流程结束
            multiFileUploadResult.postValue(ossUrls)
        }, {
            it.message.showToast()
            progressResult.postValue(-1.0)
            ossUrls.clear()
            multiFileUploadResult.postValue(ossUrls)
            it.printStackTrace()
        }, {}, true)


    }

}