package com.tubewiki.android
import android.content.Context
import androidx.annotation.Keep
import com.apkdv.mvvmfast.base.delegate.AppLifecycles
import com.apkdv.mvvmfast.base.integration.ConfigModule

@Keep
class MainConfiguration : ConfigModule {
    override fun applyOptions(context: Context?) {
    }

    override fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycles>) {
        lifecycles.add(MainLifecyclesImpl())
    }
}