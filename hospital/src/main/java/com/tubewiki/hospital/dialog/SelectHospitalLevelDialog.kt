package com.tubewiki.hospital.dialog

import android.content.Context
import com.apkdv.mvvmfast.base.dialog.BasePartShadowPopupView
import com.blankj.utilcode.util.ColorUtils
import com.lxj.xpopup.util.XPopupUtils
import com.tubewiki.hospital.R
import com.tubewiki.hospital.databinding.DialogHospitalLevelSelectBinding


class SelectHospitalLevelDialog(
    context: Context,
    var level: Int,
    var isWhiteBg: Boolean = false,
    var result: (Int) -> Unit
) :
    BasePartShadowPopupView<DialogHospitalLevelSelectBinding>(context) {


    override fun onCreate() {
        super.onCreate()

        if (!isWhiteBg) {
            binding.root.setBackgroundColor(ColorUtils.getColor(R.color.color_fa))
        }


        when (level) {
            1 -> {
                binding.rgSort.check(R.id.rb_unusual)
            }
            2 -> {
                binding.rgSort.check(R.id.rb_like)
            }
            3 -> {
                binding.rgSort.check(R.id.rb_collections)
            }
        }


        binding.rgSort.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_unusual -> level = 1
                R.id.rb_like -> level = 2
                R.id.rb_collections -> level = 3
            }
        }

        binding.sbReSet.setOnClickListener {
            result(0)
            dismiss()

        }
        binding.sbOk.setOnClickListener {
            result(level)
            dismiss()
        }
    }

}