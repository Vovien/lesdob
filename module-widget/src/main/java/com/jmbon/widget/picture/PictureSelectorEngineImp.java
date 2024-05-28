package com.jmbon.widget.picture;

import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.engine.PictureSelectorEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.util.List;


public class PictureSelectorEngineImp implements PictureSelectorEngine {
    private static final String TAG = PictureSelectorEngineImp.class.getSimpleName();

    @Override
    public ImageEngine createEngine() {
//          这种情况是内存极度不足的情况下，比如开启开发者选项中的不保留活动或后台进程限制，导致ImageEngine被回收
        // 重新创建图片加载引擎
        return GlideEngine.createGlideEngine();
    }

    @Override
    public OnResultCallbackListener<LocalMedia> getResultCallbackListener() {
        return new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(List<LocalMedia> result) {
            }

            @Override
            public void onCancel() {
            }
        };
    }
}
