package com.jmbon.middleware.arouter

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.template.IProvider

interface MineFragmentService : IProvider {
    fun refreshDataById(fragment: Fragment, uid: Int)
}