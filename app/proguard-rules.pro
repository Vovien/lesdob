# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class * implements androidx.viewbinding.ViewBinding {
    public static *** inflate(android.view.LayoutInflater);
    public static *** inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
    public static *** bind(android.view.View);
}
-keep public class * extends android.view.View

#tencent im
-keep class com.tencent.imsdk.** { *; }
#tencent tpns
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep class com.tencent.android.tpush.** {*;}
-keep class com.tencent.tpns.baseapi.** {*;}
-keep class com.tencent.tpns.mqttchannel.** {*;}
-keep class com.tencent.tpns.dataacquisition.** {*;}
#tencent tpns vivo
-dontwarn com.vivo.push.**
-keep class com.vivo.push.**{*; }
-keep class com.vivo.vms.**{*; }
-keep class com.tencent.android.vivopush.VivoPushMessageReceiver{*;}
#tencent tpns oppo
-keep public class * extends android.app.Service
-keep class com.heytap.mcssdk.** {*;}
-keep class com.heytap.msp.push.** { *;}
#tencent tpns huawei
-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}
-keep class com.huawei.agconnect.**{*;}
#tencent tpns xiaomi
-keep class com.xiaomi.**{*;}
-keep public class * extends com.xiaomi.mipush.sdk.PushMessageReceiver
#tencent tpns meizu
-dontwarn com.meizu.cloud.pushsdk.**
-keep class com.meizu.cloud.pushsdk.**{*;}


-keep class rxhttp.wrapper.param.**{*;}
-keep class com.jmbon.middleware.config.network.Http

-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class com.mob.**{*;}
-keep class com.bytedance.**{*;}
-keep class org.apache.commons.codec.**{*;}
-dontwarn cn.sharesdk.**
-dontwarn com.sina.**
-dontwarn com.mob.**

-dontwarn com.alipay.**
-dontwarn com.huawei.**
-dontwarn org.conscrypt.**
-dontwarn com.antfin.**
-dontwarn android.taobao.**
-dontwarn com.mpaas.**
-dontwarn com.ant.**
-dontwarn com.aaid.**

-keep class androidx.recyclerview.widget.**{*;}
-keep class androidx.viewpager2.widget.**{*;}

-keep class com.aliyun.vod.jasonparse.**{ *; }
-keep class com.aliyun.auth.model.**{ *; }
-keep class com.aliyun.vod.qupaiokhttp.**{ *; }
-keep class com.alibaba.sdk.android.vod.upload.model.**{ *; }
-keep class com.aliyun.auth.core.AliyunVodErrorCode{ *; }
-keep class com.alibaba.sdk.android.vod.upload.VODUploadClient{ *; }
-keep class com.alibaba.sdk.android.vod.upload.VODUploadCallback{ *; }
-keep class com.alibaba.sdk.android.vod.upload.VODSVideoUploadClient{ *; }
-keep class com.alibaba.sdk.android.vod.upload.VODSVideoUploadCallback{ *; }


######################短视频混淆配置#########################
-keep class com.alivc.**{*;}
-keep class com.aliyun.**{*;}
-keep class com.cicada.**{*;}
-dontwarn com.alivc.**
-dontwarn com.aliyun.**
-dontwarn com.cicada.**
######################播放器混淆配置#########################
-keep class com.cicada.**{*;}
-keep class com.effective.android.panel.**{*;}

-keep class android.support.v8.renderscript.** { *; }
-keep class androidx.renderscript.** { *; }

# Gson混淆
-keep class com.google.gson.** {*;}
-dontobfuscate