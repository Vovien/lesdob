package com.jmbon.pay.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

//import com.alipay.sdk.app.AuthTask;
import com.apkdv.mvvmfast.ktx.ToastKTXKt;
import com.jmbon.middleware.config.network.Http;
import com.jmbon.pay.bean.AuthResult;
import com.jmbon.pay.ext.WxResult;

import java.util.Map;

import rxhttp.wrapper.param.RxHttpKt;

public class AuthUtils {

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2021001185618757";
    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "2088831190121882";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "1";
    public static final String RSA2_PRIVATE = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDJyTswsrRjCKbClil3lfh0/JmO6YyBLtQ3g50VK8xwy4OOAmD0DGO6EWrDWV3+XnsCgE4nS7DnGRIdG3Yg/wVeaQjajeVnNbQX1pbw+GkVuZpr9Mq19aBcjsE9eqDD2uYB4iHKHrS6hJlh4zK2N7FX4l7HDk3XtP/9TB9AAm4Z9hccyJdDnCxhWnVduAIvDvNvXoJXssowtFXihqaw6/TkIEb5suUhaMPM4qjUVsRRmouLCcvFY6zNkT3A9arHrCGVSsKhsTIX6yFaMTTROnSRVyBuC7ZGORFfBhjFhgOuj59LuDYuEGktLI675zAd8mAoAVMLDe9BNGGes/Ua+sxTAgMBAAECggEAFWxl+l+ewzzx54rl0Cp9/H2tF/03y6dbJKG2lb7Wr8sLppIeFQsTs3eR7DeawRzq1nMk2QsyNvb0c16tBgfNXB6ur/WMu456FmXUFLVrAsF34apVs0fmxKzrW0mfs+uTq/OEPt9PtNXLKxdiFPKIw5nnEh80qIgw/YWNc9BOPDcyIvG/wJ73dIJLE0V+YcszdDBk7K1oTwhiagq+0mZ2qG8zXxu/R+o3tpc+lGhylr7O+TlThSF9cL4y30b3e84sNyHtZQu3IQWDddEG5wifjKskpPOfwGn5VrKSxEDns8xnXMJ1MhrkUqlNSdA8DYvXRdkuwDC4MM+ttRYFLMWE8QKBgQDtIohL+zPWAtfBBIbUuosq+4kM670w6/zYT+arSbNkNnuLTAAk1jPtWeprHtXjk4jlj7JIIMpBznwvvoEwpRVCp35NIYYoZkcNPurF748RC8p93RR7BZ79uoNbQagBmB1DUG+pXg+TBDY8WccicyFKVYzHwUqkyoT9W52B8mDlzwKBgQDZ1smmg4r+KSvRD/Ro3SrGC/Az0ZExf48KGmtDjBzunMxBS90MPsoBwrMtBKR0wBPwmFwfZ3yy9A7QcFKG1jiunAqLd3G++ooV5bAS5hocnMbKvWacwA9hGizjFhdn1DeFzussUpy+/1PGmwYCarqJzW9MLQLHuLHysXlpROfWPQKBgQCoh/9YCMsZUWHnMwFkVMOto6EOhsgp6b4EOh0VJUcSdMovAK+lfnrfOCUrRnn6yJLlm8+4BV3ZkRefkwQb/2dxvLo9tRK0yaO7nTibvhruoXOjKilxGViauSC8DZiKsYIyogsJ0ui8sqSdFa06JZrGSrDIEu6q2afUrHTvssP87wKBgQCXgBzwVK33A5U4qdr2j1KvOCBPOaG8BhXqFeOhr7FzddNMWkGawZRmUw2f8omZtPhpLN1s7eE46CnhZyzOH/B954Ih6ulNxBsDDaI+Eo/4dESBPeY+gAhyJO0w5e1TRbFzuyutwKrI3vRZvPkZ9zZSnnCYTvmPA3Gm8y0YPFbslQKBgQCpGDO1UNdELPuFEdyggW6ZkYMm8u2TdfaxxyGVrf1fN2Y6IBddy18T9Qdsk+OSG4fm33wBDVnOHF4yPLiK7w01ya2Ljpj77YuIe99cdRzTreOF23bCL83zrh5/XwsSHQOA4jujiV3vyvHCIQx50V8uSEKN+2qbqb1lI30E29vDxg==";
    public static final String RSA_PRIVATE = "";

    private static Context mContext;
    private static final int SDK_AUTH_FLAG = 1002;

    public static String userId = "";
    public static String authCode = "";

    private static AliAuthBack mAliAuthBack;


    /**
     * 调用支付宝的授权
     *
     * @param content 后台传过来的数据
     * @param context 上下文
     */
    public static void setAliAuth(final String content, Context context) {
        mContext = context;

        //检测是否安装支付宝
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        //确认安装支付宝后进行授权接口调用
        if (componentName != null) {
            boolean rsa2 = (RSA2_PRIVATE.length() > 0);
            Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
            String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

            String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
            String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
            final String authInfo = info + "&" + sign;
            //异步处理
            Runnable authRunnable = () -> {

                //构造AuthTask对象
                //    AuthTask authTask = new AuthTask((Activity) mContext);
                //调用授权接口，获取授权结果
                //    Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                //msg.obj = result;
                mHandler.sendMessage(msg);
            };
            //必须异步处理
            Thread authThread = new Thread(authRunnable);
            authThread.start();
        } else {
            ToastKTXKt.showToast("请您先安装支付宝");
        }
    }


    @SuppressLint("HandlerLeak")
    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SDK_AUTH_FLAG) {
                AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                String resultStatus = authResult.getResultStatus();
                // 判断resultStatus 为“9000”且result_code
                // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                    // 获取alipay_open_id，调支付时作为参数extern_token 的value
                    // 传入，则支付账户为该授权账户
                    if (mAliAuthBack != null) {
                        mAliAuthBack.authInfo(authResult);
                    }
                    ToastKTXKt.showToast("授权成功");
                } else {
                    // 其他状态值则为授权失败
                    ToastKTXKt.showToast("授权失败");

                }

            }

        }
    };


    private void setAuthCode(String authCode) {
        AuthUtils.authCode = authCode;
    }

    public void setAliAuthBack(AliAuthBack back) {
        mAliAuthBack = back;
    }

    public interface AliAuthBack {
        void authInfo(AuthResult authResult);
    }

}
