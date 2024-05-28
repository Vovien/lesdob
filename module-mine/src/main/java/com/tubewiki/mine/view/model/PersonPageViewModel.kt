package com.tubewiki.mine.view.model

import com.apkdv.mvvmfast.event.SingleLiveEvent
import com.apkdv.mvvmfast.ktx.showToast
import com.tubewiki.mine.api.MineApi
import com.tubewiki.mine.base.MineViewModel
import com.tubewiki.mine.bean.UserInfoData
import kotlinx.coroutines.Job

class PersonPageViewModel : MineViewModel() {


    val userInfoResult = SingleLiveEvent<UserInfoData.User>()
    var getUserJob: Job? = null
    fun getUserInfo(uid: Int) {
        getUserJob = launchOnlyResult({
            MineApi.getUserInfo(uid)
        }, {
            // 更新数据
            userInfoResult.postValue(it.user)
        }, {
            it.message.showToast()
        }, isShowDialog = false)
    }
}