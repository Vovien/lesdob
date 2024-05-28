package com.tubewiki.mine.view.wallet

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.ColorUtils
import com.jmbon.middleware.bean.WalletSettingData
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.kotlinbus.KotlinBus
import com.jmbon.middleware.kotlinbus.UI
import com.jmbon.middleware.utils.TagConstant
import com.tubewiki.mine.R
import com.tubewiki.mine.adapter.ViewPagerAdapter
import com.tubewiki.mine.bean.event.WithdrawalSuccess
import com.tubewiki.mine.databinding.ActivityWalletBinding
import com.tubewiki.mine.utils.StringFormat
import com.tubewiki.mine.view.model.WalletViewModel
import com.tubewiki.mine.view.wallet.fragment.IncomeDetailsFragment


/**
 * 钱包界面
 */
@Route(path = "/mine/wallet")
class WalletActivity : ViewModelActivity<WalletViewModel, ActivityWalletBinding>() {

    private val mTitles = arrayOf("收入明细", "提现明细")
    val fragmentList = arrayListOf<Fragment>()
    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.wallet))
        setToolbarBackground(ColorUtils.getColor(R.color.ColorFAFA))
        StatusBarCompat.StatusBarLightModeWithColor(this, ColorUtils.getColor(R.color.ColorFAFA))

        binding.apply {

            KotlinBus.register(
                this@WalletActivity.hashCode().toString(),
                UI,
                WithdrawalSuccess::class.java
            ) { event ->
                viewpager.currentItem = 1
            }
            initPageState()
            orderLayout.initTabView(mTitles.asList())
//            val typeface = Typeface.createFromAsset(assets, "fonts/roboto_black.ttf")
//            textTotal.typeface = typeface
            // textTotal.paint.isFakeBoldText = true

            ClickUtils.applySingleDebouncing(stWithdrawalSet) {
                ARouter.getInstance().build("/mine/wallet/withdrawal/setting").navigation()
            }
            ClickUtils.applySingleDebouncing(sbWithdraw) {
                if ((Constant.walletSetting.hasAlipay || Constant.walletSetting.hasWechat) && Constant.walletSetting.hasPassword)
                    ARouter.getInstance().build("/mine/withdraw")
                        .withFloat(
                            TagConstant.FULL_AMOUNT,
                            binding.textTotal.tag.toString().toFloat()
                        )
                        .navigation()
                else {
                    "需完成提现设置\n方可提现".showToast()
                }
            }

            fragmentList.add(
                ARouter.getInstance().build("/mine/wallet/fragment/income")
                    .navigation() as IncomeDetailsFragment
            )
            fragmentList.add(
                ARouter.getInstance().build("/mine/wallet/fragment/income")
                    .withInt(TagConstant.TYPE, 1)
                    .navigation() as IncomeDetailsFragment
            )
            viewpager.adapter = ViewPagerAdapter(fragmentList, supportFragmentManager)
            viewpager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    orderLayout.setSelected(position)
                }

                override fun onPageScrollStateChanged(state: Int) {
                }

            })
            orderLayout.setSelectedClickListener {
                viewpager.currentItem = it
            }
        }
//        showLoadingState()
    }


    override fun showContentState() {
        super.showContentState()
        binding.orderLayout.splitView()
    }


    private fun setPasswordStatus(it: WalletSettingData.Data) {
        binding.stWithdrawalSet.setRightString(if (it.hasPassword && (it.hasWechat || it.hasAlipay)) "已设置" else "未设置")
        binding.stWithdrawalSet.setRightTextColor(
            if (it.hasPassword && (it.hasWechat || it.hasAlipay)) resources.getColor(R.color.color_BFBFBF)
            else resources.getColor(R.color.colorFF5050)
        )
    }

    override fun onResume() {
        super.onResume()
        setPasswordStatus(Constant.walletSetting)
        getNetData()
    }


    private fun getNetData() {
        lifecycleScope.launchWhenResumed {
            viewModel.getBalance().next {
                showContentState()
                // 设置数据
                binding.textTotal.tag = this.data1.toString()
                binding.textTotal.text =
                    StringFormat.dataFormat(this.data1.toString(), true)
                // 设置钱包的 Data
                Constant.setWalletSettingData(this.data2)
                setPasswordStatus(this.data2)
            }
        }
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        getNetData()
    }
    override fun initData() {

    }

    override fun getData() {

    }

    override fun onDestroy() {
        KotlinBus.unregister(this.hashCode().toString())
        super.onDestroy()
    }

}