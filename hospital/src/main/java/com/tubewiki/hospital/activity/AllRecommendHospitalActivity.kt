package com.tubewiki.hospital.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.started
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.utils.init

import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.tubewiki.hospital.R
import com.tubewiki.hospital.adapter.LocalHospitalAdapter
import com.tubewiki.hospital.databinding.ActivityAllRecommendHospitalBinding
import com.tubewiki.hospital.viewmodel.HospitalFragmentViewModel


/**
 * 全部推荐医院
 */
@Route(path = "/hospital/activity/all_recommend_hospital")
class AllRecommendHospitalActivity :
    ViewModelActivity<HospitalFragmentViewModel, ActivityAllRecommendHospitalBinding>() {

    @Autowired
    @JvmField
    var lng = 0.0

    @Autowired
    @JvmField
    var lat = 0.0

    @Autowired
    @JvmField
    var hasLocal = 0

    @Autowired
    @JvmField
    var cityId = 0

    @Autowired
    @JvmField
    var cityPinyin = ""


    private val localHospitalAdapter by lazy {
        LocalHospitalAdapter()
    }

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.recommend_hospital))
        initStateLayout(binding.stateLayout)

        binding.recyclerView?.apply {
            init(
                localHospitalAdapter,
                layoutManager = LinearLayoutManager(
                    this@AllRecommendHospitalActivity,
                    RecyclerView.VERTICAL,
                    false
                )

            )
        }

        localHospitalAdapter.setOnItemClickListener { adapter, view, pos ->
            ArouterUtils.toHospitalDetailsActivity(localHospitalAdapter.getItem(pos).id)
        }


    }

    override fun initData() {
        recommendHospitalData()
    }

    private fun recommendHospitalData(

    ) {
        started {
            viewModel.hospitalRecommend(lng, lat, hasLocal, cityId, cityPinyin).netCatch {

                showErrorState()
            }.next {
                showContentState()
                if (this.data.isNullOrEmpty())
                    showEmptyState()
                else
                    localHospitalAdapter.setNewInstance(this.data)
            }
        }
    }

    override fun getData() {
    }


    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        recommendHospitalData()

    }


}