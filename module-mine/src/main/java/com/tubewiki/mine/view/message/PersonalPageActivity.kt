package com.tubewiki.mine.view.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseActivity
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.TagConstant
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityPersonalPageLayoutBinding


/**
 * 个人主页
 */
@Route(path = "/mine/message/personal_page")
class PersonalPageActivity : AppBaseActivity<ActivityPersonalPageLayoutBinding>() {


    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var uid: Int = 0

    @Autowired(name = "index")
    @JvmField
    var index: Int = 0

    override fun beforeViewInit() {
        super.beforeViewInit()
        StatusBarCompat.setTransparentStatusBar(window)
        ARouter.getInstance().inject(this)
    }


    override fun initView(savedInstanceState: Bundle?) {
        initFragment()
    }

    private fun initFragment() {
        val content = ARouter.getInstance().build("/mine/fragment/personal_page")
            .withInt(TagConstant.PARAMS, if (uid == 0) Constant.getUserId() else uid)
            .withInt("index", index)
            .navigation() as Fragment
        supportFragmentManager.beginTransaction().add(R.id.content, content).commit()
    }


    override fun initData() {
    }

    override fun getData() {

    }


}

