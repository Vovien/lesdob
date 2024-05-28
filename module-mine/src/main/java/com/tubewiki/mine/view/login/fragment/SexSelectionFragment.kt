package com.tubewiki.mine.view.login.fragment

import android.view.View
import com.apkdv.mvvmfast.base.AppBaseFragment
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.FragmentSexSelectionBinding
import com.tubewiki.mine.view.login.InitialSetupActivity
import com.jmbon.widget.WheelView.OnWheelViewListener


class SexSelectionFragment : AppBaseFragment<FragmentSexSelectionBinding>() {
    override fun initView(view: View) {
        binding.sexWheel.apply {
            setItems(resources.getStringArray(R.array.sex_item).toMutableList())
            onWheelViewListener = object : OnWheelViewListener() {
                override fun onSelected(selectedIndex: Int, item: String) {
                   if (activity is InitialSetupActivity){
                          (activity as InitialSetupActivity).setSex(if (selectedIndex ==1) 2 else 1)
                   }
                }
            }
        }

    }

}