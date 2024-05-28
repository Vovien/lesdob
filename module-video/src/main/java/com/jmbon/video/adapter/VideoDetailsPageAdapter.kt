package com.jmbon.video.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

//BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
class VideoDetailsPageAdapter(
    val fragments: ArrayList<Fragment>, activity: FragmentActivity,
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}