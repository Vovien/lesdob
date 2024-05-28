package com.jmbon.widget.dialog;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author : leimg
 * time   : 2021/4/13
 * desc   :
 * version: 1.0
 */
public class CustomDialogTypeBean implements MultiItemEntity {

    public static final int TYPE_TITLE = 1;
    public static final int TYPE_MESSAGE = 2;
    public static final int TYPE_CANCEL = 3;


    public int type = 0;
    public String title = "";
    public int color = 0;

    public CustomDialogTypeBean(String title, int type) {
        this.type = type;
        this.title = title;
    }

    public CustomDialogTypeBean(String title, int color, int type) {
        this.type = type;
        this.title = title;
        this.color = color;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public String getTitle() {
        return title;
    }
}
