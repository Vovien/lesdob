package com.tubewiki.android.install

import android.content.Context
import android.net.Uri
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.umeng.umlink.MobclickLink
import com.umeng.umlink.UMLinkListener

class MyUMLinkListener(val mContext: Context, val link: (String, HashMap<String, String>) -> Unit, val install: (installParams: HashMap<String, String>, uri: Uri) -> Unit) :
    UMLinkListener {
    private val mInstallParams by lazy { HashMap<String, String>() }
    private val TAG = "MyUMLinkListener"

    /**
     * 对跳转App的处理，唤起已安装App会走这个回调，
     * 可以接收到开发者在U-Link后台设置页配置的Path、App页面传参以及在JSSDK传递的data自定义参数，
     * 开发者可以根据这些信息处理后续逻辑，例如唤起App时跳转到App指定页面
     *
     * @param path
     * @param hashMap
     */
    override fun onLink(path: String, hashMap: HashMap<String, String>) {
        Log.d(TAG, "onLink() called with: path = $path, hashMap = $hashMap")

        val params = HashMap<String, String>()
        if (hashMap.isNotEmpty()) {
            params.putAll(hashMap)
        }
        if (mInstallParams.isNotEmpty()) {
            params.putAll(mInstallParams)
        }
        link(path, params)
    }

    /**
     * 为获取新装参数的处理，App首次安装启动时，开发者调取getInstallParams新安装参数接口会走这个回调，
     * 此处返回两个参数，一个是开发者在后台填写的首次安装传参installParams，另一个是服务端拼接的wakeup url，
     * 包含了JSSDK传递的data自定义参数。
     *
     * 开发者可以用 MobclickLink.handleUMLinkURI(mContext, uri, umlinkAdapter)
     * 从URL里解析出传递的自定义参数。根据这些信息可以处理后续逻辑，例如安装启动App时跳转到App指定页面、携带参数安装、绑定邀请关系
     *
     * @param installParams
     * @param uri
     */
    override fun onInstall(installParams: HashMap<String, String>, uri: Uri) {
        if (installParams.isEmpty() && uri.toString().isEmpty()) {
            // 没有匹配到安装参数
            LogUtils.e("没有匹配到安装参数")
        } else {
            if (installParams.isNotEmpty()) {
                mInstallParams.clear()
                mInstallParams.putAll(installParams)
            }
            if (uri.toString().isNotEmpty()) {
                MobclickLink.handleUMLinkURI(mContext, uri, this)
            }
        }
        install(installParams,uri)
 //       SharedPreferences.Editor sp = mContext.getSharedPreferences("MY_PREFERENCE", Context.MODE_PRIVATE).edit();
//        sp.putBoolean("key_Has_Get_InstallParams", true);
//        sp.commit();
    }

    override fun onError(error: String) {
        // 错误处理
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setMessage(error);
//        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//            }
//        });
//        builder.show();
    }
}