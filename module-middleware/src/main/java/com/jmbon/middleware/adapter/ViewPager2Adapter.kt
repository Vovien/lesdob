package com.jmbon.middleware.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPager2Adapter(
    val fragments: ArrayList<Fragment>,
    manager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(manager, lifecycle) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}