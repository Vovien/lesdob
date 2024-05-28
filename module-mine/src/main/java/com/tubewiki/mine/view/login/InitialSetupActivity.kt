package com.tubewiki.mine.view.login

import android.os.Bundle
import android.view.KeyEvent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.base.ViewModelFactory

import com.apkdv.mvvmfast.ktx.started
import com.jmbon.middleware.bean.CityList
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.String
import com.jmbon.middleware.utils.TagConstant
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.ActivityGenderSelectionBinding
import com.tubewiki.mine.view.login.model.LoginViewModel
import com.tubewiki.mine.view.model.UserInfoViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 首次登录选择性别
 */
@Route(path = "/mine/login/initial/setup")
class InitialSetupActivity :
    ViewModelActivity<UserInfoViewModel, ActivityGenderSelectionBinding>() {

    @Autowired(name = TagConstant.FROM_JMB)
    @JvmField
    var fromJMB: Boolean = false

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
        EventBus.getDefault().register(this)
    }

    private val loginView by lazy {
        ViewModelProvider(this, ViewModelFactory<Any, Any?>()).get(LoginViewModel::class.java)
    }

    private var sex = 0
    private var pregnancy = 0
    private var selected = ""
    private var chinaCity: CityList.ChinaCity? = null

    override fun initView(savedInstanceState: Bundle?) {

        registerUIChange(loginView)
      ///  val navController = findNavController(R.id.nav_host_fragment_activity_select)
        binding.apply {
            if (fromJMB) {
                tvTitle.text = "马上开启LesDoB之旅"
                tvTips.text = "选择您所在的城市"
             //   navController.navigate(R.id.navigation_city)
                sbSelectOk.isEnabled = false
                sbSelectOk.post {
                    sbSelectOk.text = R.string.complete.String
                }
                sex = Constant.userInfo.sex
                pregnancy = Constant.userInfo.pregnantStatus
            } else {
                tvTitle.text = "第一步"
                tvTips.text = "选择您的性别"
            }

            sbSelectOk.setOnClickListener {

            }
        }


    }

    override fun initData() {


    }

    override fun getData() {

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.overridePendingTransition(R.anim.activity_background, R.anim.activity_bottom_out)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
        this.overridePendingTransition(R.anim.activity_background, R.anim.activity_bottom_out)
    }

    fun setSex(sex: Int) {
        this.sex = sex
    }

    fun setPregnancy(state: Int) {
        this.pregnancy = state
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun selectCity(event: CityList.ChinaCity) {
        //更新了通知
        chinaCity = event
        checkData()
    }

    private fun checkData() {
        binding.sbSelectOk.isEnabled = chinaCity != null && pregnancy != 0 && sex != 0
    }

}