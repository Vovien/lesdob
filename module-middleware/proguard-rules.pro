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

-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**

-keep class com.tencent.smtt.** {
    *;
}
-keep class com.tencent.tbs.** {
    *;
}

-keepclasseswithmembernames class ** {
    native <methods>;
}
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-keep class com.ut.** {*;}
-keep class com.ta.** {*;}
-keep class anet.**{*;}
-keep class anetwork.**{*;}
-keep class org.android.spdy.**{*;}
-keep class org.android.agoo.**{*;}
-keep class android.os.**{*;}
-keep class org.json.**{*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-dontwarn anet.**
-dontwarn org.android.spdy.**
-dontwarn org.android.agoo.**
-dontwarn anetwork.**
-dontwarn com.ut.**
-dontwarn com.ta.**

# 小米通道
-keep class com.xiaomi.** {*;}
-dontwarn com.xiaomi.**

# 华为通道
-keep class com.huawei.** {*;}
-dontwarn com.huawei.**
# VIVO通道
-keep class com.vivo.** {*;}
-dontwarn com.vivo.**

# OPPO通道
-keep public class * extends android.app.Service

# 魅族通道
-keep class com.meizu.cloud.** {*;}
-dontwarn com.meizu.cloud.**

-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class com.mob.**{*;}
-keep class com.bytedance.**{*;}
-dontwarn cn.sharesdk.**
-dontwarn com.sina.**
-dontwarn com.mob.**

-keep class com.umeng.** {*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# SDK 9.2.4及以上版本自带oaid采集模块，不再需要开发者再手动引用oaid库，所以可以不添加这些混淆
-keep class com.zui.**{*;}
-keep class com.miui.**{*;}
-keep class com.heytap.**{*;}
-keep class a.**{*;}
-keep class com.vivo.**{*;}
-keep public class com.jmbon.android.R$*{
public static final int *;
}

-keep class com.uc.** {*;}
-keep class com.tencent.mm.opensdk.** {
    *;
}

-keep class com.tencent.wxop.** {
    *;
}

-keep class com.tencent.mm.sdk.** {
    *;
}
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}


#oaid sdk
-keep class com.bun.miitmdid.** { *; }
-keep interface com.bun.supplier.** { *; }

# asus
-keep class com.asus.msa.SupplementaryDID.** { *; }
-keep class com.asus.msa.sdid.** { *; }
# freeme
-keep class com.android.creator.** { *; }
-keep class com.android.msasdk.** { *; }
# huawei
-keep class com.huawei.hms.ads.** { *; }
-keep interface com.huawei.hms.ads.** {*; }
# lenovo
-keep class com.zui.deviceidservice.** { *; }
-keep class com.zui.opendeviceidlibrary.** { *; }
# meizu
-keep class com.meizu.flyme.openidsdk.** { *; }
# nubia
-keep class com.bun.miitmdid.provider.nubia.NubiaIdentityImpl { *; }
# oppo
-keep class com.heytap.openid.** { *; }
# samsung
-keep class com.samsung.android.deviceidservice.** { *; }
# vivo
-keep class com.vivo.identifier.** { *; }
# xiaomi
-keep class com.bun.miitmdid.provider.xiaomi.IdentifierManager { *; }
# zte
-keep class com.bun.lib.** { *; }
# coolpad
-keep class com.coolpad.deviceidsupport.** { *; }

-keep class com.bun.** {*;}
-keep class com.asus.msa.** {*;}
-keep class com.heytap.openid.** {*;}
-keep class com.huawei.android.hms.pps.** {*;}
-keep class com.meizu.flyme.openidsdk.** {*;}
-keep class com.samsung.android.deviceidservice.** {*;}
-keep class com.zui.** {*;}

-keep class com.huawei.hms.ads.** {*; }
-keep interface com.huawei.hms.ads.** {*; }
-keepattributes *Annotation*
-keep @android.support.annotation.Keep class **{
@android.support.annotation.Keep <fields>;
@android.support.annotation.Keep <methods>;
}

-keep class com.umeng.** {*;}

-keep class org.repackage.** {*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}