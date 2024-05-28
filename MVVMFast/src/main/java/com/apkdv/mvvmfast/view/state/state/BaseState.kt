package com.apkdv.mvvmfast.view.state.state

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.apkdv.mvvmfast.view.state.MultiState
import com.apkdv.mvvmfast.view.state.MultiStateContainer

abstract class BaseState : MultiState() {
    override fun onCreateMultiStateView(
        context: Context,
        inflater: LayoutInflater,
        container: MultiStateContainer
    ): View {
        return inflater.inflate(getLayoutId(), container, false)
    }

    abstract fun getLayoutId(): Int

    override fun onMultiStateViewCreate(
        view: View,
        retry: () -> Unit,
        offset: Int,
        orientation: Int
    ) {
    }

}