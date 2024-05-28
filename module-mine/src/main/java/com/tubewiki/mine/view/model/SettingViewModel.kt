package com.tubewiki.mine.view.model

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apkdv.mvvmfast.bean.ResultTwoData
import com.apkdv.mvvmfast.event.SingleLiveEvent
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.bean.ContentBean
import com.jmbon.middleware.bean.UserData
import com.jmbon.middleware.config.CacheConfig
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.oss.initOss
import com.jmbon.middleware.oss.uploadSingleFileAsync
import com.luck.picture.lib.entity.LocalMedia
import com.tubewiki.mine.api.MineApi
import com.tubewiki.mine.base.MineViewModel
import com.tubewiki.mine.bean.NotifySetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SettingViewModel : MineViewModel() {

    val uploadInfoStatus = MutableLiveData<Boolean>()
    val notifySetting = SingleLiveEvent<NotifySetting.UserNotifySetting>()

    fun uploadInfo(infoMap: HashMap<String, String>) {
        launchOnlyResult({
            MineApi.setUserInfo(infoMap)
        }, {
            uploadInfoStatus.postValue(true)
        }, {
            it.message.showToast()
            uploadInfoStatus.postValue(false)
        }, handleError = false)
    }

    suspend fun uploadInfoWithFlow(infoMap: HashMap<String, String>) = MineApi.setUserInfo(infoMap)

    fun uploadImage(image: LocalMedia) = launchWithFlow({
        if (image.compressPath.isNotEmpty()) {
            val images = arrayListOf<String>()
            val ossManager = initOss()
            withContext(Dispatchers.IO) {
                return@withContext uploadSingleFileAsync(image.compressPath, ossManager)
            }
        } else ""
    }, handleError = false)


    suspend fun setBackgroundWithFlow(bg: String) = MineApi.setBackground(bg)


//    {
//        launchOnlyResult({
//            MineApi.setBackground(bg)
//        }, {
//            //更新信息
//            Constant.userInfo.background = bg
//            Constant.updateInfo(Constant.userInfo)
//            uploadInfoStatus.postValue(true)
//        }, {
//            uploadInfoStatus.postValue(false)
//            it.message.showToast()
//        }, handleError = false)
//    }

    fun setBackground(bg: String) {
        launchOnlyResult({
            MineApi.setBackground(bg)
        }, {
            //更新信息
            Constant.userInfo.background = bg
            Constant.updateInfo(Constant.userInfo)
            uploadInfoStatus.postValue(true)
        }, {
            uploadInfoStatus.postValue(false)
            it.message.showToast()
        }, handleError = false)
    }

    fun getNotifySetting() {
        launchOnlyResult(
            {
                MineApi.notifysSetting()
            }, {
                notifySetting.postValue(it.user)
            }, {
                it.message.showToast()
            }
        )
    }

    /**
     * 状态【0：不通知，1：通知】
     */
    fun setNotifySetting(key: String, status: Int) {
        launchOnlyResult(
            {
                MineApi.setNotify(key, status)
            }, {}, {
                it.message.showToast()
            }, handleError = true
        )
    }

    val setPass = MutableLiveData<Boolean>()

    /**
     * 设置密码
     */
    fun setPassword(password: String, mobile: String, token: String) {
        launchOnlyResult(
            {
                MineApi.setPassword(password, token, mobile)
            }, {
                setPass.postValue(true)
            }, {
                it.message.showToast()
                setPass.postValue(false)
            }, handleError = true
        )
    }

    val sendEmailToken = MutableLiveData<ResultTwoData<String, String>>()

    /**
     * 修改邮箱与设置邮箱发送邮箱
     */
    fun sendVerifyEmail(email: String, token: String) = launchWithFlow({
        MineApi.sendVerifyEmail(token, email)
    }).map {
        ResultTwoData(it.emailToken, email)
    }

    val setEmailStatus = SingleLiveEvent<Boolean>()

    /**
     * 修改邮箱与设置邮箱发送邮箱
     */
    fun setEmail(token: String) {
        launchOnlyResult(
            {
                MineApi.setEmail(token)
            }, {
                if (TextUtils.isEmpty(it.email)) {
                    setEmailStatus.postValue(false)
                } else {
                    setEmailStatus.postValue(true)
                }
            }, {
                setEmailStatus.postValue(false)
                it.message.showToast()
            }, handleError = true
        )
    }

    val safeSet = MutableLiveData<UserData.User>()

    /**
     * 修改邮箱与设置邮箱发送邮箱
     */
    fun getUserSafe() {
        launchOnlyResult(
            {
                MineApi.getUserSafe()
            }, {
                safeSet.postValue(it)
            }, {
                it.message.showToast()
            }, handleError = true
        )
    }

//    val logout = MutableLiveData<Boolean>()


    /**
     * 退出登录
     */
//    fun logout() = launchWithFlow({ MineApi.logout() }, handleError = false)

    fun logout() {
        CacheConfig.cleanAllCache()
        // 冷流需要订阅
//        viewScope().launch {
//            launchWithFlow({ MineApi.logout() }, handleError = false).next { }
//        }
    }

    fun getNickCount() = launchWithFlow({
        MineApi.getNickCount()
    })

    fun contactUs() = launchWithFlow({
        MineApi.contactUs()
    })

    fun setMobile(mobile: String, token: String) = launchWithFlow({
        MineApi.setMobile(token, mobile)
    }).netCatch { message.showToast() }

    fun feedback(
        content: String, type: Int
    ) = launchWithFlow({
        MineApi.feedback(content, type)
    }).netCatch { message.showToast() }

    fun bindWx(openid:String,nickName:String) = launchWithFlow({
        MineApi.bindWechat(openid,nickName)
    })
    fun unBindWx() = launchWithFlow({
        MineApi.unBindWechat()
    })

    /**
     * 我的收藏
     */
    private val _mineCollection = MutableLiveData<List<ContentBean>?>()
    val mineCollection: LiveData<List<ContentBean>?> = _mineCollection

    /**
     * 获取我的收藏
     */
    fun getMineCollection() {
        launchOnlyResult({
            MineApi.getMineCollection(currentPage)
        }, {
            _mineCollection.postValue(it.collect_list)
        }, {
            _mineCollection.postValue(null)
        }, handleError = false)
    }

}