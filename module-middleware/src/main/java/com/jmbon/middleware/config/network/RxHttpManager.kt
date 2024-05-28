package com.jmbon.middleware.config.network


import android.app.Application
import com.apkdv.mvvmfast.network.log.HttpLoggingInterceptor
import com.blankj.utilcode.util.AppUtils
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.Constant.APP_NAME
import com.jmbon.middleware.config.Constant.APP_TYPE
import com.jmbon.middleware.config.Constant.isLogin
import com.jmbon.middleware.config.Constant.user
import com.jmbon.middleware.oiad.OAIDUtils
import com.jmbon.middleware.utils.RequestParamsUtils
import com.jmbon.middleware.utils.getRealChannel
import okhttp3.OkHttpClient
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.ssl.HttpsUtils
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLSession


class RxHttpManager(val context: Application) {

    init {
        val sslParams = HttpsUtils.getSslSocketFactory()
        val clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .pingInterval(40, TimeUnit.SECONDS)
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager) //添加信任证书
            .hostnameVerifier { hostname: String?, session: SSLSession? -> true } //忽略host验证
            //            .followRedirects(false)  //禁制OkHttp的重定向操作，我们自己处理重定向
            .addInterceptor(SignInterceptor())
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            ).dns(ApiDns())
//                        .addInterceptor(new TokenInterceptor())
//            .build()
        if (BuildConfig.DEBUG)
            clientBuilder.addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .collector(ChuckerCollector(context))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            )

        //设置缓存策略，非必须
//        val cacheFile = File(context.externalCacheDir, "HttpCache");
        //RxHttp初始化，非必须
        RxHttpPlugins.init(clientBuilder.build()) //自定义OkHttpClient对象
            .setDebug(false) //是否开启调试模式，开启后，logcat过滤RxHttp，即可看到整个请求流程日志
            // 默认关闭缓存
//            .setCache(cacheFile, 1000L * 5, CacheMode.ONLY_NETWORK)
            //            .setExcludeCacheKeys("time")               //设置一些key，不参与cacheKey的组拼
            //            .setResultDecoder(s -> s)                  //设置数据解密/解码器，非必须
            .setConverter(GsonConverter.create())  //设置全局的转换器，非必须

            .setOnParamAssembly { params ->                  //设置公共参数，非必须
                if (params.simpleUrl.contains("jmbon.com")) {
                    //1、可根据不同请求添加不同参数，每次发送请求前都会被回调
                    //2、如果希望部分请求不回调这里，发请求前调用RxHttp#setAssemblyEnabled(false)即可
                    val timestamp: String = RequestParamsUtils.getTimeString()
                    val paramsMap: HashMap<String, String> = HashMap()
                    paramsMap["timestamp"] = timestamp
                    paramsMap["device_type"] = "android"
                    paramsMap["area_code"] = "86"
                    paramsMap["app_name"] = APP_NAME
                    // APP类型[1: 孕小帮，2: 姐妹团，3: 试管百科,4:Al经期，5: LesDob,6:LesBorn]
                    paramsMap["app_type"] = "$APP_TYPE"
                    paramsMap["device_id"] = Constant.getDeviceId()
                    paramsMap["oaid"] = OAIDUtils.OAID
                    //版本号
                    paramsMap["version"] = AppUtils.getAppVersionName()
                    //激活渠道特殊，此接口不再次设置渠道
                    if (!params.simpleUrl.contains("app/active")) {
                        //渠道
                        paramsMap["channel"] = getRealChannel()
                    }
                    // .add("device_type", "android")
                    if (isLogin) {
                        params.addHeader("auth", user.token)
                    }
                    params.addAll(paramsMap)

                    return@setOnParamAssembly params
                } else return@setOnParamAssembly params
//                //1、可根据不同请求添加不同参数，每次发送请求前都会被回调
//                //2、如果希望部分请求不回调这里，发请求前调用RxHttp#setAssemblyEnabled(false)即可
//                val timestamp: String = RequestParamsUtils.getTimeString()
//                val random_num: String = RequestParamsUtils.getRandom()
//                val sign: String = RequestParamsUtils.getSign(random_num, timestamp)
//                val paramsMap: HashMap<String, String> = HashMap()
//                paramsMap["timestamp"] = timestamp
//                paramsMap["random_num"] = random_num
//                paramsMap["sign"] = sign
//                if (isLogin) {
//                    paramsMap["token"] = user.token
//                }
//                return@setOnParamAssembly params.addAll(paramsMap)
            }
    }


}