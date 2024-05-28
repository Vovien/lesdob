/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jmbon.middleware.comment.utils;

import android.text.Spannable;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.qmuiteam.qmui.link.QMUILinkTouchDecorHelper;

/**
 * @author cginechen
 * @date 2017-03-20
 */

public class MyScrollingMovementMethod extends ScrollingMovementMethod {

    int firstY = 0;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        int y = (int) event.getY();

        if (action == MotionEvent.ACTION_DOWN) {
            widget.setEnabled(true);
            firstY = y;

        } else if (action == MotionEvent.ACTION_UP) {

            if (Math.abs(y - firstY) < 5) {
                return super.onTouchEvent(widget, buffer, event);
            } else {
                widget.setEnabled(false);
                //避免滑动响应textview的点击事件
                widget.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        widget.setEnabled(true);
                    }
                }, 100);
                return true;
            }
        }
        return super.onTouchEvent(widget, buffer, event);
    }

    public static MovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new MyScrollingMovementMethod();

        return sInstance;
    }

    private static MyScrollingMovementMethod sInstance;
    private static final QMUILinkTouchDecorHelper sHelper = new QMUILinkTouchDecorHelper();
}
