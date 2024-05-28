package com.jmbon.middleware.search

import android.view.View
import com.apkdv.mvvmfast.base.BaseViewModel
import com.jmbon.middleware.bean.KeyWordList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class SearchMessageViewModel : BaseViewModel() {

    val editHint =  MutableStateFlow("")
    val searchKey = MutableSharedFlow<String>()

    val canSearchHint = MutableStateFlow(false)

    // 需要推荐数据
    val needRecommended = MutableStateFlow(false)

    // 需要推荐的关键字
    // 允许发送重复数据
    val recommendKey = MutableSharedFlow<String>()

    // 搜索推荐数据
    val recommendList = MutableSharedFlow<ArrayList<KeyWordList.KeyWord>?>()

    // 被动开始搜索（搜索关键字不是由输入框触发的）
    val passiveSearchKey = MutableSharedFlow<String>()

    // 右边取消按钮
    val showCancel = MutableStateFlow(false)
    val makeCancelTextLikeSearch = MutableStateFlow(false)
    val cancelText = MutableStateFlow("")
    val cancelTextColor = MutableStateFlow(0)
    val cancelTextSize = MutableStateFlow(0)
    //自己设置了文本后，自动取消原有的点击事件
    val onCancelClick = MutableSharedFlow<String>()

    // 返回按钮
    val showBack = MutableStateFlow(false)
    val needShowSearchContentWhenInit = MutableStateFlow(false)


    val enableEdited = MutableStateFlow(true)
    // 输入框点击事件
    val onEditClick =  MutableSharedFlow<Boolean>()
}