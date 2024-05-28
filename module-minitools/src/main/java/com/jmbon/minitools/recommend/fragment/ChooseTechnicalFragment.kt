package com.jmbon.minitools.recommend.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.decoration.GridSpacingItemDecoration
import com.jmbon.middleware.extention.getViewModel
import com.jmbon.middleware.extention.setStateBackground
import com.jmbon.middleware.extention.setTextColor
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.dp
import com.jmbon.minitools.R
import com.jmbon.minitools.databinding.FragmentChooseTechnicalLayoutBinding
import com.jmbon.minitools.recommend.bean.FormTag
import com.jmbon.minitools.recommend.bean.ItemFormBean
import com.jmbon.minitools.recommend.viewmodel.ChooseViewModel


/******************************************************************************
 * Description: 选择想了解的技术
 *
 * Author: jhg
 *
 * Date: 2023/5/30
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class ChooseTechnicalFragment: ViewModelFragment<CommonViewModel, FragmentChooseTechnicalLayoutBinding>() {

    private val ownerViewModel by lazy {
        context.getViewModel(ChooseViewModel::class.java)
    }

    private val technicalAdapter by lazy {
        object: BaseSectionQuickAdapter<FormTag, BaseViewHolder>(R.layout.item_choose_technical_title_layout, R.layout.item_choose_child_reason_layout) {
            override fun convert(holder: BaseViewHolder, item: FormTag) {
                holder.setText(R.id.tv_content, item.title)
                holder.getView<TextView>(R.id.tv_content).apply {
                    setTextColor(state = android.R.attr.state_selected, falseTextColor = R.color.color_262626.toColorInt(), trueTextColor = R.color.color_5384FF.toColorInt())
                    setStateBackground(state = android.R.attr.state_selected, falseBackgroundColor = R.color.colorF5F5F5.toColorInt(), trueBackgroundColor = R.color.color_5384FF_5.toColorInt(),
                    trueBorderWidth = 2.dp, trueBorderColor = R.color.color_5384FF.toColorInt(), radius = 10.dp)
                    isSelected = item.is_selected
                }
            }

            override fun convertHeader(helper: BaseViewHolder, item: FormTag) {
                helper.setText(R.id.tv_title, item.title)
            }

            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                val layoutParams = holder.itemView.layoutParams
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                holder.itemView.layoutParams = layoutParams
            }
        }
    }

    override fun initView(view: View) {
         binding.rvTechnical.addItemDecoration(
             GridSpacingItemDecoration(spacing = 10.dp, edgeSpacing = 20.dp)
         )
         binding.rvTechnical.adapter = technicalAdapter
         arguments?.getParcelable<ItemFormBean>(PARAMS_KEY_FORM_INFO)?.apply {
             binding.tvTitle.text = title
             val dataList = mutableListOf<FormTag>()
             multiple_list?.forEach  {
                 if (!it.tag_list.isNullOrEmpty()) {
                     if (!it.title.isNullOrEmpty()) {
                         dataList.add(FormTag(title = it.title, value = -1))
                     }
                     it.tag_list.forEach { it1 ->
                         it1.groupId = it.id
                     }
                     dataList.addAll(it.tag_list)
                 }
             }
             technicalAdapter.setList(dataList)
             ownerViewModel?.paramsMap?.put(key, dataList.filter { it1 -> it1.is_selected }.map { it1 -> it1.value }.joinToString(","))

             technicalAdapter.setOnItemClickListener { _, _, position ->
                 technicalAdapter.data[position].let {
                     if (it.is_mutual == 1 && it.is_selected) {
                         return@setOnItemClickListener
                     }

                     it.is_selected = !it.is_selected
                     if (it.is_selected) {
                         if (it.is_mutual == 1) {
                             technicalAdapter.data.filter { it1 -> it1.is_mutual != 1 && it1.groupId == it.groupId }.map { it1 -> it1.is_selected = false }
                         } else {
                             technicalAdapter.data.filter { it1 -> it1.is_mutual == 1 && it1.groupId == it.groupId}.map { it1 -> it1.is_selected = false }
                         }
                     } else {
                         if (technicalAdapter.data.find { it1 -> it1.is_selected && it1.groupId == it.groupId } == null) {
                             technicalAdapter.data.filter { it1 -> it1.is_mutual == 1 && it1.groupId == it.groupId}.map { it1 -> it1.is_selected = true }
                         }
                     }
                     technicalAdapter.notifyDataSetChanged()
                     ownerViewModel?.paramsMap?.put(key, technicalAdapter.data.filter { it1 -> it1.is_selected }.map { it1 -> it1.value }.joinToString(","))
                 }
             }
         }
    }

    companion object {
        private const val PARAMS_KEY_FORM_INFO = "formInfo"

        fun newInstance(formInfo: ItemFormBean?): Fragment {
            return ChooseTechnicalFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAMS_KEY_FORM_INFO, formInfo)
                }
            }
        }
    }
}