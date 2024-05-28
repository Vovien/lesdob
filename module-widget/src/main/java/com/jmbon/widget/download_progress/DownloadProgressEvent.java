package com.jmbon.widget.download_progress;

import androidx.annotation.Keep;

@Keep
public class DownloadProgressEvent {
    public int progress;

    public String apkPath;

    public DownloadProgressEvent(int progress) {
        this.progress = progress;
    }

    public DownloadProgressEvent(int progress, String apkPath) {
        this.progress = progress;
        this.apkPath = apkPath;
    }
}
