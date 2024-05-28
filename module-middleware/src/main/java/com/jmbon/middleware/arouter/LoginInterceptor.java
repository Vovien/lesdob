package com.jmbon.middleware.arouter;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jmbon.middleware.config.Constant;
import com.jmbon.middleware.utils.TagConstant;

import static com.jmbon.middleware.arouter.RouterHub.LOGIN;

/**
 * 登录的拦截器
 */
@Interceptor(priority = 100, name = "login")
public class LoginInterceptor implements IInterceptor {

    private static InterceptorCallback interceptorCallback;
    private static Postcard mPostcard;

    /***
     * The operation of this interceptor.
     *
     * @param postcard meta
     * @param callback cb
     */
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        if (postcard.getExtras() != null) {
            boolean needLogin = postcard.getExtras().getBoolean(TagConstant.NEED_LOGIN_KEY, false);
            if (needLogin && !Constant.INSTANCE.isLogin()) {
                interceptorCallback = callback;
                mPostcard = postcard;
                // 将需要处理的 path 保存起来
                LoginCallBack.INSTANCE.setMPostcard(postcard);
                LoginCallBack.INSTANCE.setInterceptorCallback(callback);
                ARouter.getInstance().build(LOGIN).navigation();
                callback.onInterrupt(new Throwable("登录的拦截器"));
                return;
            }else alreadyLoginOnContinue();
        }
        callback.onContinue(postcard);
    }

    /***
     * Do your init work in this method, it well be call when processor has been load.
     *
     * @param context ctx
     */
    @Override
    public void init(Context context) {

    }

    public static void alreadyLoginOnContinue() {
        if (Constant.INSTANCE.isLogin()) {
            if (interceptorCallback != null && mPostcard != null) {
                interceptorCallback.onContinue(mPostcard);
            }
        } else {
            if (interceptorCallback != null) {
                interceptorCallback.onInterrupt(new Exception("需要登录异常"));
            }
        }
        interceptorCallback = null;
        mPostcard = null;
    }
}
