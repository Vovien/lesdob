
######################短视频混淆配置#########################
-keep class com.alivc.**{*;}
-keep class com.aliyun.**{*;}
-keep class com.cicada.**{*;}
-dontwarn com.alivc.**
-dontwarn com.aliyun.**
-dontwarn com.cicada.**
######################播放器混淆配置#########################
-keep class com.cicada.**{*;}