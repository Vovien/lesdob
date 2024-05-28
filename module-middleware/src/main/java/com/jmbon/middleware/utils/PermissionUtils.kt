package com.jmbon.middleware.utils

import android.Manifest
import android.content.Context
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.PermissionUtils
import com.jmbon.widget.GeneralDialog

/**
 * @author : leimg
 * time   : 2021/5/13
 * desc   :
 * version: 1.0
 */
object PermissionUtils {

    /**
     * 单权限请求判断
     *  action:获取权限成功执行的操作
     *  deniedTips：权限名称
     */
    fun doNeedPermissionAction(
        context: Context,
        permissionStr: String,
        action: () -> Unit,
        functionStr: String,
        permissionName: String
    ) {

        if (PermissionUtils.isGranted(permissionStr)) {
            action()
            return
        }

        PermissionUtils.permission(Manifest.permission.CALL_PHONE)
            .explain { activity, denied, shouldRequest ->

                GeneralDialog.showNormalDialog(
                    activity,
                    "${permissionName}权限申请",
                    "${functionStr}功能需要${permissionName}权限,是否开启权限？",
                    confirmListener = {
                        shouldRequest.start(true)
                    },
                    cancelListener = {
                        shouldRequest.start(false)
                    })

            }
            .callback { isAllGranted, granted, deniedForever, denied ->
                if (isAllGranted) {
                    action()
                    return@callback
                }
                if (deniedForever.isNotEmpty()) {
                    "请手动开启${permissionName}权限".showToast()
                    PermissionPageUtils(context).jumpPermissionPage()
                } else {
                    "${permissionName}权限已拒绝".showToast()
                }
            }
            .request()

    }

    /**
     * 多权限请求判断
     *  action:获取权限成功执行的操作
     *  deniedTips：拒绝后提示的操作
     */
    fun doNeedPermissionAction(
        context: Context,
        permissionStrList: MutableList<String>,
        action: () -> Unit,
        functionStr: String,
        permissionName: String
    ) {

        if (PermissionUtils.isGranted(*permissionStrList.toTypedArray())) {
            action()
            return
        }
        PermissionUtils.permission(*permissionStrList.toTypedArray())
            .explain { activity, denied, shouldRequest ->

                GeneralDialog.showNormalDialog(
                    activity,
                    "${permissionName}权限申请",
                    "${functionStr}功能需要${permissionName}权限,是否开启权限？",
                    confirmListener = {
                        shouldRequest.start(true)
                    },
                    cancelListener = {
                        shouldRequest.start(false)
                    })

            }
            .callback { isAllGranted, granted, deniedForever, denied ->
                if (isAllGranted) {
                    action()
                    return@callback
                }
                if (deniedForever.isNotEmpty()) {
                    "请手动开启${permissionName}权限".showToast()
                    PermissionPageUtils(context).jumpPermissionPage()
                } else {
                    "${permissionName}权限已拒绝".showToast()
                }
            }
            .request()
    }


    /**
     * 多权限请求判断 -不需要解释
     *  action:获取权限成功执行的操作
     *  deniedTips：拒绝后提示的操作
     */
    fun doNeedPermissionAction2(
        context: Context,
        permissionStrList: MutableList<String>,
        action: () -> Unit,
        deny: () -> Unit,
        functionStr: String,
        permissionName: String,
        isShowTipsDialog: Boolean
    ) {

        if (PermissionUtils.isGranted(*permissionStrList.toTypedArray())) {
            action()
            return
        }
        PermissionUtils.permission(*permissionStrList.toTypedArray())
            .explain { activity, denied, shouldRequest ->

                shouldRequest.start(true)
            }
            .callback { isAllGranted, granted, deniedForever, denied ->
                if (isAllGranted) {
                    action()
                    return@callback
                }

                if (isShowTipsDialog) {
                    if (deniedForever.isNotEmpty()) {
                        // "请手动开启${permissionName}权限".showToast()
                        GeneralDialog.showNormalDialog(
                            context,
                            "${permissionName}权限申请",
                            functionStr,
                            //"${functionStr}功能需要${permissionName}权限,是否开启权限？",
                            confirmBtnText = "开启权限",
                            confirmListener = {
                                PermissionPageUtils(context).jumpPermissionPage()
                            },
                            cancelListener = {
                               // "${permissionName}权限已拒绝".showToast()
                            })

                    } else {
                      //  "${permissionName}权限已拒绝".showToast()
                    }
                }

                deny()
            }
            .request()
    }
}