package com.jmbon.minitools.recommend.activity

import android.graphics.Color
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.blankj.utilcode.util.ActivityUtils
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.adapter.CommonFragmentAdapter
import com.jmbon.middleware.arouter.service.IHomeProvider
import com.jmbon.middleware.bean.event.CommonEventHub
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.navigationWithFinish
import com.jmbon.minitools.databinding.ActivityChooseContainerLayoutBinding
import com.jmbon.minitools.recommend.bean.FormTypeEnum
import com.jmbon.minitools.recommend.bean.ItemTypeBean
import com.jmbon.minitools.recommend.fragment.ChooseBudgetFragment
import com.jmbon.minitools.recommend.fragment.ChooseCommonFragment
import com.jmbon.minitools.recommend.fragment.ChooseTechnicalFragment
import com.jmbon.minitools.recommend.router.RecommendRouter
import com.jmbon.minitools.recommend.router.RequirementTypeEnum
import com.jmbon.minitools.recommend.viewmodel.ChooseViewModel
import com.qmuiteam.qmui.kotlin.onClick
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/******************************************************************************
 * Description: 选择容器页
 *
 * Author: jhg
 *
 * Date: 2023/5/30
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Route(path = RecommendRouter.RECOMMEND_CHOOSE_CONTAINER)
class ChooseContainerActivity :
    ViewModelActivity<ChooseViewModel, ActivityChooseContainerLayoutBinding>() {

    @Autowired
    @JvmField
    var formInfo: ItemTypeBean? = null

    /**
     * 性别表单的Key
     */
    private val genderKey = "sex"

    private val pageChangedListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.tvSkip.isVisible =
                formInfo?.form?.filter { it.type != FormTypeEnum.FORM_TYPE_ADDRESS.value }
                    ?.getOrNull(position)?.skip_status == 1
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .statusBarColorInt(Color.TRANSPARENT)
            .statusBarDarkFont(true)
            .transparentNavigationBar()
            .init()
        binding.apply {
            vpContent.apply {
                isUserInputEnabled = false
                currentItem = 0
                val fragmentList = getFragmentList()
                if (fragmentList.isNotEmpty()) {
                    binding.llOperate.isVisible = true
                }
                offscreenPageLimit = fragmentList.size
                adapter = CommonFragmentAdapter(this@ChooseContainerActivity, fragmentList)
            }

            tvSkip.onClick {
                viewModel.getRecommendResult()
            }

            tvNext.onClick {
                val currentItem = vpContent.currentItem
                val lastItemIndex = (vpContent.adapter?.itemCount ?: 1) - 1
                if (currentItem < lastItemIndex) {
                    if (needSkip(currentItem)) {
                        vpContent.setCurrentItem(currentItem + 2, false)
                    } else {
                        vpContent.currentItem = currentItem + 1
                    }
                    if (vpContent.currentItem == lastItemIndex) {
                        binding.tvNext.text = formInfo?.last_button_word ?: ""
                    }
                } else {
                    viewModel.getRecommendResult()
                    Constant.savePregnantStatus(formInfo?.value ?: 0)
                }
            }
            tvPre.onClick {
                val currentItem = vpContent.currentItem
                if (currentItem == 0) {
                    // TODO 需确认是关闭还是禁用
                    finish()
                } else {
                    // 如果当前是我要试管流程, 且当前表单前面第二个表单是性别, 并且选择了男性
                    if (needSkip(currentItem - 2)) {
                        vpContent.currentItem = currentItem - 2
                    } else {
                        vpContent.currentItem = currentItem - 1
                    }
                    binding.tvNext.text = "下一步"
                }
            }

            vpContent.registerOnPageChangeCallback(pageChangedListener)
        }
    }

    override fun initData() {

    }

    override fun getData() {

    }

    override fun initViewModel() {
        super.initViewModel()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        ARouter.getInstance().inject(this)
        if (formInfo?.form.isNullOrEmpty()) {
            ARouter.getInstance().build("/main/main").navigationWithFinish(this)
            return
        }

        viewModel.paramsMap["type"] = formInfo?.value
        if (formInfo?.value == RequirementTypeEnum.REQUIREMENT_TYPE_TEST_TUBE.value) {
            // 此处获取位置只是为了处理用户未选择城市时, 使用默认的定位位置
            viewModel.startLocation()
        }

        // 监听推荐结果
        viewModel.recommendResultLD.observe(this) {
            ActivityUtils.finishActivity(ChooseHomeActivity::class.java)
            if (it == null) {
                ARouter.getInstance().build("/main/main").navigationWithFinish(this)
            } else {
                if (it.guide_page_type == 2 && (!it.guide_page_img.isNullOrBlank() || !it.guide_button_list.isNullOrEmpty())) {
                    RecommendRouter.toTubeGuide(it.guide_page_id, it.guide_page_img, it.guide_button_list)
                } else if (it.guide_page_type == 1 && !it.originality_list.isNullOrEmpty()){
                    RecommendRouter.toRecommendResult(this, it.originality_list)
                } else {
                    ARouter.getInstance().build("/main/main").navigationWithFinish(this)
                }
            }
        }
    }

    /**
     * 指定位置是否是跳过操作
     */
    private fun needSkip(position: Int): Boolean {
        // v6.2.1取消性别判断逻辑, 所有直接屏蔽
        return false
        // 在我要试管流程, 如果性别表单选择了男性, 则需要跳过后面一个表单
//        return formInfo?.value == RequirementTypeEnum.REQUIREMENT_TYPE_TEST_TUBE.value
//                && position >= 0
//                && formInfo?.form?.get(position)?.key == genderKey
//                && viewModel.paramsMap[genderKey] == 1
    }

    private fun getFragmentList(): List<Fragment> {
        return mutableListOf<Fragment>().apply {
            formInfo?.form?.forEach {
                when (it.type) {
                    FormTypeEnum.FORM_TYPE_ADDRESS.value -> {
                        ARouter.getInstance().navigation(IHomeProvider::class.java)?.toChooseCity(false)
                    }

                    FormTypeEnum.FORM_TYPE_MULTI.value -> {
                        ChooseTechnicalFragment.newInstance(it)
                    }

                    FormTypeEnum.FORM_TYPE_COST_MAP.value -> {
                        ChooseBudgetFragment()
                    }

                    else -> {
                        ChooseCommonFragment.newInstance(it)
                    }
                }.apply {
                    if (this is Fragment) {
                        add(this)
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: CommonEventHub.ChangeCityEvent) {
        if (event.cityName.isNotEmpty()) {
            viewModel.paramsMap["city_name"] = event.cityName
        } else {
            if (event.countryName.isNotEmpty()) {
                viewModel.paramsMap["city_name"] = event.countryName
            }
        }
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        binding.vpContent.unregisterOnPageChangeCallback(pageChangedListener)
        super.onDestroy()
    }
}