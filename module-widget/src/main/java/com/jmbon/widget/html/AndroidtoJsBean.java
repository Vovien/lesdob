package com.jmbon.widget.html;

import android.content.Context;

import com.tencent.smtt.sdk.WebView;


/***
 * <pre>
 * author：张俊
 * date： 2017/8/9
 * desc：
 * <pre>
 */

public class AndroidtoJsBean {

    private final Context mContext;
    private final WebView webView;

    public AndroidtoJsBean(WebView webView, Context context) {
        mContext = context;
        this.webView = webView;
    }


//    @JavascriptInterface
//    public String getUserId() {
//        if (!Data.isLogin()) {
//            ARouter.getInstance().build("/common/login").navigation();
//            if (mContext instanceof Activity) {
//                ((Activity) mContext).finish();
//            }
//            return "";
//        }
//        return "{\"userId\":\"" + Data.getUserId() + "\",\"token\":\"" + Data.getUser().getAccessToken() + "\",\"qiNiuPrefix\":\"" + Data.getUser().getQiNiuPrefix() + "\"}";
//    }
//
//    @JavascriptInterface
//    public String getToken() {
//        String token = Data.isLogin() ? Data.getUser().getTicket() : "";
//        return "{\"token\":\"" + token + "\"}";
//    }
//
//
//    @JavascriptInterface
//    public void loginOut() {
////        if (mContext instanceof Activity)
////            (((Activity) mContext).getApplication()).registerActivityLifecycleCallbacks(new WebViewActivityLifecycle());
//        ARouter.getInstance().build("/common/login")
//                .withBoolean(TagConstant.TOURIST_NEED_LOGIN_KEY,Data.isTourist())
//                .navigation();
//    }
//
//    @JavascriptInterface
//    public void tokenTimeOut() {
//        if (AppManager.getAppManager().topActivityIsLogin()) return;
////        if (mContext instanceof Activity)
////            (((Activity) mContext).getApplication()).registerActivityLifecycleCallbacks(new WebViewActivityLifecycle());
//        Data.exitAccount(Utils.getContext());
//        EventBus.getDefault().post(new UserLoginEvent(false));
//        ARouter.getInstance().build("/common/login").withString("SignOutType", SIGNOUTTYPE_SESSION).navigation();
//    }
//
//    @JavascriptInterface
//    public void share(
//    ) {
//        JsShareBean jsShareBean = null;
//        try {
//            jsShareBean = GsonUtil.fromJson(json, JsShareBean.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (jsShareBean == null) {
//            return;
//        }
//        mShareBottomDialog = new ShareBottomDialog(mContext, new ShareBean(jsShareBean.getTitle(), jsShareBean.getDes(), jsShareBean.getUrl(), jsShareBean.getImg()));
//        mShareBottomDialog.show();
//    }

//
//    @JavascriptInterface
//    public void toCoachDetail(String coachId) {
//        ARouter.getInstance().build("/community/coach_detail").withString("coachId", coachId).navigation();
//    }
//
//    @JavascriptInterface
//    public void back() {
//        EventBus.getDefault().post(new WebViewFinish());
//        if (mContext instanceof Activity) {
//            ((Activity) mContext).finish();
//        }
//    }
//
//
//    @JavascriptInterface
//    public void createOrder(String productId) {
//        String id = null;
//        String productType = null;
//        try {
//            JSONObject jsonObject = new JSONObject(productId);
//            id = jsonObject.getString("id");
//            productType = jsonObject.getString("productType");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        ARouter.getInstance().build("/mall/O2OConfirmOrderActivity")
//                .withBoolean(TagConstant.NEED_LOGIN_KEY, true)
//                .withBoolean(TagConstant.NEED_NOT_TOURIST_KEY,true)
//                .withString("productType", productType).withString("productId", id).navigation();
//    }
//
//    @JavascriptInterface
//    public void chatRoom(String equipmentId) {
//        try {
//            JSONObject jsonObject = new JSONObject(equipmentId);
//            String Id = jsonObject.getString("equipmentId");
//            if (!TextUtils.isEmpty(Id)) {
//                ARouter.getInstance().build("/common/ComfortBridgeChatActivity")
//                        .withString(AppConstant.COMFORTBRIDGE_CHAT_USERID, Id)
//                        .navigation();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @JavascriptInterface
//    public void bindDevice(String empty) {
//        boolean emptyJson = false;
//        try {
//            JSONObject jsonObject = new JSONObject(empty);
//            emptyJson = jsonObject.getBoolean("empty");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        ARouter.getInstance().build("/common/ComfortBridgeRegistActivity")
//                .withBoolean(AppConstant.COMFORTBRIDGE_EMPTY, emptyJson)
//                .navigation();
//    }
//
//
//    @JavascriptInterface
//    public void openAxq(String mineUrl) {
//        try {
//            JSONObject jsonObject = new JSONObject(mineUrl);
//            String url = jsonObject.getString("url");
//            if (!TextUtils.isEmpty(url)) {
//                ARouter.getInstance().build("/common/html5")
//                        .withString(Html5Activity.URL, url)
//                        .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        .navigation();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @JavascriptInterface
//    public void openPage(String url) {
//        try {
//            if (!TextUtils.isEmpty(url)) {
//                ARouter.getInstance().build("/common/html5")
//                        .withString(Html5Activity.URL, url).withBoolean(Html5Activity.NO_TITLE, true).navigation();
//                if (mContext instanceof Activity) {
//                    ((Activity) mContext).finish();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    @JavascriptInterface
//    public void appOpenPage(String url) {
//        try {
//            if (!TextUtils.isEmpty(url)) {
//                ARouter.getInstance().build("/common/html5")
//                        .withString(Html5Activity.URL, BuildConfig.H5_HOST + "/" + url).withBoolean(Html5Activity.NO_TITLE, false)
//                        .withBoolean("showMenu", true).navigation();
//                if (mContext instanceof Activity) {
//                    ((Activity) mContext).finish();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @JavascriptInterface
//    public void publishOrder() {
//        EventBus.getDefault().post(new AddReportEvent());
//        if (mContext instanceof Activity) {
//            ((Activity) mContext).finish();
//        }
//    }
//
//    @JavascriptInterface
//    public void toLogin() {
//        ARouter.getInstance().build("/common/login").withBoolean("isO2O", true).navigation();
//        if (mContext instanceof Activity) {
//            ((Activity) mContext).finish();
//        }
//    }
//
//
//    @JavascriptInterface
//    public void toMyCoupon() {
//        ARouter.getInstance().build("/mine/MineCardCouponsActivityActivity")
//                .withBoolean(TagConstant.NEED_LOGIN_KEY, true)
//                .navigation();
//    }
//
//    @JavascriptInterface
//    public void toCouponProduct(String jsonStr) {
//        try {
//            JSONObject jsonObject = new JSONObject(jsonStr);
//            String couponId = jsonObject.getString("couponId");
//            if (!TextUtils.isEmpty(couponId)) {
//                ARouter.getInstance()
//                        .build("/mall/MallUseMyTicketActivity")
//                        .withBoolean(TagConstant.NEED_LOGIN_KEY, true)
//                        .withString("couponId", couponId).navigation();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @JavascriptInterface
//    public void setTitle(String jsonStr) {
//        try {
//            JSONObject jsonObject = new JSONObject(jsonStr);
//            String title = jsonObject.getString("title");
//            if (!TextUtils.isEmpty(title)) {
//                EventBus.getDefault().post(new HtmlTitleEvent(title));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @JavascriptInterface
//    public void loginTimeOut() {
//        if (AppManager.getAppManager().topActivityIsLogin()) return;
//        Data.exitAccount(Utils.getContext());
//        EventBus.getDefault().post(new UserLoginEvent(false));
//        ARouter.getInstance().build("/app/common/main").navigation();
//        JPushInterface.setAliasAndTags(Utils.getContext(), "", null, null);
//        ARouter.getInstance().build("/common/login").withString("SignOutType", SIGNOUTTYPE_SESSION).navigation();
//    }
//
//    private void toNewWebPage() {
//        String token = "&token=" + Data.getUser().getTicket();
//        ((Activity) mContext).runOnUiThread(() -> {
//            ARouter.getInstance().build("/common/html5")
//                    .withString(Html5Activity.URL, webView.getUrl() + token).navigation();
//            if (mContext instanceof Activity) {
//                ((Activity) mContext).finish();
//            }
//        });
//
//    }
//
//    @JavascriptInterface
//    public void getPayParam(String payParam) {
//        if (TextUtils.isEmpty(payParam)) return;
//        PigletH5PayCallback pigletH5PayCallback = GsonUtil.fromJson(payParam, PigletH5PayCallback.class);
//        if (pigletH5PayCallback != null && pigletH5PayCallback.getDataRes().getPayParam() != null) {
//            weChatPay(pigletH5PayCallback);
//        }
//    }
//
//    private void weChatPay(PigletH5PayCallback results) {
//        PigletH5PayCallback.DataResBean.PayParamBean payParam = results.getDataRes().getPayParam();
//        WXPay wxPay = WXPay.getInstance(AppManager.getAppManager().getTopActivity(), payParam.getAppid());
//        WXPayInfoImpli wxPayInfoImpli = new WXPayInfoImpli();
//        wxPayInfoImpli.setAppId(payParam.getAppid());
//        wxPayInfoImpli.setNonceStr(payParam.getNoncestr());
//        wxPayInfoImpli.setPackageName(payParam.getPackageX());
//        wxPayInfoImpli.setPartnerId(payParam.getPartnerid());
//        wxPayInfoImpli.setPrepayId(payParam.getPrepayid());
//        wxPayInfoImpli.setSign(payParam.getSign());
//        wxPayInfoImpli.setTimeStamp(payParam.getTimestamp());
//        //策略场景类调起支付方法开始支付，以及接收回调。
//        PayUtil.pay(wxPay, AppManager.getAppManager().getTopActivity(), wxPayInfoImpli, new IPayCallback() {
//            @Override
//            public void success() {
//                toPigletDetai(results);
//            }
//
//            @Override
//            public void failed() {
//                toPigletDetai(results);
//            }
//
//
//            @Override
//            public void cancel() {
//                toPigletDetai(results);
//            }
//        });
//
//    }
//
//    private void toPigletDetai(PigletH5PayCallback results) {
//        ARouter.getInstance()
//                .build("/mall/index/O2OWebViewActivity")
//                .withString(Html5Activity.URL, results.getDataRes().getBackUrl())
//                .withBoolean(TagConstant.NEED_LOGIN_KEY, true)
//                .navigation();
//        EventBus.getDefault().post(new PigletH5ReLoadEvent());
//    }
//
//    @JavascriptInterface
//    public void getMap(String longlat) {
//        if (!checkBaiduInstalled(Utils.getContext())) {
//            ToastUtils.showErrorToast("请安装百度地图");
//            return;
//        }
//        double lon = 0d;
//        double lat = 0d;
//        try {
//            JSONObject jsonObject = new JSONObject(longlat);
//            JSONObject longlat1 = jsonObject.getJSONObject("longlat");
//            lon = longlat1.getDouble("long");
//            lat = longlat1.getDouble("lat");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        if (lon != 0d && lat != 0d) {
//            Intent i1 = new Intent();
//            i1.setData(Uri.parse("baidumap://map/geocoder?location=" + lat + "," + lon + "&src=andr.baidu.openAPIdemo"));
//            i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Utils.getContext().startActivity(i1);
//        }
//    }
//
//    public boolean checkBaiduInstalled(Context context) {
//        Uri uri = Uri.parse("baidumap://map/geocoder");
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
//        return componentName != null;
//    }
//
//    @JavascriptInterface
//    public void getGeo() {
//        EventBus.getDefault().post(new H5LocationEvent());
//    }
//    @JavascriptInterface
//    public void mallNavTo(String data) {
//        EventBus.getDefault().post(new PageNavToEvent(data.replace("\"","")));
//
//    }
}
