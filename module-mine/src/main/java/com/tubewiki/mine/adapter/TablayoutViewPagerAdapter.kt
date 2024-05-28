package com.tubewiki.mine.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * @author : wangzhen
 * time   : 2021/3/23
 * desc   : viewpager适配器
 * version: 1.0
 */
class TablayoutViewPagerAdapter(
    var fragments: ArrayList<Fragment>,
    manager: FragmentManager,
    var titles: Array<String>?
) :  FragmentStatePagerAdapter(manager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)  {


    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        titles?.let {
            if (it.isNotEmpty() && position < it.size)
                return it[position]
        }
        return ""
    }
}