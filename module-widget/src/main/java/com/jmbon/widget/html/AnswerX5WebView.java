package com.jmbon.widget.html;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class AnswerX5WebView extends Html5WebView {
    public AnswerX5WebView(Context context) {
        super(context);
        View view = getView();
        view.setVerticalScrollBarEnabled(false);
        view.setHorizontalScrollBarEnabled(false);
        view.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public AnswerX5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public AnswerX5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    public void setOnHtml5Progress(OnHtml5Progress onHtml5Progress) {
        super.setOnHtml5Progress(onHtml5Progress);

    }

    @Override
    public void scrollBy(int x, int y) {
        getView().scrollBy(x, y);
    }

    public void scroll(int y) {
        getView().scrollBy(0, y);
    }

    @Override
    public void destroy() {
        // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
//        setWebChromeClient(null);
//        setWebViewClient(null);
//        loadUrl("about:blank");
        //pauseTimers();
        super.destroy();

    }
}
