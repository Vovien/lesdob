package com.tubewiki.mine.view

import android.view.View
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.jmbon.middleware.adapter.ImageCommonBannerAdapter
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.loadCircle
import com.jmbon.middleware.utils.logInToIntercept
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.middleware.valid.action.Action
import com.tubewiki.mine.databinding.FragmentMineBinding
import com.tubewiki.mine.view.model.MineFragmentViewModel
import com.youth.banner.indicator.CircleIndicator
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 我的首页
 */
@Route(path = "/mine/main/fragment")
class MineFragment : ViewModelFragment<MineFragmentViewModel, FragmentMineBinding>() {
    private val statusHeight by lazy { StatusBarCompat.getStatusBarHeight(view?.context) }

    private val bannerAdapter by lazy {
        ImageCommonBannerAdapter {

        }
    }

    override fun beforeViewInit() {
        super.beforeViewInit()
        EventBus.getDefault().register(this)
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    override fun initView(view: View) {
        binding.apply {
//            tvName.text = Constant.userInfo.userName
//            ivAvatar.loadCircle(Constant.userInfo.avatarFile)
//            ivBg.load(Constant.userInfo.background)
            val isNormalMode = CommonViewModel.configFlow.value?.examine_switch == 0
            rlSessionLog.isVisible = isNormalMode
            rlSessionLog.setOnSingleClickListener({
                Action{
                    ARouter.getInstance().build("/mine/conversationRecord").navigation()
                }.logInToIntercept()
            })
            rlSetting.setOnSingleClickListener({
                ARouter.getInstance().build("/mine/setting/activity").navigation()
            })
            rlCollect.setOnSingleClickListener({
                Action{
                    ARouter.getInstance().build("/mine/setting/collect").navigation()
                }.logInToIntercept()
            })

//            jbEdit.setOnSingleClickListener({
//                ARouter.getInstance().build("/mine/edit/info").navigation()
//            })

            banner.apply {
                indicator = CircleIndicator(context)
                setAdapter(bannerAdapter, true)
            }
        }

    }

    override fun initViewModel() {
        super.initViewModel()
    }


    override fun onResume() {
        super.onResume()

        binding.apply {
            tvName.text = Constant.userInfo.userName
            ivAvatar.loadCircle(Constant.userInfo.avatarFile)
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: UserLoginEvent) {

    }

}