package com.apkdv.mvvmfast.view.state

/**
 * @ProjectName: MultiStatePage
 * @Author: 赵岩
 * @Email: 17635289240@163.com
 * @Description: TODO
 * @CreateDate: 2020/11/4 16:04
 */
fun interface OnNotifyListener<T : MultiState> {
    fun onNotify(multiState: T)
}