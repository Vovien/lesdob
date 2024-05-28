package com.tubewiki.hospital.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.launch
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.resumed
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.apkdv.mvvmfast.utils.getParcelable
import com.apkdv.mvvmfast.utils.saveParcelable
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jmbon.middleware.adapter.ImageCommonBannerAdapter
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.bean.CityList
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.bury.BuryHelper
import com.jmbon.middleware.bury.db.BuryPointInfo
import com.jmbon.middleware.bury.event.ClickEventEnum
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.MMKVKey
import com.jmbon.middleware.location.DLocationTools
import com.jmbon.middleware.location.DLocationUtils
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.load
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.middleware.utils.toWxMiniProgram
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.SimpleCallback
import com.tubewiki.hospital.R
import com.tubewiki.hospital.adapter.LocalHospitalAdapter
import com.tubewiki.hospital.adapter.RecommendHospitalAdapter
import com.tubewiki.hospital.databinding.FragmentHospitalLayoutBinding
import com.tubewiki.hospital.dialog.SelectAllSortPopDialog
import com.tubewiki.hospital.dialog.SelectHospitalLevelDialog
import com.tubewiki.hospital.viewmodel.HospitalFragmentViewModel
import com.youth.banner.indicator.CircleIndicator
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.math.abs


/**
 * 医院
 */
