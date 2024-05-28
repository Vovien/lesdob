package com.jmbon.widget.dialog.pregnant

import android.content.Context
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.dialog.BaseBottomDialog
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.jmbon.widget.R
import com.jmbon.widget.arouter.MobEventService
import com.jmbon.widget.databinding.DialogSelectPregnantStateBinding
import com.lihang.ShadowLayout

/**
 * @author : wangzhen
 * time   : 2021/3/25
 * desc   : 选择怀孕状态
 * version: 1.0
 */
class SelectPregnantDialog(
    context: Context,
    var haveTestTube: Boolean = false,
    var selectedStatus: String?,
    var confirm: (type: String) -> Unit
) : BaseBottomDialog<DialogSelectPregnantStateBinding>(context) {

    private val mobEventService by lazy {
        ARouter.getInstance().build("/middleware/mobevent/service").navigation() as MobEventService
    }

    private var selected = selectedStatus ?: ""
    private var selectedValue = 0
    override fun onCreate() {

        if (haveTestTube) {
            binding.flShiguanPregnant.visible()
        } else {
            binding.flShiguanPregnant.gone()
        }

        when (selectedStatus) {
            "试管助孕", "试管婴儿" -> {
                selectedValue = 4
                setSelectView(binding.flShiguanPregnant, binding.tvShiguanPregnantState, true)
                binding.ivShiguanPregnant.setImageResource(R.drawable.icon_baby_tube_status_selected)
            }
            "备孕中" -> {
                selectedValue = 1
                setSelectView(binding.flReadyPregnant, binding.tvReadyPregnantState, true)
                binding.ivReadyPregnant.setImageResource(R.drawable.icon_ready_pregnant_selected)

            }
            "已怀孕" -> {
                selectedValue = 2
                setSelectView(binding.flHavePregnant, binding.tvHavePregnantState, true)
                binding.ivHavePregnant.setImageResource(R.drawable.icon_pregnant_ing_selected)
            }
            "有宝宝" -> {
                selectedValue = 3
                setSelectView(binding.flHaveBaby, binding.tvHaveBaby, true)
                binding.ivHaveBaby.setImageResource(R.drawable.icon_pregnant_have_baby_selected)
            }
        }

        binding.flShiguanPregnant.setOnClickListener {
            hideImg()
            selectedValue = 4
            selected = binding.tvShiguanPregnantState.text.toString()
            setSelectView(binding.flHavePregnant, binding.tvHavePregnantState, false)
            setSelectView(binding.flReadyPregnant, binding.tvReadyPregnantState, false)
            setSelectView(binding.flHaveBaby, binding.tvHaveBaby, false)
            setSelectView(binding.flShiguanPregnant, binding.tvShiguanPregnantState, true)
            binding.ivShiguanPregnant.setImageResource(R.drawable.icon_baby_tube_status_selected)
        }
        binding.flHaveBaby.setOnClickListener {
            hideImg()
            selectedValue = 3
            selected = binding.tvHaveBaby.text.toString()
            setSelectView(binding.flHavePregnant, binding.tvHavePregnantState, false)
            setSelectView(binding.flReadyPregnant, binding.tvReadyPregnantState, false)
            setSelectView(binding.flHaveBaby, binding.tvHaveBaby, true)
            setSelectView(binding.flShiguanPregnant, binding.tvShiguanPregnantState, false)
            binding.ivHaveBaby.setImageResource(R.drawable.icon_pregnant_have_baby_selected)
        }

        binding.flReadyPregnant.setOnClickListener {
            hideImg()
            selectedValue = 1
            selected = binding.tvReadyPregnantState.text.toString()
            setSelectView(binding.flHavePregnant, binding.tvHavePregnantState, false)
            setSelectView(binding.flReadyPregnant, binding.tvReadyPregnantState, true)
            setSelectView(binding.flHaveBaby, binding.tvHaveBaby, false)
            setSelectView(binding.flShiguanPregnant, binding.tvShiguanPregnantState, false)
            binding.ivReadyPregnant.setImageResource(R.drawable.icon_ready_pregnant_selected)
        }

        binding.flHavePregnant.setOnClickListener {
            hideImg()
            selectedValue = 2
            selected = binding.tvHavePregnantState.text.toString()
            setSelectView(binding.flHavePregnant, binding.tvHavePregnantState, true)
            setSelectView(binding.flReadyPregnant, binding.tvReadyPregnantState, false)
            setSelectView(binding.flHaveBaby, binding.tvHaveBaby, false)
            setSelectView(binding.flShiguanPregnant, binding.tvShiguanPregnantState, false)
            binding.ivHavePregnant.setImageResource(R.drawable.icon_pregnant_ing_selected)
        }
        binding.tvConfirm.setOnClickListener {
            //已经选中的状态就不继续更新了
            if (selectedStatus != selected) {
                confirm(selected)
                //提交mob event
                mobEventService.sendEvent(
                    "Event_Components_StatusPopup_DoneButton_Click",
                    mutableMapOf(Pair("Status", selected))
                )
            }
            this.dismiss()
        }
    }

    private fun hideImg() {
        binding.ivShiguanPregnant.setImageResource(R.drawable.icon_baby_tube_status_select)
        binding.ivHaveBaby.setImageResource(R.drawable.icon_pregnant_have_baby_normal)
        binding.ivHavePregnant.setImageResource(R.drawable.icon_pregnant_ing_naomal)
        binding.ivReadyPregnant.setImageResource(R.drawable.icon_ready_pregnant_normal)
    }


    private fun setSelectView(view: ShadowLayout, tvState: TextView, isSelected: Boolean) {
        binding.tvConfirm.isEnabled = true
        view.setShadowColor(
            if (isSelected) resources.getColor(R.color.color_currency) else resources.getColor(
                R.color.transparent
            )
        )
        view.setStrokeColor(
            if (isSelected) resources.getColor(R.color.color_currency) else resources.getColor(
                R.color.colorF9F9F9
            )
        )
        tvState.setTextColor(
            if (isSelected) resources.getColor(R.color.color_262626) else resources.getColor(
                R.color.color_BFBFBF
            )
        )
    }
}