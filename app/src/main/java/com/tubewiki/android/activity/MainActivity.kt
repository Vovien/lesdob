package com.tubewiki.android.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.Utils
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.utils.logInToIntercept
import com.jmbon.middleware.valid.action.Action
import com.jmbon.minitools.content.fragment.KnowledgeFragment
import com.qmuiteam.qmui.kotlin.onClick
import com.tubewiki.android.databinding.ActivityMainBinding
import com.tubewiki.android.fragment.AddGroupFragment
import com.tubewiki.android.viewmodel.MainViewModel
import com.tubewiki.home.fragment.HomeFragment
import com.tubewiki.home.fragment.MainFragment
import com.tubewiki.mine.view.MineFragment
import com.umeng.analytics.MobclickAgent
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@ObsoleteCoroutinesApi
@Route(path = "/main/main")
class MainActivity : ViewModelActivity<MainViewModel, ActivityMainBinding>() {
    override fun beforeViewInit() {
        super.beforeViewInit()
        ImmersionBar.with(this@MainActivity)
            .transparentStatusBar()
            .navigationBarColorInt(Color.BLACK)
            .init()
        ActivityUtils.finishAllActivitiesExceptNewest()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        initPageState()
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.commonConfigLD.observe(this) {
            initTabLayout(it?.examine_switch != 0)
        }
        viewModel.generalConf()
    }

    private fun initTabLayout(isCheckMode: Boolean) {
        showContentState()
        val tabTitles = if (isCheckMode) {
            arrayOf("首页", "文章", "我的")
        } else {
            arrayOf("首页", "知识", "加群", "我的")
        }
        val fragmentList = if (isCheckMode) {
            arrayListOf(MainFragment(), KnowledgeFragment(), MineFragment())
        } else {
            arrayListOf(HomeFragment(), KnowledgeFragment(), AddGroupFragment(), MineFragment())
        }
        binding.tabLayout.setViewPager(binding.viewPage, tabTitles, supportFragmentManager, fragmentList)
        binding.tabLayout.getTabView(tabTitles.size-1)?.onClick {
            Action{
                binding.tabLayout.setCurrentTab(tabTitles.size-1, false)
            }.logInToIntercept()
        }
    }


    private var isExit: Boolean = false

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val handler = Handler()
            if ((!isExit)) {
                isExit = true
                "再按一次退出".showToast()
                handler.postDelayed({ isExit = false }, (1000 * 2).toLong()) //2秒后没按就取消
            } else {
                MobclickAgent.onKillProcess(Utils.getApp())
                finish()
            }
        }
        return false
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: UserLoginEvent) {
        // 退出登录后页面回到首页
        if (!event.login) {
            binding.viewPage.setCurrentItem(0, false)
        }
    }

    override fun onStop() {
        super.onStop()
        if (isFinishing && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }
}