@Route(path = "/hospital/main/fragment")
class HospitalFragment :
    ViewModelFragment<HospitalFragmentViewModel, FragmentHospitalLayoutBinding>() {

    private val bannerAdapter by lazy {
        ImageCommonBannerAdapter {
            BuryHelper.addEvent(ClickEventEnum.EVENT_HOSPITAL_HOME_NARROW_BANNER.value)
        }
    }


    private val lastCity by lazy {
        MMKVKey.APP_LOCATION.getParcelable<CityList.ChinaCity>(null)
    }

    var type = 1 // type：1.综合排序，2.医院等级排序，3.入驻医生数排序,默认综合排序
    var levelIds = 0
    var page = 1

    var keyword: String = ""
    var lng: Double = 0.0
    var lat: Double = 0.0
    var hasLocal: Int = 0
    var cityId: Int = 0
    var cityPinyin: String = ""
    var cityName: String = ""

    var isStartSearch = false


    private val recommendHospitalAdapter by lazy {
        RecommendHospitalAdapter()
    }
    private val localHospitalAdapter by lazy {
        LocalHospitalAdapter()
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
        initStateLayout(binding.stateLayout)

        var maxHeight = SizeUtils.dp2px(80f).toFloat()

        binding.appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            Log.e("TAG", "${verticalOffset}")
            var rate = abs(verticalOffset) / maxHeight

            binding.ivSearch.alpha = rate
            binding.ivSearch.visibility = if (rate > 0.2) View.VISIBLE else View.GONE

        }


        lastCity?.let {
            hasLocal = 1
            binding.tvCity.text = it.title
            cityId = it.id
            cityPinyin = it.name
            cityName = it.title
        }

        initRecyclerView()

        initListener()


    }

    private fun initListener() {

        binding.smartRefresh.setOnLoadMoreListener {

            page++
            localHospitalData()
        }
        binding.smartRefresh.setOnRefreshListener {
            page = 1
            binding.smartRefresh.resetNoMoreData()
            localHospitalData()
        }

        binding.banner.apply {
            indicator = CircleIndicator(context)
            setAdapter(bannerAdapter, true)
        }

        binding?.apply {

            llSearch.setOnSingleClickListener({
                toSearchPage()
            })
            ivSearch.setOnSingleClickListener({
                toSearchPage()
            })


            tvCity.setOnSingleClickListener({
                ARouter.getInstance().build("/middleware/search/activity")
                    .withString(TagConstant.SEARCH_CONTENT, "/mine/city/select")
                    .withBoolean(TagConstant.DIRECT_SEARCH, false)
                    .withBoolean(TagConstant.CAN_SHOW_KEYBOARD, false)
                    .navigation()
            })

            tvAll.setOnSingleClickListener({
                ARouter.getInstance().build("/hospital/activity/all_recommend_hospital")
                    .withDouble("lng", lng)
                    .withDouble("lat", lat)
                    .withInt("hasLocal", hasLocal)
                    .withInt("cityId", cityId).withString("cityPinyin", cityPinyin).navigation()
            })


            llAllSort.setOnSingleClickListener({
                if (selectAllSortPopDialog?.isShow == true) {
                    return@setOnSingleClickListener
                }
                llAllSort.isEnabled = false
                appbar.setExpanded(false)
                appbar.postDelayed({
                    binding.ivAllSort.animate()?.rotation(180f)
                    showAllSortDialog()
                }, 200)
            })

            llHospitalLevel.setOnSingleClickListener({
                if (selectHospitalLevelDialog?.isShow == true) {
                    return@setOnSingleClickListener
                }
                llHospitalLevel.isEnabled = false
                appbar.setExpanded(false)
                appbar.postDelayed({
                    binding.ivHospitalLevel.animate()?.rotation(180f)
                    showHospitalLevelDialog()
                }, 200)
            })

        }

    }

    private fun toSearchPage() {
        val bundle = Bundle()
        bundle.putBoolean(TagConstant.PAGE_TYPE, true)
        bundle.putInt("hasLocal", hasLocal)
        bundle.putDouble("lng", lng)
        bundle.putDouble("lat", lat)
        bundle.putInt("cityId", cityId)
        bundle.putString("cityPinyin", cityPinyin)
        bundle.putString("cityName", cityName)
        ARouter.getInstance().build("/middleware/search/activity")
            .withString(TagConstant.SEARCH_CONTENT, "/hospital/fragment/search_hospital")
            .withBundle(TagConstant.SEARCH_CONTENT_BUNDLE, bundle)
            .withBoolean(TagConstant.DIRECT_SEARCH, false)
            .navigation()
        isStartSearch = true
    }

    private fun initRecyclerView() {

        binding.recycleHospital?.apply {
            init(
                recommendHospitalAdapter,
                layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            )
        }
        binding.recycleLocal?.apply {
            init(
                localHospitalAdapter,
                layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false),
                dividerHeight = 16f,
                dividerColor = ColorUtils.getColor(R.color.color_fa),
                vertical = false
            )
        }

        recommendHospitalAdapter.setEmptyView(R.layout.default_viewstatus_box_empty3)
        localHospitalAdapter.setEmptyView(R.layout.default_viewstatus_box_empty)

        recommendHospitalAdapter.setOnItemClickListener { adapter, view, pos ->
            ArouterUtils.toHospitalDetailsActivity(recommendHospitalAdapter.getItem(pos).id)
        }
        localHospitalAdapter.setOnItemClickListener { adapter, view, pos ->
            ArouterUtils.toHospitalDetailsActivity(localHospitalAdapter.getItem(pos).id)
            //  ArouterUtils.toHospitalDetailsActivity(696)
        }
    }


    override fun getData() {
        super.getData()

        //适配小米
        this.hasLocal = 0
        localHospitalData()

        started {
            viewModel.getGuideBanner().next {
                val banners = this.map {
                    CommonBanner(
                        img = it.cover,
                        identity = it.identity.toString(),
                        item_type = it.itemType
                    )
                }
                bannerAdapter.setDatas(banners)
            }
        }

        resumed {
            context?.let { c ->
                viewModel.getLocation(c, { city, latitude, longitude ->
                    if (lastCity == null) {
                        binding.tvCity.text = city
                        cityName = city

                    }
                    DLocationUtils.getInstance().unregister()
                    this@HospitalFragment.lng = longitude
                    this@HospitalFragment.lat = latitude
                    this@HospitalFragment.hasLocal = 1
                    // recommendHospitalData(longitude, latitude, 1)
                    page = 1
                    binding.smartRefresh.resetNoMoreData()
                    localHospitalData()

                    updateUserInfo(city)

                }, {
                    if (lastCity == null) {
                        binding.tvCity.text = "点击获取定位"
                    }
                    this@HospitalFragment.hasLocal = 0
                    // recommendHospitalData(0.0, 0.0, 0)
                    localHospitalData()
                })
            }
        }

    }


    private fun localHospitalData(
    ) {
        started {
            viewModel.hospitalList(
                keyword,
                type,
                levelIds,
                lng,
                lat,
                hasLocal,
                cityId,
                cityPinyin,
                page
            ).netCatch {
                message.showToast()
                binding.smartRefresh.finishLoadMore()
                showErrorState()
            }.next {
                showContentState()
                if (page == 1) {
                    localHospitalAdapter.setNewInstance(this.hospitals)
                    recommendHospitalAdapter.setNewInstance(this.recommendHospitals)
                    binding.smartRefresh.finishRefresh()

                    if (this.hospitals.isNullOrEmpty()) {
                        binding.smartRefresh.setEnableLoadMore(false)
                    }
                } else {
                    localHospitalAdapter.addData(this.hospitals)
                }

                if (this.hospitals.size < Constant.PAGE_SIZE) {
                    binding.smartRefresh.finishLoadMoreWithNoMoreData()
                } else {
                    binding.smartRefresh.finishLoadMore()
                }

            }
        }
    }

    /**
     * 更新用户定位
     */
    private fun updateUserInfo(city: String) {
        started {
            var infoMap = hashMapOf<String, String>()
            infoMap["firstLocate"] = city
            viewModel.uploadInfo(infoMap).netCatch {

            }.next {

            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun selectCity(event: CityList.ChinaCity) {
        //搜索页面定位不回调
        if (isStartSearch || cityId == event.id) {
            return
        }

        //选择城市重置条件
        type = 1
        levelIds = 0
        hasLocal = 1

        binding.tvHospitalLevel.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        binding.tvHospitalLevel.setTextColor(resources.getColor(R.color.color_7F7F7F))


        page = 1
        binding.smartRefresh.resetNoMoreData()
        binding.smartRefresh.setEnableLoadMore(true)
        // 选择了城市，重新刷新页面
        cityId = event.id
        cityPinyin = event.name
        binding.tvCity.text = event.title
        localHospitalData()
        event.saveParcelable(MMKVKey.APP_LOCATION)
    }

    override fun onResume() {
        super.onResume()
        StatusBarCompat.setLightStatus(activity?.window)
        isStartSearch = false
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        page = 1

        getData()
    }

    private var selectAllSortPopDialog: SelectAllSortPopDialog? = null
    private fun showAllSortDialog() {
        selectAllSortPopDialog = SelectAllSortPopDialog(requireContext(), type, false) {
            page = 1
            binding.smartRefresh.resetNoMoreData()
            type = it
            localHospitalData()

            when (it) {
                1 -> binding.tvAllSort.text = "综合排序"
                2 -> binding.tvAllSort.text = "医院等级"
                3 -> binding.tvAllSort.text = "专家最多"
            }
        }
        XPopup.Builder(requireContext())
            .atView(binding.llAllSort)
            .isClickThrough(true)
            .autoOpenSoftInput(false)
            .isDestroyOnDismiss(true)
            .dismissOnTouchOutside(true)
            .dismissOnBackPressed(true)
            .autoDismiss(true)
            .animationDuration(200)
            .enableDrag(false)
            .setPopupCallback(object : SimpleCallback() {
                override fun onDismiss(popupView: BasePopupView?) {
                    binding.ivAllSort.animate()?.rotation(0f)
                    binding.llAllSort.isEnabled = true
                }
            })
            .asCustom(selectAllSortPopDialog).show()
    }


    private var selectHospitalLevelDialog: SelectHospitalLevelDialog? = null
    private fun showHospitalLevelDialog() {
        selectHospitalLevelDialog = SelectHospitalLevelDialog(requireContext(), levelIds, false) {
            page = 1
            binding.smartRefresh.resetNoMoreData()
            levelIds = it
            localHospitalData()

            //选择等级
            if (it != 0) {
                binding.tvHospitalLevel.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                binding.tvHospitalLevel.setTextColor(resources.getColor(R.color.color_262626))
            } else {
                binding.tvHospitalLevel.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                binding.tvHospitalLevel.setTextColor(resources.getColor(R.color.color_7F7F7F))
            }

        }

        XPopup.Builder(requireContext())
            .atView(binding.llHospitalLevel)
            .isClickThrough(true)
            .autoOpenSoftInput(false)
            .isDestroyOnDismiss(true)
            .dismissOnTouchOutside(true)
            .dismissOnBackPressed(true)
            .autoDismiss(true)
            .animationDuration(200)
            .enableDrag(false)
            .setPopupCallback(object : SimpleCallback() {
                override fun onDismiss(popupView: BasePopupView?) {
                    binding.ivHospitalLevel.animate()?.rotation(0f)
                    binding.llHospitalLevel.isEnabled = true
                }

            })
            .asCustom(selectHospitalLevelDialog).show()
    }

}