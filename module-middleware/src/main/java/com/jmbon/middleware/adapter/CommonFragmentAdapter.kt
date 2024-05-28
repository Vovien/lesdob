package com.jmbon.middleware.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/******************************************************************************
 * Description: 通用的FragmentAdapter
 *
 * Author: jhg
 *
 * Date: 2023/5/30
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class CommonFragmentAdapter(context: FragmentActivity, private val fragmentList: List<Fragment>): FragmentStateAdapter(context) {

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}