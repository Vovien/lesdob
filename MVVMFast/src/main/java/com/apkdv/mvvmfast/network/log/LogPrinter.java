package com.apkdv.mvvmfast.network.log;

/**
 * Created by lengyue on 2019/8/2.
 * Email : lengyue@apkdv.com
 * Description :
 */
class LogPrinter {

    /**
     * 打印方法加锁，解决并发请求时日志输出错乱问题
     *
     * @param entity 被打印的日志对象
     */
    static synchronized void printLog(LogEntity entity) {
        entity.printLog();
    }

}
