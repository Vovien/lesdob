package com.jmbon.middleware.bean;

import com.google.gson.annotations.SerializedName;

/***
 * <pre>
 * desc：分享bean
 * <pre>
 */

public class ShareBean {
    public String title;
    public String content;
    public String url;
    @SerializedName(value = "imageUrl", alternate = {"image"})
    public String image;
    public String localImgPath;
    public String shareAutor;
    public int pageId;
    public int answerId;
    public String friendTargetDesc; //qq微信好友指定分享文案，做差异化
    public String path;
    public String shareText;
    public int shareIcon;

    public ShareBean() {
    }

    /**
     * 分享 + 顶部操作栏
     *
     * @param title
     * @param content
     * @param url
     * @param image
     * @param pageId
     * @param shareText 操作栏文本
     * @param shareIcon 操作 icon
     */
    public ShareBean(String title, String content, String url, String image, int pageId, String shareText, int shareIcon) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.image = image;
        this.pageId = pageId;
        this.shareText = shareText;
        this.shareIcon = shareIcon;
    }

    public ShareBean(String title, String content, String url, String image) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.image = image;
    }

    public ShareBean(String title, String content, String url, String image, int pageId) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.image = image;
        this.pageId = pageId;
    }

    public ShareBean(String title, String content, String url, String image, int pageId, String friendTargetDesc) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.image = image;
        this.pageId = pageId;
        this.friendTargetDesc = friendTargetDesc;
    }

    public ShareBean(String title, String content, String url, String image, int pageId, int answerId) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.image = image;
        this.pageId = pageId;
        this.answerId = answerId;
    }

    public ShareBean(String title, String content, String url, String image, int pageId, int answerId, String friendTargetDesc) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.image = image;
        this.pageId = pageId;
        this.answerId = answerId;
        this.friendTargetDesc = friendTargetDesc;
    }

    public ShareBean(String title, String content, String url, String image, String localImgPath, String shareAutor) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.image = image;
        this.localImgPath = localImgPath;
        this.shareAutor = shareAutor;
    }

    public ShareBean(String title, String content, String url, String image, String localImgPath, String shareAutor, int pageId) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.image = image;
        this.localImgPath = localImgPath;
        this.shareAutor = shareAutor;
        this.pageId = pageId;
    }

    public ShareBean(String title, String content, String url, String image, String localImgPath, String shareAutor, int pageId, int answerId) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.image = image;
        this.localImgPath = localImgPath;
        this.shareAutor = shareAutor;
        this.pageId = pageId;
        this.answerId = answerId;
    }
}
