package com.tubewiki.mine.utils;


import com.blankj.utilcode.util.LogUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorManager {
    private static final ExecutorService threadExecutor;

    public static void run(Runnable var0) {
        threadExecutor.execute(var0);
    }

    static {
        threadExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(),
                0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 开发者自己app的服务端对接阿里号码认证，并提供接口给app调用
     * 1、调用app服务端接口将一键登录token发送过去
     * 2、app服务端拿到token调用阿里号码认证服务端换号接口，获取手机号
     * 3、app服务端拿到手机号帮用户完成注册以及登录的逻辑，返回账户信息给app
     * @return 账户信息
     */
    public static String getPhoneNumber(String token) {
        String result = "";
        try {
            //模拟网络请求
            Thread.sleep(500);
            LogUtils.i(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
