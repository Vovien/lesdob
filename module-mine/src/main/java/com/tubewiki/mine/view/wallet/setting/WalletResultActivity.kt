package com.tubewiki.mine.view.wallet.setting

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.blankj.utilcode.util.ClickUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.dp
import com.tubewiki.mine.databinding.ActivityWalletResultLayoutBinding
import com.tubewiki.mine.view.model.MessageCenterViewModel

/**
 * 钱包操作结果页面
 */
@Route(path = "/mine/wallet/result")
class WalletResultActivity :
    ViewModelActivity<MessageCenterViewModel, ActivityWalletResultLayoutBinding>() {

    /**
     * 设置标题
     */
    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var title: String = "设置提现密码"

    /**
     * 设置主提示
     */
    @Autowired(name = TagConstant.PARAMS2)
    @JvmField
    var result: String = "设置成功"

    /**
     * 设置二级提示
     */
    @Autowired(name = TagConstant.PARAMS3)
    @JvmField
    var tips: String = ""

    /**
     * 按钮的文字
     */
    @Autowired(name = TagConstant.PARAMS4)
    @JvmField
    var btnMsg: String = "完成"

    /**
     * 设置需要标题
     */
    @Autowired(name = TagConstant.PARAMS5)
    @JvmField
    var needTitle: Boolean = true

    @Autowired(name = TagConstant.NEED_BACK)
    @JvmField
    var needBack: Boolean = false


    @Autowired(name = TagConstant.BTN_PADDING)
    @JvmField
    var btnPadding: Int = 16f.dp()

    @Autowired(name = TagConstant.PARAMS6)
    @JvmField
    var offsetTop: Int = 0

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun enableBack(): Boolean {
        return needTitle && needBack
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (needTitle) {
            setTitleName(title)
        } else {
            supportActionBar?.hide()
        }

        binding.apply {
            btnSure.setPadding(btnPadding, 0, btnPadding, 0)
            root.setPadding(0, offsetTop, 0, 0)
            if (tips.isEmpty()) {
                tvTips.visibility = View.GONE
            } else {
                tvTips.text = tips
                tvTips.visibility = View.VISIBLE
                titleBarView.leftImageButton.visibility = View.GONE
            }
            tvResult.text = result
            btnSure.text = btnMsg


            ClickUtils.applySingleDebouncing(btnSure) {
                finish()
            }
        }

    }


    override fun initData() {
    }

    override fun getData() {
    }
}