package com.jmbon.widget.html;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.jmbon.widget.BuildConfig;
import com.jmbon.widget.R;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.List;


public class GeneralWebView extends WebView {
    boolean isItemClicked = false;
    private ValueCallback webValueCallbackBefore5; // 5.0以下回调
    private ValueCallback<Uri[]> webValueCallbackLater5;// 5.0以上回调
    private ProgressBar progressbar;
    private OnHtml5Listener onHtml5Listener;
    //    private LocalMedia mMedia;
    private OnProgressChanged progressChanged;
    WebChromeClient webChromeClient = new WebChromeClient() {

        //=========HTML5定位==========================================================
        //需要先加入权限
        //<uses-permission android:name="android.permission.INTERNET"/>
        //<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
        //<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>


        @Override
        public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
            return super.onJsAlert(webView, s, s1, jsResult);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (progressChanged != null)
                progressChanged.onProgressChanged(newProgress);
            if (newProgress == 100) {
                progressbar.setVisibility(View.GONE);
            } else {
                if (progressbar.getVisibility() == View.GONE) {
                    progressbar.setVisibility(View.VISIBLE);
                }
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (onHtml5Listener != null) {
                onHtml5Listener.onReceivedTitle(title);
            }
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
        }

//        @Override
//        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissionsCallback callback) {
//            callback.invoke(origin, true, false);//注意个函数，第二个参数就是是否同意定位权限，第三个是是否希望内核记住
//            super.onGeolocationPermissionsShowPrompt(origin, callback);
//        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);//注意个函数，第二个参数就是是否同意定位权限，第三个是是否希望内核记住
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        //=========HTML5定位==========================================================


        //=========多窗口的问题==========================================================
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebViewTransport transport = (WebViewTransport) resultMsg.obj;
            transport.setWebView(view);
            resultMsg.sendToTarget();
            return true;
        }
        //=========多窗口的问题==========================================================

