package com.jmbon.home.view.article.viewmodel

import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.bean.ResultThreeData
import com.apkdv.mvvmfast.event.SingleLiveEvent
import com.chad.library.adapter.base.entity.MultiItemEntity


class ArticleViewModel : BaseViewModel() {
    private var page = 2
    private var columnPage = 2
    private var creatorsPage = 2

    val articleList =
        SingleLiveEvent<ResultThreeData<ArrayList<MultiItemEntity>, Boolean, Boolean>>()




}