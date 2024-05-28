package com.tubewiki.mine.view.model

import android.content.Intent
import androidx.lifecycle.LifecycleCoroutineScope
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.activity.PhotoPreviewActivity
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.oss.initOss
import com.jmbon.middleware.oss.uploadSingleFileAsync
import com.jmbon.middleware.utils.TagConstant
import com.tubewiki.mine.R
import com.tubewiki.mine.base.MineViewModel
import com.tubewiki.mine.view.setting.MinePhotoPreviewActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SetNewImageViewModel : MineViewModel() {

    fun uploadImage(image: String, type: Int) = launchWithFlow({
        if (image.isNotEmpty()) {
            val ossManager = initOss()
            withContext(Dispatchers.IO) {
                return@withContext uploadSingleFileAsync(image, ossManager)
            }
        } else ""
    }, handleError = false, isShowDialog = true, complete = {
        if (it) {
            if (type == MinePhotoPreviewActivity.USER_ACCOUNT_PICTURES) {
                R.string.with_head_is_in_success.showToast()
            } else {
                R.string.change_background_successfully.showToast()
            }
        }
    })

    fun processingImages(
        intent: Intent,
        startedScope: LifecycleCoroutineScope,
        settingViewModel: SettingViewModel,
        nextCallBack: (type: Int, imageUrl: String) -> Unit,
    ) {
        intent.getStringExtra(TagConstant.IMAGE_PATH)?.let {
            val type = intent.getIntExtra(
                TagConstant.TYPE,
                PhotoPreviewActivity.USER_ACCOUNT_PICTURES
            )
            startedScope.launchWhenStarted {
                uploadImage(it, type).map {
                    withContext(Dispatchers.IO) {
                        if (type == MinePhotoPreviewActivity.USER_ACCOUNT_PICTURES){
                            var infoMap = hashMapOf<String,String>()
                            infoMap["avatar"] = it
                            settingViewModel.uploadInfoWithFlow(infoMap)
                        }
                        else settingViewModel.setBackgroundWithFlow(it)
                    }
                    it
                }.netCatch {
                    message.showToast()
                }.next {
                    nextCallBack.invoke(type, this)
                    if (type == MinePhotoPreviewActivity.USER_ACCOUNT_PICTURES) {
                        Constant.userInfo.avatarFile = this
                    } else {
                        Constant.userInfo.background = this
                    }
                    Constant.updateInfo(Constant.userInfo)
                }
            }
        }
    }


}