        /**H5选择本地照片适配*/

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            webValueCallbackLater5 = filePathCallback;
            showPhotoSelectDialog();
            return true;
        }

        public void openFileChooser(ValueCallback uploadMsg) {
            webValueCallbackBefore5 = uploadMsg;
            showPhotoSelectDialog();
        }

        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            webValueCallbackBefore5 = uploadMsg;
            showPhotoSelectDialog();
        }

        public void openFileChooser(ValueCallback uploadMsg, String acceptType, String capture) {
            webValueCallbackBefore5 = uploadMsg;
            showPhotoSelectDialog();
        }

    };
    private OnHtml5ImToP2PListener onHtml5ImToP2PListener;
    private OnHtml5CallPhoneListener mOnHtml5CallPhoneListener;
    private OnHtml5Progress onHtml5Progress;
    WebViewClient webViewClient = new WebViewClient() {

        /***
         * 多页面在同一个WebView中打开，就是不新建activity或者调用系统浏览器打开
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.e("打开连接：====" + url);
            if (!TextUtils.isEmpty(url) && isJsBridgeUrl(url)) {
                // JSbridge的处理逻辑
                String spuId = "";
                try {
                    Uri uri = Uri.parse(url);
                    spuId = uri.getQueryParameter("spuId");
                } catch (Exception e) {
                    e.printStackTrace();
                    spuId = "";
                }
                if (!TextUtils.isEmpty(spuId)) {
                    ARouter.getInstance().build("/mall/productDetail")
                            .withString("spuid", spuId)
                            .navigation();
                }
                return true;
            } else if (url.startsWith("tel:")) {
                LogUtils.e("打开连接：==22222==" + url);
                if (mOnHtml5CallPhoneListener != null) {
                    mOnHtml5CallPhoneListener.onCallPhone(url);


                }
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (onHtml5Progress != null) {
                onHtml5Progress.onProgressStart();
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (onHtml5Progress != null) {
                onHtml5Progress.onProgressFinish();
            }


        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            super.onReceivedSslError(view, handler, error);
            handler.proceed();
            if (onHtml5Progress != null) {
                onHtml5Progress.onProgressError();
            }
        }
    };

    public GeneralWebView(Context context) {
        super(context);
        init();
    }

    public GeneralWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GeneralWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void destroy() {
        stopLoading();
        // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
        getSettings().setJavaScriptEnabled(false);
        clearHistory();
        clearView();
        removeAllViews();

        mOnHtml5CallPhoneListener = null;
        onHtml5Listener = null;
        onHtml5Progress = null;
        onHtml5ImToP2PListener = null;
        super.destroy();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        progressbar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 5);
        params.gravity = Gravity.TOP;
        progressbar.setLayoutParams(params);

        Drawable drawable = getContext().getResources().getDrawable(R.drawable.webview_progress_bar);
        progressbar.setProgressDrawable(drawable);
        addView(progressbar);

        WebSettings mWebSettings = getSettings();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");
        mWebSettings.setLoadsImagesAutomatically(true);
        String ua = mWebSettings.getUserAgentString();
        mWebSettings.setUserAgentString(ua + "; JMB");
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportMultipleWindows(true);
        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        //addJavascriptInterface(new AndroidtoJsBean(this, getContext()), "JsToNative");
        //缓存数据
        saveData(mWebSettings);
        newWin(mWebSettings);
        setWebChromeClient(webChromeClient);
        setWebViewClient(webViewClient);
    }

    /***
     * 多窗口的问题
     */
    private void newWin(WebSettings mWebSettings) {
        //html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下
        //然后 复写 WebChromeClient的onCreateWindow方法
        mWebSettings.setSupportMultipleWindows(false);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    /***
     * HTML5数据存储
     */
    private void saveData(WebSettings mWebSettings) {
        //有时候网页需要自己保存一些关键数据,Android WebView 需要自己设置
//        if (NetworkUtils.isConnected()) {
//            mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
//        } else {
//            mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
//        }
//        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
    }

    public void setOnHtml5Listener(OnHtml5Listener onHtml5Listener) {
        this.onHtml5Listener = onHtml5Listener;
    }

    private boolean isJsBridgeUrl(String url) {
        return url.contains("/pages/store/goodsdetail.html");
    }

    public void setOnHtml5ImToP2PListener(OnHtml5ImToP2PListener onHtml5ImToP2PListener) {
        this.onHtml5ImToP2PListener = onHtml5ImToP2PListener;
    }

    private void onPhotoSelectedCallback(List<LocalMedia> photoPaths) {
        Uri[] uris;
        if (photoPaths == null) {
            uris = new Uri[]{Uri.parse("")};
        } else {
            uris = new Uri[photoPaths.size()];
            for (int i = 0; i < photoPaths.size(); i++) {
                uris[i] = Uri.fromFile(new File(photoPaths.get(i).getCompressPath()));
            }
        }
        if (webValueCallbackBefore5 != null) {
            webValueCallbackBefore5.onReceiveValue(uris[0]);
        }
        if (webValueCallbackLater5 != null) {
            webValueCallbackLater5.onReceiveValue(uris);
        }
        webValueCallbackBefore5 = null;
        webValueCallbackLater5 = null;
    }

    private void showPhotoSelectDialog() {
//        final String[] stringItems = {"拍照", "相册"};
//        final NewActionSheetDialog dialog = new NewActionSheetDialog(getContext(), stringItems, null);
//        dialog.title("选择图片")
//                .itemTextSize(20)
//                .itemTextColor(0xFF3C3C3C)
//                .titleTextSize_SP(13)
//                .titleTextColor(0xFF8F8E94)
//                .cornerRadius(12)
//                .show();
//        dialog.setOnDismissListener(dialogInterface -> {
//            if (!isItemClicked) {
//                onPhotoSelectedCallback(null);
//            }
//            isItemClicked = false;
//
//        });
//        dialog.setOnOperItemClickL((parent, view, position, id) -> {
//            isItemClicked = true;
//            if (position == 0) {
//                openCamera();
//            } else if (position == 1) {
//                openAlbum();
//            }
//            dialog.dismiss();
//        });
    }

    /***
     * 打开相册
     */
    private void openAlbum() {
//        new RxPermissions((FragmentActivity) AppManager.getAppManager().getTopActivity()).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SimpleSubscriber<Boolean>() {
//                    @Override
//                    public void onNext(Boolean aBoolean) {
//                        if (aBoolean) {
//                            PictureSelector.create(AppManager.getAppManager().getTopActivity())
//                                    .openGallery(PictureMimeType.ofImage())
//                                    .imageEngine(GlideEngine.createGlideEngine())
//                                    .theme(R.style.picture_default_style)
//                                    .selectionMode(PictureConfig.MULTIPLE)
//                                    .isCompress(true)
//                                    .forResult(PictureConfig.CHOOSE_REQUEST);
//                        } else {
//                            ToastUtils.showErrorToast("相机,存储访问权限被拒绝！");
//                        }
//                    }
//
//                    @Override
//                    protected void onError(ApiException ex) {
//
//                    }
//                });
    }

//    @Override
//    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
////        FrameLayout.LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
////        lp.x = l;
////        lp.y = t;
////        progressbar.setLayoutParams(lp);
//        super.onScrollChanged(l, t, oldl, oldt);
//    }

    /***
     * 打开相机
     */
    private void openCamera() {
//        new RxPermissions((FragmentActivity) AppManager.getAppManager().getTopActivity()).request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SimpleSubscriber<Boolean>() {
//                    @Override
//                    public void onNext(Boolean aBoolean) {
//                        if (aBoolean) {
//                            PictureSelector.create(AppManager.getAppManager().getTopActivity())
//                                    .openCamera(PictureMimeType.ofImage())
//                                    .imageEngine(GlideEngine.createGlideEngine())
//                                    .isCompress(true)
//                                    .forResult(PictureConfig.CHOOSE_REQUEST);
//                        } else {
//                            ToastUtils.showErrorToast("相机,存储访问权限被拒绝！");
//                        }
//                    }
//
//                    @Override
//                    protected void onError(ApiException ex) {
//
//                    }
//                });
    }

    public void setMedia(List<LocalMedia> media) {
        onPhotoSelectedCallback(media);
    }

    public void setOnHtml5CallPhoneListener(OnHtml5CallPhoneListener onHtml5CallPhoneListener) {
        mOnHtml5CallPhoneListener = onHtml5CallPhoneListener;
    }

    public void setOnHtml5Progress(OnHtml5Progress onHtml5Progress) {
        this.onHtml5Progress = onHtml5Progress;
    }

    public void setOnProgressChanged(OnProgressChanged progressChanged) {
        this.progressChanged = progressChanged;
    }

    public interface OnHtml5ImToP2PListener {
        void imToP2P(String accountId);
    }

    public interface OnHtml5Listener {
        void onReceivedTitle(String title);
    }

    public interface OnHtml5CallPhoneListener {
        void onCallPhone(String Phone);
    }

    public interface OnHtml5Progress {
        void onProgressStart();

        void onProgressFinish();

        void onProgressError();
    }

    public interface OnProgressChanged {
        void onProgressChanged(int progress);

    }
}
