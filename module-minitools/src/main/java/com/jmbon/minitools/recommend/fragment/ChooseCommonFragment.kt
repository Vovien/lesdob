package com.jmbon.minitools.recommend.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.github.gzuliyujiang.wheelview.contract.OnWheelChangedListener
import com.github.gzuliyujiang.wheelview.widget.WheelView
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.extention.getViewModel
import com.jmbon.minitools.databinding.FragmentChooseCommonLayoutBinding
import com.jmbon.minitools.recommend.bean.ItemFormBean
import com.jmbon.minitools.recommend.viewmodel.ChooseViewModel

/******************************************************************************
 * Description: 通用的选择页面, 用于选择性别/年龄/生育史/助孕史/妊娠史/妊娠方式/胎龄
 * Author: jhg
 *
 * Date: 2023/5/30
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class ChooseCommonFragment :
    ViewModelFragment<CommonViewModel, FragmentChooseCommonLayoutBinding>() {

    private val ownerViewModel by lazy {
        context.getViewModel(ChooseViewModel::class.java)
    }

    override fun initView(view: View) {
        arguments?.getParcelable<ItemFormBean>(PARAMS_KEY_FORM_INFO)?.let {
            binding.tvTitle.text = it.title
            binding.wwValue.apply {
                var defaultPos = it.single_list?.indexOfFirst { it1 -> it1.is_selected } ?: 0
                if (defaultPos < 0) {
                    defaultPos = 0
                }
                ownerViewModel?.paramsMap?.put(
                    it.key,
                    it.single_list?.getOrNull(defaultPos)?.value
                )
                setData(it.single_list?.map { it1 -> it1.title }, it.single_list?.indexOfFirst { it1 -> it1.is_selected } ?: 0)
                setOnWheelChangedListener(object : OnWheelChangedListener {
                    override fun onWheelScrolled(view: WheelView?, offset: Int) {

                    }

                    override fun onWheelSelected(view: WheelView?, position: Int) {
                        ownerViewModel?.paramsMap?.put(
                            it.key,
                            it.single_list?.getOrNull(position)?.value
                        )
                    }

                    override fun onWheelScrollStateChanged(view: WheelView?, state: Int) {

                    }

                    override fun onWheelLoopFinished(view: WheelView?) {

                    }
                })
            }

        }
    }

    companion object {

        private const val PARAMS_KEY_FORM_INFO = "formInfo"

        fun newInstance(formInfo: ItemFormBean?): Fragment {
            return ChooseCommonFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAMS_KEY_FORM_INFO, formInfo)
                }
            }
        }
    }
}