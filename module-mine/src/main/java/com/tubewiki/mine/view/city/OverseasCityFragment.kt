package com.tubewiki.mine.view.city

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseFragment
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.ktx.activityViewModels
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.started
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.tubewiki.mine.adapter.city.OverseasAdapter
import com.jmbon.middleware.bean.CityList
import com.jmbon.middleware.search.SearchContentActivity
import com.tubewiki.mine.view.login.InitialSetupActivity
import com.jmbon.middleware.search.SearchMessageViewModel
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.isNotNullEmpty
import com.tubewiki.mine.databinding.FragmentSelectOverseasCityBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.greenrobot.eventbus.EventBus

// 城市选择、城市真正搜索的界面
@Route(path = "/mine/overseas/city/select")
class OverseasCityFragment : AppBaseFragment<FragmentSelectOverseasCityBinding>() {

    @Autowired(name = TagConstant.SEARCH_CONTENT_BUNDLE)
    @JvmField
    var subBundle: Bundle? = null

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }


    private val searchModel: SearchMessageViewModel by activityViewModels()
    lateinit var searchDataModel: SelectCityViewModel
    private var isSearchPage: Boolean = false
    private val model: SelectCityViewModel by activityViewModels()
    private val adapter by lazy { OverseasAdapter() }
    override fun initView(view: View) {
        isSearchPage = subBundle?.getBoolean(TagConstant.PAGE_TYPE, false) ?: false
        if (isSearchPage) {
            searchDataModel = ViewModelProvider(viewModelStore, ViewModelFactory<Any, Any?>()).get(
                SelectCityViewModel::class.java
            )
            searchModel.showCancel.value = false
            searchModel.showBack.value = true
            searchModel.makeCancelTextLikeSearch.value = true
            searchModel.cancelText.value = "搜索"
            searchModel.editHint.value = "请输入城市名称"

            searchModel.searchKey.onEach {
                if (it.isNotNullEmpty()) {
                    findSearchCity(it)
                }
            }.launchIn(lifecycleScope)
            binding.cityRecyclerview.setBackgroundColor(Color.WHITE)
        }


        binding.apply {
            cityRecyclerview.init(
                adapter, dividerHeight = 1f, dividerColor = Color.parseColor("#08000000"),
                left = 20f, right = 20f, vertical = false, showEnd = true
            )
            cityRecyclerview.setHasFixedSize(true)
            adapter.setOnItemClickListener { _, view, position ->
                EventBus.getDefault().post(adapter.data[position])
                ActivityUtils.finishActivity(SearchContentActivity::class.java)
                requireActivity()?.finish()
            }
        }

    }

    private fun findSearchCity(key: String) {
        allCity.filter { it.title.contains(key) }.let {
            adapter.setNewInstance(it.toMutableList())
            activity?.let { it1 -> KeyboardUtils.hideSoftInput(it1) }
        }
    }

    val allCity = arrayListOf<CityList.ChinaCity>()

    override fun getData() {
        if (isSearchPage) {
            started {
                searchDataModel.getCityList().next {
                    chinaCityList.forEach { city ->
                        if (city.children.isNotNullEmpty()) {
                            city.children.forEach { child ->
                                allCity.add(child)
                            }
                        }
                    }
                    allCity.addAll(this.overseasCountryList)
                }
            }

        } else {
            model.overseasCountryList.onEach {
                it?.let {
                    if (it.isNotNullEmpty()) {
                        adapter.setNewInstance(it)
                    }
                }
            }.launchIn(lifecycleScope)
        }

    }


}