package com.tubewiki.android.adapter

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerFragmentStateAdapter(
    val fragmentList: ArrayList<Fragment>,
    context: FragmentActivity
) : FragmentStateAdapter(context) {
    @NonNull
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }
}