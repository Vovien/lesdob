package com.jmbon.middleware.bury

import com.apkdv.mvvmfast.ktx.launchWithFlow
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.utils.getBoolean
import com.apkdv.mvvmfast.utils.getInt
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.Utils
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.bury.api.BuryApi
import com.jmbon.middleware.bury.bean.PageTypeEnum
import com.jmbon.middleware.bury.db.BuryDatabase
import com.jmbon.middleware.bury.db.BuryPointInfo
import com.jmbon.middleware.bury.event.AppEventEnum
import com.jmbon.middleware.config.MMKVKey
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/******************************************************************************
 * Description: 埋点辅助类
 *
 * Author: jhg
 *
 * Date: 2023/7/19
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
object BuryHelper {

    private const val TAG = "BURY_TAG"

    /**
     * 记录上传阈值
     */
    private const val RECORD_UPLOAD_THRESHOLD = 20

    /**
     * 是否正在上传埋点数据
     */
    @Volatile
    private var uploading = false

    /**
     * 添加点击埋点事件
     * @param eventId: 埋点事件
     */
    fun addEvent(eventId: String, pageStayTime: Long = 0) {
        addEvent(BuryPointInfo(event_id = eventId, params = mutableMapOf<String, String?>().apply {
            if (pageStayTime > 0) {
                this["page_stay_time"] = if (pageStayTime >= 0) "$pageStayTime" else "0"
            }
        }), false)
    }

    /**
     * 添加热力图点击事件
     * @param pageType: 页面类型
     * @param areaId: 页面区域id
     * @param x: 点击位置的x坐标
     * @param y: 点击位置的y坐标
     */
    fun addEvent(pageType: PageTypeEnum, areaId: Int, x: Int, y: Int) {
        addEvent(
            BuryPointInfo(
                event_id = AppEventEnum.EVENT_HOT_IMAGE.value,
                params = mutableMapOf<String, String?>().apply {
                    this["page_type"] = "${pageType.value}"
                    this["area_id"] = "$areaId"
                    this["area_x"] = "$x"
                    this["area_y"] = "$y"
                }
            ), false
        )
    }

    /**
     * 插入埋点信息
     * 说明: 插入记录后, 如果总记录数超过RECORD_UPLOAD_THRESHOLD, 则执行action操作
     * @param buryPointInfo: 待插入的记录信息
     * @param uploadDirectly: 是否要直接触发上传操作, 当App启动或被切换到后台时需设置为true
     */
    fun addEvent(buryPointInfo: BuryPointInfo?, uploadDirectly: Boolean = false) {
        GlobalScope.launch {
            BuryDatabase.getDatabase(Utils.getApp()).buryDao().apply {
                if (buryPointInfo == null) {
                    if (uploadDirectly) {
                        // 直接上传数据库中的记录
                        getAllRecords()?.let {
                            uploadBuryPoint(it)
                        }
                    }
                    return@launch
                }

                LogUtils.i(TAG, "eventInfo===${GsonUtils.toJson(buryPointInfo)}")
                if (BuildConfig.DEBUG && MMKVKey.REALTIME_UPLOAD.getBoolean()) {
                    uploadBuryPoint(mutableListOf(buryPointInfo))
                    return@launch
                }

                val result = insert(buryPointInfo)
                val allRecordList = getAllRecords()
                // 如果插入成功, 则根据条件上传数据库中的所有记录, 否则构造上传记录
                val recordList = if (result > 0) {
                    allRecordList ?: mutableListOf()
                } else {
                    val tempList = mutableListOf<BuryPointInfo>()
                    if (!allRecordList.isNullOrEmpty()) {
                        tempList.addAll(allRecordList)
                    }
                    tempList.add(buryPointInfo)
                    tempList
                }
                LogUtils.i(TAG, "upload Delete Before: ${recordList.size} ===== uploading==$uploading")
                // 根据配置判断是否可上传
                val canUpload = BuildConfig.DEBUG && MMKVKey.UPLOAD_SPECIFIC_COUNT.getBoolean() && recordList.size >= MMKVKey.BURY_UPLOAD_COUNT.getInt()
                if (uploadDirectly || (!uploading && (recordList.size >= RECORD_UPLOAD_THRESHOLD || canUpload))) {
                    uploadBuryPoint(recordList)
                }
            }
        }
    }

    /**
     * 上传埋点信息
     */
    private suspend fun uploadBuryPoint(dataList: List<BuryPointInfo>) {
        uploading = true
        launchWithFlow(
            {
                val valueList = mutableListOf<Map<String, Any?>>()
                dataList.distinctBy { "${it.event_id}-${it.timestamp}" }.forEach {
                    valueList.add(mutableMapOf<String, Any?>().apply {
                        this["event_id"] = it.event_id
                        this["timestamp"] = it.timestamp
                        it.params?.forEach {
                            val value = it.value?.toLongOrNull() ?: -1
                            if (value >= 0 && it.key != "search_word") {
                                this[it.key] = value
                            } else {
                                this[it.key] = it.value
                            }
                        }
                    })
                }
                BuryApi.buryPoint(GsonUtils.toJson(valueList))
            }, {
                // 如果网络连接正常就直接删掉上传失败的埋点记录
                if (NetworkUtils.isConnected()) {
                    BuryDatabase.getDatabase(Utils.getApp()).buryDao().delete(dataList)
                    if (!it.message.isNullOrBlank() && BuildConfig.DEBUG) {
                        it.message.showToast()
                    }
                }
                uploading = false
            }
        ).next {
            // 上传成功后删除本地的埋点记录
            BuryDatabase.getDatabase(Utils.getApp()).buryDao().delete(dataList)
            LogUtils.i(TAG, "upload Delete after: ${BuryDatabase.getDatabase(Utils.getApp()).buryDao().getCount()} uploading==$uploading")
            uploading = false
        }
    }
}