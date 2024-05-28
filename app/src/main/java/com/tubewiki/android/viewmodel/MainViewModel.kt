package com.tubewiki.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.utils.mmkv
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.Utils
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.config.MMKVKey
import com.tubewiki.android.api.MainApi
import com.tubewiki.android.bean.CircleConfigBean
import com.umeng.commonsdk.UMConfigure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel : CommonViewModel() {

    val firstShowFlow = MutableStateFlow(true)

    fun clickMark() {
        viewScope().launch {
            launchWithFlow({ MainApi.clickMark() }).next { }
        }
    }

    /**
     * 圈子配置信息
     */
    private val _circleConfigLD = MutableLiveData<CircleConfigBean?>()
    val circleConfigLD: LiveData<CircleConfigBean?> = _circleConfigLD

    /**
     *  设备激活同步，不考虑卸载情况，后台会自动过滤
     */
    fun syncDevice(result: () -> Unit) {
        if (!mmkv.getBoolean(MMKVKey.ACTIVATE_DEVICE, false)) {
            activeDevice(result)
        }
    }

    private fun activeDevice(result: () -> Unit) {
        UMConfigure.getOaid(Utils.getApp()) { oaid ->
            viewScope().launch {
                //获取oaid
                val ip = "" //NetworkUtils.getIPAddress(true) wifi是获取局域网，ip让后台服务器获取
                var sysVersion = DeviceUtils.getSDKVersionName()
                var deviceModel = DeviceUtils.getModel()
                launchWithFlow({
                    MainApi.syncDevice(
                        oaid,
                        ip,
                        sysVersion,
                        deviceModel
                    )
                }).next {
                    true.saveToMMKV(MMKVKey.ACTIVATE_DEVICE)
                    result()
                }
            }
        };

    }


    fun setUseToolStatus(toolId: String = "") {
        viewScope().launch {
            launchWithFlow({
                MainApi.setUseToolStatus(toolId)
            }, handleError = false).next {
            }
        }
    }

    /**
     * 获取圈子配置信息
     */
    fun getCircleConfig() {
        viewModelScope.launch {
            launchWithFlow({ MainApi.getCircleConfig() }, {
                _circleConfigLD.postValue(null)
            }).next {
                _circleConfigLD.postValue(this)
            }
        }
    }

    fun getRecommendCircle(refresh: Boolean) = launchWithFlow({
        if (refresh) {
            currentPage = 1
        }
        MainApi.getRecommendCircle(page = currentPage)
    }, handleError = true).map {
        currentPage++
        it
    }

}