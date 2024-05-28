package com.tubewiki.mine.view.model

import androidx.lifecycle.MutableLiveData
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.config.Constant
import com.tubewiki.mine.api.MineApi
import com.tubewiki.mine.base.MineViewModel

class UserInfoViewModel : MineViewModel() {

    val setSex = MutableLiveData<Boolean>()
    val setPregnantStatus = MutableLiveData<Boolean>()

    /**
     * 设置性别
     * 1：男生，2：女生
     */
    fun setSex(sex: Int) {
        launchOnlyResult({
            MineApi.setSex(sex)
        }, {
            // 更新用户数据
            Constant.userInfo.sex = sex
            Constant.updateInfo(Constant.userInfo)
            setSex.postValue(true)
        }, {
            setSex.postValue(false)
            it.message.showToast()
        }, isShowDialog = false)
    }

    /**
     * 设置怀孕状态
     * 状态：0：未设置，1：备孕中，2：已怀孕，3：有宝宝
     */
    fun setPregnantStatus(status: Int) {
        launchOnlyResult({
            MineApi.setPregnantStatus(status)
        }, {
            // 更新用户数据
            Constant.userInfo.pregnantStatus = status
            Constant.updateInfo(Constant.userInfo)
            setPregnantStatus.postValue(true)
        }, {
            setPregnantStatus.postValue(false)
            it.message.showToast()
        }, isShowDialog = false)
    }

    fun setAgeAndGender(sex: Int, age: String) = launchWithFlow({
        MineApi.setAgeAndGender(sex, age)
    }, handleError = false)
}