package com.tubewiki.mine.view.mine

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseActivity
import com.blankj.utilcode.util.StringUtils
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityPersonalPageLayoutBinding

/**
 * 草稿箱
 */
@Route(path = "/mine/draft/activity")
class DraftActivity : AppBaseActivity<ActivityPersonalPageLayoutBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(StringUtils.getString(R.string.mine_draft_box))
        initFragment()
    }

    override fun initData() {
    }

    override fun getData() {
    }

    private fun initFragment() {
        //等于0就是刚发布新增的评论，后台还未返回数据
        val content = ARouter.getInstance().build("/mine/questions/draft")
            .navigation() as Fragment
        supportFragmentManager.beginTransaction().add(R.id.content, content).commit()
    }
}