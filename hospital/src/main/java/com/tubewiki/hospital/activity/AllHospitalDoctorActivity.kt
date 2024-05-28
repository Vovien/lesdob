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
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.init

import com.tubewiki.hospital.R
import com.tubewiki.hospital.adapter.HospitalDoctorAdapter
import com.tubewiki.hospital.databinding.ActivityAllHospitalDoctorBinding
import com.tubewiki.hospital.viewmodel.HospitalFragmentViewModel


/**
 * 全部医院医生
 */
@Route(path = "/hospital/activity/all_hospital_doctor")
class AllHospitalDoctorActivity :
    ViewModelActivity<HospitalFragmentViewModel, ActivityAllHospitalDoctorBinding>() {

    @Autowired(name = TagConstant.HOSPITAL_ID)
    @JvmField
    var hospitalId = 0
    var page = 1


    private val localHospitalAdapter by lazy {
        HospitalDoctorAdapter()
    }

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.all_hospital_doctor))
        initStateLayout(binding.stateLayout)

        binding.smartRefresh.setOnLoadMoreListener {
            page++
            loadHospitalDoctorListData()
        }

        binding.recyclerView?.apply {
            init(
                localHospitalAdapter,
                layoutManager = LinearLayoutManager(
                    this@AllHospitalDoctorActivity,
                    RecyclerView.VERTICAL,
                    false
                )

            )
        }

        localHospitalAdapter.setOnItemClickListener { adapter, view, pos ->
            ArouterUtils.toDoctorDetailsActivity(localHospitalAdapter.getItem(pos).doctorId)
        }


    }


    override fun initData() {
        loadHospitalDoctorListData()
    }

    private fun loadHospitalDoctorListData() {
        started {
            viewModel.hospitalDoctorList(hospitalId, page).netCatch {
                showErrorState()
            }.next {
                showContentState()
                if (page == 1) {
                    localHospitalAdapter.setNewInstance(this.doctors)
                } else {
                    localHospitalAdapter.addData(this.doctors)
                }
                if (this.doctors.size < Constant.PAGE_SIZE) {
                    binding.smartRefresh.finishLoadMoreWithNoMoreData()
                } else {
                    binding.smartRefresh.finishLoadMore()
                }
            }

        }
    }

    override fun getData() {
    }


    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        loadHospitalDoctorListData()

    }


}