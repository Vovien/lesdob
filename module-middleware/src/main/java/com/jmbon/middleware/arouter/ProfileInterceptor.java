package com.jmbon.middleware.arouter;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.apkdv.mvvmfast.ktx.ToastKTXKt;
import com.jmbon.middleware.utils.TagConstant;

/**
 * 登录的拦截器
 */
@Interceptor(priority = 99, name = "profile")
public class ProfileInterceptor implements IInterceptor {


    /***
     * The operation of this interceptor.
     *
     * @param postcard meta
     * @param callback cb
     */
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        if (postcard.getExtras() != null) {
            String url = postcard.getPath();
            if (url.equals("/my/circle/profile") || url.equals("/mine/message/personal_page")) {
                int cancel = postcard.getExtras().getInt(TagConstant.USER_CANCEL, 0);
                if (cancel == 1) {
                    ToastKTXKt.showToast("该用户已注销");
                    callback.onInterrupt(null);
                } else {
                    callback.onContinue(postcard);
                }
            }else callback.onContinue(postcard);
        }
    }

    /***
     * Do your init work in this method, it well be call when processor has been load.
     *
     * @param context ctx
     */
    @Override
    public void init(Context context) {

    }

}
