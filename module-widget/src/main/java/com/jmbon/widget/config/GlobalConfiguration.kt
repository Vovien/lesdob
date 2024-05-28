package com.jmbon.widget.config

import android.app.Application
import android.content.Context
import androidx.annotation.Keep
import com.apkdv.mvvmfast.base.delegate.AppLifecycles
import com.apkdv.mvvmfast.base.integration.ConfigModule
import com.apkdv.mvvmfast.utils.mmkv
import com.blankj.utilcode.util.ColorUtils
import com.jmbon.widget.R
import com.jmbon.widget.picture.PictureSelectorEngineImp
import com.luck.picture.lib.app.IApp
import com.luck.picture.lib.app.PictureAppMaster
import com.luck.picture.lib.engine.PictureSelectorEngine
import com.luck.picture.lib.tools.PictureFileUtils
import com.lxj.xpopup.XPopup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import xyz.doikki.videoplayer.exo.ExoMediaPlayerFactory
import xyz.doikki.videoplayer.player.VideoViewConfig
import xyz.doikki.videoplayer.player.VideoViewManager

@Keep
class GlobalConfiguration : ConfigModule {
    override fun applyOptions(context: Context?) {
    }

    override fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycles>) {
        lifecycles.add(object : AppLifecycles {
            override fun attachBaseContext(base: Context) {

            }

            override fun onCreate(application: Application) {
                // 设置背景
                XPopup.setShadowBgColor(ColorUtils.getColor(R.color.black40))
                MainScope().launch(Dispatchers.Main.immediate) {
                    async(Dispatchers.Default) {
                        PictureAppMaster.getInstance().app = object : IApp {
                            override fun getAppContext(): Context {
                                return application
                            }

                            override fun getPictureSelectorEngine(): PictureSelectorEngine {
                                return PictureSelectorEngineImp()
                            }
                        }

                       // PictureFileUtils.deleteAllCacheDirFile(application)
                        VideoViewManager.setConfig(
                            VideoViewConfig.newBuilder() //使用ExoPlayer解码
                                .setPlayerFactory(ExoMediaPlayerFactory.create()) //                .setRenderViewFactory(SurfaceRenderViewFactory.create())
                                .setEnableOrientation(true)
                                .setEnableAudioFocus(true)
                                .setPlayOnMobileNetwork(true)
                                .build()
                        )
                    }.await()
                }

            }

            override fun onTerminate(application: Application) {
            }

        })
    }
}