package com.tubewiki.hospital.dialog

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.apkdv.mvvmfast.base.dialog.BaseBottomDialog
import com.jmbon.middleware.bean.WebScrollOffset
import com.jmbon.middleware.utils.init
import com.jmbon.widget.recyclerview.CenterLayoutManager
import com.tubewiki.hospital.adapter.HospitalDoctorTitleListAdapter
import com.tubewiki.hospital.databinding.DialogHospitalDoctorTitleListLayoutBinding
import com.tubewiki.hospital.databinding.DialogMoreDetailLayoutBinding

/**
 * @author : leimg
 * time   : 2022/8/11
 * desc   :
 * version: 1.0
 */
class HospitalDoctorTitleListDialog(
    var mContext: Context,
    var title: String,
    var titles: MutableList<WebScrollOffset>,
    var selectedPos: Int,
    var result: (WebScrollOffset, Int) -> Unit
) :
    BaseBottomDialog<DialogHospitalDoctorTitleListLayoutBinding>(mContext) {

    val titleAdapter by lazy {
        HospitalDoctorTitleListAdapter()
    }

    override fun onCreate() {
        super.onCreate()

        binding.tvTitle.text = title


        var mCenterLayoutManager =
            CenterLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)


        binding.recycleTitle.init(
            titleAdapter, mCenterLayoutManager
        )


        titleAdapter.setNewInstance(titles)
        titleAdapter.selectedPos = selectedPos
        titleAdapter.notifyDataSetChanged()

        titleAdapter.setOnItemClickListener { adapter, view, pos ->
            result(titleAdapter.data[pos], pos)
            dismiss()
        }

    }

    override fun doAfterShow() {
        super.doAfterShow()
        binding.recycleTitle.smoothScrollToPosition(selectedPos)
    }
}