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

-keepclassmembers class * implements androidx.viewbinding.ViewBinding {
  public static ** inflate(...);
  public static ** bind(***);
}

-keep @androidx.annotation.Keep class * {*;}

-keep class androidx.annotation.Keep

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <init>(...);
}

########### OkHttp3 ###########
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**

########### RxJava RxAndroid ###########
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

########### Gson ###########
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod
# Gson 自定义相关
-keep class com.pandaq.rxpanda.entity.**{*;}
-keep class com.pandaq.rxpanda.gsonadapter.**{*;}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# Preserve all classes implementing Parcelable
-keep class * implements android.os.Parcelable

# Preserve all classes implementing Serializable
-keep class * implements java.io.Serializable

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

-keep public class com.apkdv.mvvmfast.base.integration.** { *; }
-keep public class com.apkdv.mvvmfast.network.entity.** { *; }
-keep public class com.apkdv.mvvmfast.base.delegate.** { *; }
-keep public class com.apkdv.mvvmfast.** { *; }
-keep interface * implements com.apkdv.mvvmfast.base.integration.ConfigModule


#bannerviewpager
-keep class androidx.recyclerview.widget.**{*;}
-keep class androidx.viewpager2.widget.**{*;}

#xpopup
-dontwarn com.lxj.xpopup.widget.**
-keep class com.lxj.xpopup.widget.**{*;}
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}