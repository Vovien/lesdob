package com.tubewiki.mine.view.city

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.AppBaseFragment
import com.apkdv.mvvmfast.ktx.activityViewModels
import com.apkdv.mvvmfast.ktx.launch
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.utils.SizeUtil
import com.blankj.utilcode.util.ActivityUtils
import com.tubewiki.mine.adapter.city.HotCityListAdapter
import com.tubewiki.mine.adapter.city.SelectCityListAdapter
import com.jmbon.middleware.bean.CityList
import com.jmbon.middleware.bean.HOT
import com.jmbon.middleware.location.DLocationTools
import com.jmbon.middleware.location.DLocationUtils
import com.jmbon.middleware.location.DLocationWhat
import com.jmbon.middleware.location.OnLocationChangeListener
import com.tubewiki.mine.view.login.InitialSetupActivity
import com.jmbon.middleware.utils.*
import com.tubewiki.mine.databinding.FragmentSelectDomesticCityBinding
import com.tubewiki.mine.databinding.LayoutSelectCityTopBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.greenrobot.eventbus.EventBus
import java.util.*

// 国内城市选择
@Route(path = "/mine/domestic/city/select")
class DomesticFragment :
    AppBaseFragment<FragmentSelectDomesticCityBinding>() {


    private val model: SelectCityViewModel by activityViewModels()


    private val mAdapter by lazy { SelectCityListAdapter() }
    private val hotListAdapter by lazy { HotCityListAdapter() }

    private val headerView by lazy { LayoutSelectCityTopBinding.inflate(layoutInflater) }
    override fun initView(view: View) {
        binding.apply {
            sideIndexBar.setOnIndexChangedListener { index, position ->
                mAdapter.scrollToSection(index)
            }

            val arrayList: ArrayList<String> = ArrayList()
            arrayList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            arrayList.add(Manifest.permission.ACCESS_FINE_LOCATION)
            context?.let {
                PermissionUtils.doNeedPermissionAction2(
                    it, arrayList, {
                        //开始定位
                        getLocation()
                    }, {
                        setCity("定位失败")
                    }, "位置",
                    "定位", false
                )
            }

            initCity()
            setHotCity()
            // set header
            setHeaderView(headerView)
            mAdapter.addHeaderView(headerView.root)

        }
    }

    private fun setHeaderView(headerView: LayoutSelectCityTopBinding) {
        headerView.apply {

        }

    }

    private fun initCity() {
        binding.apply {
            val mLayoutManager = LinearLayoutManager(context)
            cityRecyclerview.layoutManager = mLayoutManager
            cityRecyclerview.setHasFixedSize(true)
            cityRecyclerview.layoutParams.height = SizeUtil.getHeight()

            mAdapter.setLayoutManager(mLayoutManager)
            cityRecyclerview.adapter = mAdapter
            mAdapter.setOnItemClickListener { adapter, view, position ->
                setResult(mAdapter.data[position])
            }

        }
    }

    private fun getLocation() {
        DLocationUtils.init(context)
        val state = DLocationUtils.getInstance().register(object :
            OnLocationChangeListener {
            override fun getLastKnownLocation(location: Location?) {
                launch {
                    location?.let {
                        val city = DLocationTools.getLocality(
                            context,
                            location.latitude,
                            location.longitude
                        )
                        setCity(city)
                    }
                }
            }

            override fun onLocationChanged(location: Location?) {
                location?.let {
                    val city = DLocationTools.getLocality(
                        context,
                        location.latitude,
                        location.longitude
                    )
                    setCity(city)
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

        })
        if (state == DLocationWhat.NO_PROVIDER || state == DLocationWhat.NO_LOCATIONMANAGER) {
            "请打开系统定位功能".showToast()
            DLocationTools.openGpsSettings(context)
        }
    }

    private fun setCity(city: String?) {
        headerView.layoutLocation.textLocation.text = city
        headerView.layoutLocation.textLocation.setOnSingleClickListener({
            // 反查
            mAdapter.data.forEach {
                if (it.title == city) {
                    setResult(it)
                    return@forEach
                }
            }
        })
    }

    override fun getData() {
        model.chinaCityList.onEach {
            it?.let { it ->
                if (it.first.isNotNullEmpty()) {
                    // allcity
                    val allCity = arrayListOf<CityList.ChinaCity>()
                    it.first.forEach { city ->
                        if (city.children.isNotNullEmpty()) {
                            city.children.forEach { child ->
                                allCity.add(child)
                            }
                        }
                    }
                    val comparator = CityComparator()
                    Collections.sort(allCity, comparator)
                    mAdapter.setNewInstance(allCity)
                    // 设置 itemDecoration
                    hotListAdapter.setNewInstance(it.second.map { hot ->
                        hot.dataType = HOT
                        hot
                    }.toMutableList())
                    binding.cityRecyclerview.apply {
                        for (i in 0 until itemDecorationCount) {
                            removeItemDecorationAt(i)
                        }
                        addItemDecoration(
                            com.tubewiki.mine.utils.decoration.SectionItemDecoration(
                                allCity
                            ), 0
                        )
                        addItemDecoration(
                            com.tubewiki.mine.utils.decoration.DividerItemDecoration(),
                            1
                        )
                    }
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun setHotCity() {
        headerView.layoutHot.apply {
            cpHotList.layoutManager = GridLayoutManager(
                context,
                3, LinearLayoutManager.VERTICAL, false
            )
            cpHotList.setHasFixedSize(true)
            cpHotList.addItemDecoration(
                com.tubewiki.mine.utils.decoration.GridItemDecoration(
                    3,
                    16f.dp()
                )
            )
            cpHotList.adapter = hotListAdapter
            hotListAdapter.setOnItemClickListener { adapter, view, position ->
                setResult(hotListAdapter.data[position])
            }
        }
    }

    private fun setResult(city: CityList.ChinaCity) {
        EventBus.getDefault().post(city)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        DLocationUtils.getInstance().unregister();
        super.onDestroyView()
    }


    /**
     * sort by a-z
     */
    private class CityComparator : Comparator<CityList.ChinaCity> {
        override fun compare(lhs: CityList.ChinaCity, rhs: CityList.ChinaCity): Int {
            val a: String = lhs.name.substring(0, 1)
            val b: String = rhs.name.substring(0, 1)
            return a.compareTo(b)
        }
    }


}