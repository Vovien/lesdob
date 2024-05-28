package com.tubewiki.hospital.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseFragment
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.databinding.DefaultViewstatusBoxEmptyBinding
import com.apkdv.mvvmfast.ktx.activityViewModels
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.utils.saveParcelable
import com.blankj.utilcode.util.ColorUtils
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.bean.CityList
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.MMKVKey
import com.jmbon.middleware.search.SearchMessageViewModel
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.SimpleCallback
import com.tubewiki.hospital.R
import com.tubewiki.hospital.adapter.LocalHospitalAdapter
import com.tubewiki.hospital.databinding.FragmentSearchHospitalLayoutBinding
import com.tubewiki.hospital.dialog.SelectAllSortPopDialog
import com.tubewiki.hospital.dialog.SelectHospitalLevelDialog
import com.tubewiki.hospital.viewmodel.HospitalFragmentViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

// 医院真正搜索的界面
@Route(path = "/hospital/fragment/search_hospital")
class SearchHospitalFragment : AppBaseFragment<FragmentSearchHospitalLayoutBinding>() {

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

    @Autowired(name = TagConstant.SEARCH_CONTENT_BUNDLE)
    @JvmField
    var subBundle: Bundle? = null

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
        EventBus.getDefault().register(this)

    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    private val searchModel: SearchMessageViewModel by activityViewModels()
    lateinit var searchDataModel: HospitalFragmentViewModel
    private var isSearchPage: Boolean = false
    private val localHospitalAdapter by lazy { LocalHospitalAdapter() }
    override fun initView(view: View) {
        isSearchPage = subBundle?.getBoolean(TagConstant.PAGE_TYPE, false) ?: false
        hasLocal = subBundle?.getInt("hasLocal", 0) ?: 0
        lng = subBundle?.getDouble("lng", 0.0) ?: 0.0
        lat = subBundle?.getDouble("lat", 0.0) ?: 0.0
//        cityId = subBundle?.getInt("cityId", 0) ?: 0
//        cityPinyin = subBundle?.getString("cityPinyin", "") ?: ""
//        cityName = subBundle?.getString("cityName", "") ?: ""

        if (cityName.isNotNullEmpty()) {
            binding.tvCity.text = cityName
            binding.tvCity.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
            binding.tvCity.setTextColor(ColorUtils.getColor(R.color.color_262626))
        }

        if (isSearchPage) {
            searchDataModel = ViewModelProvider(viewModelStore, ViewModelFactory<Any, Any?>()).get(
                HospitalFragmentViewModel::class.java
            )
            searchModel.showCancel.value = false
            searchModel.showBack.value = true
            searchModel.makeCancelTextLikeSearch.value = true
            searchModel.cancelText.value = "搜索"
            searchModel.editHint.value = "疾病、医院、科室"

            searchModel.searchKey.onEach {
                if (it.isNotNullEmpty()) {
                    keyword = it
                    page = 1
                    binding.smartRefresh.resetNoMoreData()
                    startSearch()
                }
            }.launchIn(lifecycleScope)
           // binding.cityRecyclerview.setBackgroundColor(Color.WHITE)
        }


        binding.apply {
            cityRecyclerview.init(
                localHospitalAdapter,
                layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false),
                dividerHeight = 16f,
                dividerColor = ColorUtils.getColor(R.color.color_fa)
                    , vertical = false
            )


            var viewBinding =
                DefaultViewstatusBoxEmptyBinding.inflate(LayoutInflater.from(requireContext()))

            viewBinding.stateLayoutEmptyHint.text = "抱歉，没有匹配到医院"
            viewBinding.root.setBackgroundColor(ColorUtils.getColor(R.color.color_fa))
            localHospitalAdapter.setEmptyView(viewBinding.root)

            localHospitalAdapter.setOnItemClickListener { _, view, position ->
                ArouterUtils.toHospitalDetailsActivity(localHospitalAdapter.getItem(position).id)
            }


            smartRefresh.setOnLoadMoreListener {
                page++
                startSearch()
            }

            llAllSort.setOnSingleClickListener({
                if (selectAllSortPopDialog?.isShow == true) {
                    return@setOnSingleClickListener
                }
                binding.ivAllSort.animate()?.rotation(180f)
                showAllSortDialog()

            })

            llHospitalLevel.setOnSingleClickListener({
                if (selectHospitalLevelDialog?.isShow == true) {
                    return@setOnSingleClickListener
                }
                binding.ivHospitalLevel.animate()?.rotation(180f)
                showHospitalLevelDialog()
            })


            llCity.setOnSingleClickListener({
                ARouter.getInstance().build("/middleware/search/activity")
                    .withString(TagConstant.SEARCH_CONTENT, "/mine/city/select")
                    .withBoolean(TagConstant.DIRECT_SEARCH, false)
                    .withBoolean(TagConstant.CAN_SHOW_KEYBOARD, false)
                    .navigation()
            })

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun selectCity(event: CityList.ChinaCity) {
        if (cityId == event.id) {
            return
        }
        page = 1
        hasLocal = 1 //手动选定位也是1
        binding.smartRefresh.resetNoMoreData()
        // 选择了城市，重新刷新页面
        cityId = event.id
        cityPinyin = event.name
        binding.tvCity.text = event.title
        binding.tvCity.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        binding.tvCity.setTextColor(ColorUtils.getColor(R.color.color_262626))
        startSearch()
    }

    override fun getData() {

    }

    fun startSearch() {
        if (isSearchPage) {
            started {
                searchDataModel.hospitalList(
                    keyword,
                    type,
                    levelIds,
                    lng,
                    lat,
                    hasLocal,
                    cityId,
                    cityPinyin,
                    page
                ).next {

                    if (page == 1) {
                        localHospitalAdapter.setNewInstance(this.hospitals)
                        binding.smartRefresh.finishRefresh()
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

    }


    private var selectAllSortPopDialog: SelectAllSortPopDialog? = null
    private fun showAllSortDialog() {
        selectAllSortPopDialog = SelectAllSortPopDialog(requireContext(), type, true) {
            page = 1
            binding.smartRefresh.resetNoMoreData()
            type = it
            startSearch()

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
            .enableDrag(false)
            .setPopupCallback(object : SimpleCallback() {
                override fun onDismiss(popupView: BasePopupView?) {
                    binding.ivAllSort.animate()?.rotation(0f)
                }
            })
            .asCustom(selectAllSortPopDialog).show()
    }


    private var selectHospitalLevelDialog: SelectHospitalLevelDialog? = null
    private fun showHospitalLevelDialog() {
        selectHospitalLevelDialog = SelectHospitalLevelDialog(requireContext(), levelIds, true) {
            page = 1
            binding.smartRefresh.resetNoMoreData()
            levelIds = it
            startSearch()

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
            .enableDrag(false)
            .setPopupCallback(object : SimpleCallback() {
                override fun onDismiss(popupView: BasePopupView?) {
                    binding.ivHospitalLevel.animate()?.rotation(0f)
                }

            })
            .asCustom(selectHospitalLevelDialog).show()
    }

}