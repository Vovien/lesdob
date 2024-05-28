package com.tubewiki.mine.view.wallet.details

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.visible
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.TimeUtils
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.loadCircle
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.middleware.utils.toUserCenter
import com.tubewiki.mine.R
import com.tubewiki.mine.bean.IncomeList
import com.tubewiki.mine.databinding.ActivityIncomeDetailsBinding
import com.tubewiki.mine.utils.StringFormat
import com.tubewiki.mine.view.model.WalletViewModel

/**
 * 收入详情
 */
@Route(path = "/mine/wallet/income/details")
class IncomeDetailsActivity : ViewModelActivity<WalletViewModel, ActivityIncomeDetailsBinding>() {

    @Autowired(name = TagConstant.PARAMS)
    @JvmField
    var id: Int = 0

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.income_details))
        setTitleBarColor(ColorUtils.getColor(R.color.ColorFAFA))
        StatusBarCompat.StatusBarLightModeWithColor(this, ColorUtils.getColor(R.color.ColorFAFA))
        initStateLayout(binding.stateLayout)

    }

    override fun initData() {
        lifecycleScope.launchWhenResumed {
            viewModel.incomeDetails(id).next {
                binding.apply {
                    personInfoLayout.setOnSingleClickListener({
                        if(user.isCancel==1){
                            R.string.user_has_log_off.showToast()
                            return@setOnSingleClickListener
                        }
                        user.uid.toUserCenter()
                    })
                    imageAvatar.loadCircle(user.avatarFile)
                    imageAvatar.setOnSingleClickListener({
                        if(user.isCancel==1){
                            R.string.user_has_log_off.showToast()
                            return@setOnSingleClickListener
                        }
                        user.uid.toUserCenter()
                    })
                    textUserName.text = user.userName
                    val handleMoney = StringFormat.dataFormat("$money", true)
                    exceptionalAmount.text = if (cateType == 6) handleMoney else "+${handleMoney}"
                    exceptionalAmount.setTextColor(
                        if (cateType == 6) ColorUtils.getColor(R.color.color_262626) else ColorUtils.getColor(
                            R.color.color_ff9823
                        )
                    )
                    textExceptionalTime.text = TimeUtils.millis2String(addTime * 1000L)
                    textArticleTitle.text = content.title
                    textExceptionalType.text = when (cateType) {
                        1 -> "文章"
                        3, 4, 6 -> "问答"
                        5 -> "咨询"
                        else -> "文章"
                    }
                    getToAdmireWay.text = when (cateType) {
                        1 -> "文章获得打赏"
                        3 -> {
                            // 打开答案标题
                            answerItem(this@next)
                            "回答获得打赏"
                        }
                        4 -> {
                            answerItem(this@next)
                            "回答被采纳，获得悬赏金"
                        }
                        5 -> "咨询费用"
                        6 -> {
                            personInfoLayout.gone()
                            // 删除问题的标题
                            textTitle.text = titleType
                            textAnswerContent.text = content.content
                            "悬赏问题被删除，金额退回"
                        }
                        else -> "文章打赏获得"
                    }
                }
                showContentState()
            }
        }
    }

    private fun ActivityIncomeDetailsBinding.answerItem(income: IncomeList.Income) {
        textTitle.text =  when (income.cateType) {
            1 -> "文章标题"
            3, 4, 6 -> "问题"
            else -> "问题"
        }
        textAnswer.text = getString(R.string.mine_my_answer)
        layoutAnswer.visible()
        textAnswerContent.text = income.content.content
    }

    override fun getData() {
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        initData()
    }
}