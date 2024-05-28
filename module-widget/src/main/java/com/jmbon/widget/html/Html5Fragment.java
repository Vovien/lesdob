package com.jmbon.widget.html;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jmbon.widget.R;
import com.jmbon.widget.html.event.WebViewScrollEvent;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

//@Route(path = "/common/html/fragment")
public class Html5Fragment extends Fragment implements
        Html5WebView.OnHtml5Listener, Html5WebView.OnHtml5CallPhoneListener, Html5WebView.OnHtml5Progress {
    public static final String URL = "url";
    public static final String HTML_DATA = "html_data";
    private String mUrl;
    private Html5WebView mWebView;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        mWebView = view.findViewById(R.id.html5_web_view);
        mWebView.setOnHtml5Listener(this);
        mWebView.setOnHtml5CallPhoneListener(this);
        mWebView.setOnHtml5Progress(this);
//        if (getActivity() instanceof BaseActivity)
//            ((BaseActivity) getActivity()).mTbLeftView.setOnClickListener(this);

        if (getArguments() != null) {
            mUrl = getArguments().getString(URL);
            String htmlData = getArguments().getString(HTML_DATA);
            if (htmlData == null || htmlData.isEmpty()) {
                mWebView.loadUrl(mUrl);
            } else {
                //设置字符编码，避免乱码
                mWebView.getSettings().setDefaultTextEncodingName("utf-8");
                mWebView.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null);
                mWebView.post(() -> mWebView.loadUrl("javascript:getHtag()"));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_webview, container, false);
    }

    @Override
    public void onDestroyView() {
        if (mWebView != null) {

//            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
//            mWebView.clearHistory();
//
//            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    private boolean goBack(int keyCode) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    @Override
    public void onReceivedTitle(String title) {
        //setTitle
    }


    /***
     * 登录广播
     //     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventBus(UserLoginEvent event) {
//        mWebView.reload();
//    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScrollEvent(WebViewScrollEvent event) {
        mWebView.getView().scrollTo(event.x, event.y);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCallPhone(final String Phone) {
        if (null != getActivity()) {

        }

    }

    @Override
    public void onProgressStart() {

    }

    @Override
    public void onProgressFinish() {

    }

    @Override
    public void onProgressError() {

    }

    public Html5WebView getmWebView() {
        return mWebView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
            if (localMedia != null && !localMedia.isEmpty()) {
                mWebView.setMedia(localMedia);
            } else {
                mWebView.setMedia(null);
            }
        }

    }

}