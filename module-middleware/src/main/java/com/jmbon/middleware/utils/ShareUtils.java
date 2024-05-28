package com.jmbon.middleware.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.apkdv.mvvmfast.ktx.ToastKTXKt;
import com.jmbon.middleware.bean.ShareBean;
import com.mob.MobSDK;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 类描述: 分享工具
 */
public class ShareUtils {

    private ShareUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 微信
     */
    public static final int TYPE_SHARE_WECHAT = 0x01;
    /**
     * 朋友圈
     */
    public static final int TYPE_SHARE_WECHAT_FRIENDS = 0x01 << 1;
    /**
     * 微博
     */
    public static final int TYPE_SHARE_SINA = 0x01 << 2;
    /**
     * QQ
     */
    public static final int TYPE_SHARE_QQ = 0x01 << 4;

    public static final String JMB_ICON = "https://www.jmbon.com/_nuxt/img/logo.ebd46a0.png";
    public static final String JMB_ICON2 = "https://image.jmbon.com/uploads/app/common/ic_launcher.png";

    public static void share(int shareType, ShareBean shareBean, PlatformActionListener platformActionListener) {
        Platform plat = null;
//        if (platformActionListener != null) {
//            platformActionListener.onComplete(null, 0, null);
//        }
        //微信
        if (shareType == TYPE_SHARE_WECHAT) {

            plat = ShareSDK.getPlatform(Wechat.NAME);
            if (!plat.isClientValid()) {
                //没有安装QQ
                ToastKTXKt.showToast("您还没有安装微信，请安装后再试");
                return;
            }
        }
        //朋友圈
        else if (shareType == TYPE_SHARE_WECHAT_FRIENDS) {

            plat = ShareSDK.getPlatform(WechatMoments.NAME);
            if (!plat.isClientValid()) {
                //没有安装QQ
                ToastKTXKt.showToast("您还没有安装微信，请安装后再试");
                return;
            }
        }
        //微博
        if (shareType == TYPE_SHARE_SINA) {
            plat = ShareSDK.getPlatform(SinaWeibo.NAME);
            if (!plat.isClientValid()) {
                //没有安装QQ
                ToastKTXKt.showToast("您还没有安装微博，请安装后再试");
                return;
            }


        } else if (shareType == TYPE_SHARE_QQ) {
            plat = ShareSDK.getPlatform(QQ.NAME);
            if (!plat.isClientValid()) {
                //没有安装QQ
                ToastKTXKt.showToast("您还没有安装QQ，请安装后再试");
                return;
            }
        }
        if (plat == null) {
            return;
        }
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        oks.setPlatform(plat.getName());
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        if (!TextUtils.isEmpty(shareBean.title)) {
            oks.setTitle(shareBean.title);
        }
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        if (!TextUtils.isEmpty(shareBean.url)) {
            oks.setTitleUrl(shareBean.url);
        }
        // text是分享文本，所有平台都需要这个字段
        if (!TextUtils.isEmpty(shareBean.content)) {
            if (shareType == TYPE_SHARE_SINA) {
                oks.setText(shareBean.title);
            } else {
                oks.setText(shareBean.content);
            }
        }
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        // 必须要有一个图片
        oks.setImageUrl(!TextUtils.isEmpty(shareBean.image) ? shareBean.image : JMB_ICON);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        if (!TextUtils.isEmpty(shareBean.localImgPath)) {
//            //确保SDcard下面存在此张图片
//            oks.setImagePath(shareBean.localImgPath);
//        }
        // url仅在微信（包括好友和朋友圈）中使用
        if (!TextUtils.isEmpty(shareBean.url)) {
            oks.setUrl(shareBean.url);
        }
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        if (!TextUtils.isEmpty(shareBean.content)) {
            oks.setComment(shareBean.content);
        }
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("LesDoB");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("https://jmbon.com/");
        if (platformActionListener != null) {
            oks.setCallback(platformActionListener);
        }
        oks.setDisappearShareToast(true);
        //启动分享
        oks.show(MobSDK.getContext());
    }


    public interface ShareImageCallBack {
        void callBack();

        void succeed();
    }
//
//    public interface LoginCallBack {
//        void success(String unionid);
//
//        void failure(String msg);
//
//        void cancel();
//    }

    public static void shareQQImage(String imagePath, final ShareImageCallBack shareImageCallBack) {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        if (!qq.isClientValid()) {
            ToastKTXKt.showToast("您还没有安装QQ，请安装后再试");
        } else {
            QQ.ShareParams sp = new QQ.ShareParams();
            sp.setImagePath(imagePath);
            qq.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    shareImageCallBack.succeed();
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    shareImageCallBack.callBack();
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    shareImageCallBack.callBack();
                }
            });
            qq.share(sp);
        }


    }

    public static void shareWx(Bitmap bitmap, final ShareImageCallBack shareImageCallBack) {
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        if (!wechat.isClientValid()) {
            ToastKTXKt.showToast("您还没有安装微信，请安装后再试");
        } else {
            Wechat.ShareParams sp = new Wechat.ShareParams();
            sp.setShareType(Platform.SHARE_IMAGE);
            sp.setImageData(bitmap);
            wechat.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    shareImageCallBack.succeed();
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    shareImageCallBack.callBack();
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    shareImageCallBack.callBack();
                }
            });
            wechat.share(sp);
        }
    }


    public static void shareWxFriend(Bitmap bitmap, final ShareImageCallBack shareImageCallBack) {
        Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
        if (!wechat.isClientValid()) {
            ToastKTXKt.showToast("您还没有安装微信，请安装后再试");
        } else {
            Wechat.ShareParams sp = new Wechat.ShareParams();
            sp.setShareType(Platform.SHARE_IMAGE);
            sp.setImageData(bitmap);
            wechat.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    shareImageCallBack.succeed();
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    shareImageCallBack.callBack();
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    shareImageCallBack.callBack();
                }
            });
            wechat.share(sp);
        }
    }

    public static void shareWx(ShareBean bean, final PlatformActionListener shareImageCallBack) {
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        if (!wechat.isClientValid()) {
            ToastKTXKt.showToast("您还没有安装微信，请安装后再试");
        } else {
            Platform.ShareParams shareParams = new Platform.ShareParams();
            shareParams.setText(bean.content);
            shareParams.setTitle(bean.title);
            shareParams.setUrl(bean.url);
            shareParams.setImageUrl(bean.image);


            shareParams.setWxPath(bean.path + bean.pageId);//小程序页面路径
            shareParams.setWxUserName("gh_a1937d8ca562");//小程序的原始ID
            shareParams.setShareType(Platform.SHARE_WXMINIPROGRAM);
//        shareParams.setWxWithShareTicket(true);
            // 正式版:0，测试版:1，体验版:2
            shareParams.setWxMiniProgramType(2);
            wechat.setPlatformActionListener(shareImageCallBack);
            wechat.share(shareParams);
        }
    }

}