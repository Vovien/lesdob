package com.tubewiki.home.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.tubewiki.home.databinding.ActivityEmptyArticleBinding

/**
 * 空文章界面
 * @author MilkCoder
 * @date 2023/8/9
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
@Route(path = "/home/activity/empty_article")
class EmptyArticleActivity : ViewModelActivity<BaseViewModel, ActivityEmptyArticleBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

    }

}