package com.jmbon.middleware.oss;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.ImagePersistRequest;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.blankj.utilcode.util.LogUtils;

/**
 * time   : 2021/4/16
 * desc   : OSS上传管理类
 * version: 1.0
 */
public class OssManager {

    private OSS oss;
    private final String accessKeyId;
    private final String bucketName;
    private final String accessKeySecret;
    private final String securityToken;
    private String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    private final String action = "image/auto-orient,1/quality,q_90/format,jpg/watermark,image_d2F0ZXJtYXJrL3dhdGVybWFyay5wbmc_eC1vc3MtcHJvY2Vzcz1pbWFnZS9yZXNpemUsUF8yMA,g_center,x_10,y_10";
    private final Context context;

    private ProgressCallback progressCallback;
    private OSSAsyncTask task;

    public OssManager(Context context, String accessKeyId, String accessKeySecret, String securityToken, String bucketName) {
        this.context = context;
        this.bucketName = bucketName;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.securityToken = securityToken;
    }


    public void initOSSClient() {
        if (oss != null) {
            return;
        }
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, securityToken);
        //这个初始化安全性没有Sts安全，如需要很高安全性建议用OSSStsTokenCredentialProvider创建（上一行创建方式）多出的参数SecurityToken为临时授权参数
        //OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(15); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        // oss为全局变量，endpoint是一个OSS区域地址
        oss = new OSSClient(context, endpoint, credentialProvider, conf);
        // 构造上传请求。
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * 异步上传
     *
     * @param context
     * @param filename
     * @param path
     */
    public void beginAsyncUpload(Context context, String filename, String path) {
        //通过填写文件名形成objectname,通过这个名字指定上传和下载的文件
        String objectname = filename;
        if (objectname == null || objectname.equals("")) {
            LogUtils.d("文件名不能为空");
            return;
        }
        //下面3个参数依次为bucket名，Object名，上传文件路径
        PutObjectRequest put = new PutObjectRequest(bucketName, objectname, path);


        // 在HTTP header中上传设置ACL权限
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader("x-oss-object-acl", "public-read");
        put.setMetadata(metadata);

        if (path == null || path.equals("")) {
            LogUtils.d("请选择图片....");
            //ToastUtils.showShort("请选择图片....");
            return;
        }
        LogUtils.d("正在上传中....");
        //ToastUtils.showShort("正在上传中....");
        // 异步上传，可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                LogUtils.d("currentSize: " + currentSize + " totalSize: " + totalSize);
                double progress = currentSize * 1.0 / totalSize * 100.f;

                if (progressCallback != null) {
                    progressCallback.onProgress(progress, currentSize, totalSize);
                }
            }
        });
        task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                LogUtils.d("UploadSuccess");
                //ToastUtils.showShort("上传成功");
                if (progressCallback != null) {
                    progressCallback.onUploadSuccess(result);
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                LogUtils.d("UploadFailure");
                if (progressCallback != null) {
                    progressCallback.onUploadFail(serviceException);
                }
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    LogUtils.e("UploadFailure：表示向OSS发送请求或解析来自OSS的响应时发生错误。\n" +
                            "  *例如，当网络不可用时，这个异常将被抛出");
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    LogUtils.e("UploadFailure：表示在OSS服务端发生错误");
                    LogUtils.e("ErrorCode", serviceException.getErrorCode());
                    LogUtils.e("RequestId", serviceException.getRequestId());
                    LogUtils.e("HostId", serviceException.getHostId());
                    LogUtils.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
        //task.cancel(); // 可以取消任务
        //task.waitUntilFinished(); // 可以等待直到任务完成
    }

    /**
     * 同步上传
     */
    public PutObjectResult beginSyncUpload(Context context, String filename, String path) {
        //通过填写文件名形成objectname,通过这个名字指定上传和下载的文件
        String objectname = filename;
        if (objectname == null || objectname.equals("")) {
            LogUtils.d("文件名不能为空");
            return null;
        }
        //下面3个参数依次为bucket名，Object名，上传文件路径
        PutObjectRequest put = new PutObjectRequest(bucketName, objectname, path);

        // 在HTTP header中上传设置ACL权限
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader("x-oss-object-acl", "public-read");
        put.setMetadata(metadata);

        if (path == null || path.equals("")) {
            LogUtils.d("请选择图片....");
            //ToastUtils.showShort("请选择图片....");
            return null;
        }
        LogUtils.d("正在上传中....");
        // 异步上传，可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                //  LogUtils.d("currentSize: " + currentSize + " totalSize: " + totalSize);
                double progress = currentSize * 1.0 / totalSize * 100.f;

                if (progressCallback != null) {
                    progressCallback.onProgress(progress, currentSize, totalSize);
                }
            }
        });

        PutObjectResult putObjectResult = null;
        try {
            putObjectResult = oss.putObject(put);
            if (progressCallback != null) {
                progressCallback.onUploadSuccess(putObjectResult);
            }
        } catch (ClientException | ServiceException e) {
            e.printStackTrace();
        } finally {
            return putObjectResult;
        }

    }

    /**
     * 同步上传,带水印
     */
    public PutObjectResult beginSyncUploadWithWaterMark(String filename, String path) {
        //通过填写文件名形成objectname,通过这个名字指定上传和下载的文件
        String objectname = filename;
        if (objectname == null || objectname.equals("")) {
            LogUtils.d("文件名不能为空");
            return null;
        }
        //下面3个参数依次为bucket名，Object名，上传文件路径
        PutObjectRequest put = new PutObjectRequest(bucketName, objectname, path);

        // 在HTTP header中上传设置ACL权限
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader("x-oss-object-acl", "public-read");
        put.setMetadata(metadata);

        if (path == null || path.equals("")) {
            LogUtils.d("请选择图片....");
            //ToastUtils.showShort("请选择图片....");
            return null;
        }
        LogUtils.d("正在上传中....");
        // 异步上传，可以设置进度回调
        put.setProgressCallback((request, currentSize, totalSize) -> {
            //  LogUtils.d("currentSize: " + currentSize + " totalSize: " + totalSize);
            double progress = currentSize * 1.0 / totalSize * 100.f;

            if (progressCallback != null) {
                progressCallback.onProgress(progress, currentSize, totalSize);
            }
        });

        PutObjectResult putObjectResult = null;
        try {
            putObjectResult = oss.putObject(put);
            //添加水印
            addWaterMark(bucketName, objectname, bucketName, objectname, action);
            if (progressCallback != null) {
                progressCallback.onUploadSuccess(putObjectResult);
            }
        } catch (ClientException | ServiceException e) {
            e.printStackTrace();
        } finally {
            return putObjectResult;
        }

    }

    public PutObjectResult syncUploadFile(String filename, String path) throws ServiceException, ClientException {
        if (filename == null || filename.equals("")) {
            LogUtils.d("文件名不能为空");
            return null;
        }
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(bucketName, filename, path);
        //  在HTTP header中上传设置ACL权限
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader("x-oss-object-acl", "public-read");
        put.setMetadata(metadata);
        return oss.putObject(put);
    }

    /**
     * 带水印上传文件
     *
     * @param filename
     * @param path
     * @return
     * @throws ServiceException
     * @throws ClientException
     */
    public PutObjectResult syncUploadFileWithWaterMark(String filename, String path) throws ServiceException, ClientException {
        if (filename == null || filename.equals("")) {
            LogUtils.d("文件名不能为空");
            return null;
        }
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(bucketName, filename, path);
        //  在HTTP header中上传设置ACL权限
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader("x-oss-object-acl", "public-read");
        put.setMetadata(metadata);
        PutObjectResult putObjectResult = oss.putObject(put);
        addWaterMark(bucketName, filename, bucketName, filename, action);
        return putObjectResult;

    }


    public void addWaterMark(String fromBucket, String fromObjectKey, String toBucket, String toObjectkey, String action) {
        // fromBucket和toBucket分别表示源Bucket和目的Bucket名称。
        // fromObjectKey和toObjectkey分别表示源Object和目标Object名称，其填写格式为指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg，不包含host。
        // action表示图片处理操作。
        ImagePersistRequest request = new ImagePersistRequest(fromBucket, fromObjectKey, toBucket, toObjectkey, action);

        try {
            oss.imagePersist(request);
            Log.e("watermark", "成功:" + toObjectkey);
        } catch (ClientException | ServiceException e) {
            e.printStackTrace();
            Log.e("watermark", "失败:" + toObjectkey);
        }
    }


    public void cancelTask() {
        if (task != null) {
            task.cancel();
        }
    }

    public void release() {
        if (oss != null) {
            oss = null;
        }
    }

    public ProgressCallback getProgressCallback() {
        return progressCallback;
    }

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    public interface ProgressCallback {
        void onProgress(double progress, long currentSize, long totalSize);

        void onUploadSuccess(PutObjectResult result);

        void onUploadFail(ServiceException serviceException);
    }

}

