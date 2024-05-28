package com.jmbon.minitools.recommend.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.extention.getViewModel
import com.jmbon.minitools.databinding.FragmentChildBudgetLayoutBinding
import com.jmbon.minitools.recommend.bean.ItemBudgetBean
import com.jmbon.minitools.recommend.viewmodel.ChooseViewModel

/******************************************************************************
 * Description: 选择预算
 *
 * Author: jhg
 *
 * Date: 2023/6/27
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class ChildBudgetFragment : ViewModelFragment<ChooseViewModel, FragmentChildBudgetLayoutBinding>() {

    private val ownerViewModel by lazy {
        context.getViewModel(ChooseViewModel::class.java)
    }

    override fun initView(view: View) {

    }

    override fun initViewModel() {
        super.initViewModel()

        arguments?.let {
            val budgetType = it.getInt(PARAMS_KEY_BUDGET_TYPE)
            CommonViewModel.locationLD.observeForever { locationInfo ->
                val areaName = if (!locationInfo?.cityName.isNullOrBlank()) {
                    "${locationInfo?.citySimpleName}地区"
                } else {
                    locationInfo?.countryName ?: ""
                }
                setTip(areaName, budgetType, it.getString(PARAMS_KEY_BUDGET_RANGE))
            }

            it.getParcelableArrayList<ItemBudgetBean>(PARAMS_KEY_BUDGET_LIST)?.apply {
                binding.vPriceDistribution.setData(this) { startIndex, endIndex ->
                    binding.tvPrice.text = if (endIndex == this.size - 1) {
                        "${this[startIndex].title}元以上"
                    } else {
                        "${this[startIndex].title}元-${this[endIndex].title}元"
                    }
                    binding.tvTip.isVisible = startIndex == 0 && endIndex == size - 1
                    if (budgetType == 1) {
                        ownerViewModel?.inseminationBudget =
                            "${this[startIndex].title}~${this[endIndex].title}"
                    } else {
                        ownerViewModel?.tubeBudget =
                            "${this[startIndex].title}~${this[endIndex].title}"
                    }
                }
            }
        }
    }

    private fun setTip(areaName: String, budgetType: Int, budgetRange: String?) {
        val title = if (budgetType == 1) "人工授精" else "试管"
        buildSpannedString {
            append("树状图为${areaName}${title}价格分布\n")
            color(Color.parseColor("#A93497")) {
                append(budgetRange)
            }
            binding.tvTip.text = this
        }
    }

    companion object {

        private const val PARAMS_KEY_BUDGET_TYPE = "budgetType"
        private const val PARAMS_KEY_BUDGET_RANGE = "budgetRange"
        private const val PARAMS_KEY_BUDGET_LIST = "budgetList"

        /**
         * @param budgetType: 预算类型: 0=试管婴儿, 1=人工授精
         * @param budgetRange: 预算范围提示
         * @param budgetList: 预算分布
         */
        fun newInstance(
            budgetType: Int,
            budgetRange: String,
            budgetList: ArrayList<ItemBudgetBean>?
        ): Fragment {
            return ChildBudgetFragment().apply {
                arguments = Bundle().apply {
                    putInt(PARAMS_KEY_BUDGET_TYPE, budgetType)
                    putString(PARAMS_KEY_BUDGET_RANGE, budgetRange)
                    putParcelableArrayList(PARAMS_KEY_BUDGET_LIST, budgetList)
                }
            }
        }
    }
}