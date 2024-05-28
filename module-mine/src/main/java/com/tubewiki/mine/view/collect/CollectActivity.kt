package com.tubewiki.mine.view.collect

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseActivity
import com.blankj.utilcode.util.StringUtils
import com.jmbon.middleware.utils.TagConstant
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityCollectBinding

/**
 * 收藏页面
 */
@Route(path = "/mine/collect/activity")
class CollectActivity : AppBaseActivity<ActivityCollectBinding>() {

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var collectionType: String = "answer"

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(StringUtils.getString(R.string.collect))

        titleBarView.setRightView(R.layout.custom_titlebar_search_view)
        titleBarView.rightCustomView.setOnClickListener {
            ARouter.getInstance().build("/mine/collect_search/activity")
                .withInt(TagConstant.SEARCH_TYPE, TagConstant.SEARCH_COLUMN)
                .navigation()
        }
        initFragment()
    }


    override fun initData() {
    }

    override fun getData() {
    }

    private fun initFragment() {
        val content = ARouter.getInstance().build("/mine/collection/fragment")
            .withString(TagConstant.PARAMS, collectionType)
            .navigation() as Fragment
        supportFragmentManager.beginTransaction().add(R.id.content, content).commit()
    }
}