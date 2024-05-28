package com.tubewiki.mine.utils

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.jmbon.middleware.arouter.MineFragmentService
import com.tubewiki.mine.view.message.fragment.PersonalPageFragment

///mine/fragment/personal_page
@Route(path = "/mine/personal/provider", name = "个人主页同步")
class MineFragmentServiceImp: MineFragmentService {
    override fun refreshDataById(fragment: Fragment, uid: Int) {
        if (fragment is PersonalPageFragment){
            fragment.refreshData(uid)
        }
    }
//    override fun refreshDataById(fragment: Fragment,uid: Int){
//        if (fragment is PersonalPageFragment) {
//        }
//    }

    override fun init(context: Context?) {
    }
}