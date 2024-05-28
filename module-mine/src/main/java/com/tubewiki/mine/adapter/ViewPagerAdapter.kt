package com.tubewiki.mine.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @author : wangzhen
 * time   : 2021/3/23
 * desc   : viewpager适配器
 * version: 1.0
 */
class ViewPagerAdapter(var fragments: ArrayList<Fragment>, manager: FragmentManager) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }
}