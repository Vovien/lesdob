package com.jmbon.middleware.arouter

import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.template.IProvider
import com.jmbon.middleware.bean.User

interface DialogService: IProvider {
    fun showRewardDialog(mContext: AppCompatActivity, itemId: Int, type: String, user: User)
}