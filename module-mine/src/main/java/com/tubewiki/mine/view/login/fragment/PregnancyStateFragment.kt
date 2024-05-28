package com.tubewiki.mine.view.login.fragment

import android.view.View
import com.apkdv.mvvmfast.base.AppBaseFragment
import com.tubewiki.mine.R
import com.tubewiki.mine.databinding.FragmentPregnancyStateBinding
import com.tubewiki.mine.view.login.InitialSetupActivity
import com.jmbon.widget.WheelView


class PregnancyStateFragment : AppBaseFragment<FragmentPregnancyStateBinding>() {
    override fun initView(view: View) {
        binding.sexWheel.apply {
            setItems(resources.getStringArray(R.array.pregnancy_item).toMutableList())
            setSeletion(1)
            onWheelViewListener = object : WheelView.OnWheelViewListener() {
                override fun onSelected(selectedIndex: Int, item: String) {
                    if (activity is InitialSetupActivity) {
                        (activity as InitialSetupActivity).setPregnancy(
                            when (item) {
                                "育儿" -> 3
                                "备孕" -> 1
                                else -> 2
                            }
                        )
                    }
                }
            }
        }
    }

}