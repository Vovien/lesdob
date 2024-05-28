package com.jmbon.middleware.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.sdk.android.oss.model.GetBucketLifecycleRequest

/**
 * @author : wangzhen
 * time   : 2021/3/23
 * desc   : viewpager适配器
 * version: 1.0
 */
class ViewPagerStateAdapter(
    var fragments: ArrayList<Fragment>,
    manager: FragmentManager
) :
    FragmentStatePagerAdapter(manager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }


}