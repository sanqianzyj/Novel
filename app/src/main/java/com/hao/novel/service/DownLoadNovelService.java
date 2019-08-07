package com.hao.novel.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.hao.novel.R;
import com.hao.novel.base.App;
import com.hao.novel.spider.SpiderNovelFromBiQu;
import com.hao.novel.ui.MainActivity;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于更新获取小说相关数据
 * 创建一个前台服务 防止GC回收
 */
public class DownLoadNovelService extends Service {
    Thread downloadThread;
    boolean isRunThread = true;
    List<NovelDownTag> tag = new ArrayList<>();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    DownLoadNovelBinder binder = new DownLoadNovelBinder() {
        long downAllNum;//下载的总数量
        long nowDownNum;//当前下载数
        DownListener downListener;


        @Override
        public void sendCmd(NovelDownTag o) {
            tag.add(o);
        }

        @Override
        public void sendCmd(NovelDownTag o, int index) {
            try {
                tag.add(index, o);
            } catch (Exception e) {
                tag.add(o);
            }
        }

        @Override
        public Object getMassage() {
            Log.i("DownLoadNovelService", "getMassage: ");
            return null;
        }

        @Override
        public void setDownListener(DownListener downListener) {
            this.downListener = downListener;
        }

        public DownListener getDownListener() {
            return downListener;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        Log.i("DownLoadNovelService", "onCreate");
        super.onCreate();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("程序正在运行")
                .setContentText("程序正在运行")
                .setOngoing(false)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent);

        // 兼容  API 26，Android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 第三个参数表示通知的重要程度，默认则只在通知栏闪烁一下
            NotificationChannel notificationChannel = new NotificationChannel("com.hao.novel", App.getInstance().getOpPackageName(), NotificationManager.IMPORTANCE_DEFAULT);
            // 注册通道，注册后除非卸载再安装否则不改变
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId("com.hao.novel");
        }
        notificationManager.notify(11, builder.build());

        //开启线程
        startDownloadThread();
    }

    private void startDownloadThread() {
        if (downloadThread == null) {
            downloadThread = new Thread(runnable);
        }
        downloadThread.start();
    }


    Runnable runnable = new Runnable() {
        boolean isRun = true;

        @Override
        public void run() {
            isRun = isRunThread;
            while (true) {
                if (!isRun) {
                    break;
                }
                if (tag.size() > 0) {
                    switch (tag.get(0)) {
                        case none://不做任何操作
                            break;
                        case allTitle://下载所有的小说标题
                            SpiderNovelFromBiQu.getAllNovel();
                            break;
                        case allDetail://完善所有的小说的介绍信息
                            break;
                        case novelallchapter://下载单本小说的所有章节
                            break;
                        case singlechaptercontent://下载单章内容
                            break;
                        case novelallchaptercontent://下载所有小说的内容
                            break;
                    }
                    tag.remove(0);
                }
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("DownLoadNovelService", "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("DownLoadNovelService", "onDestroy: ");
        isRunThread = false;
        runnable.run();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("DownLoadNovelService", "onConfigurationChanged: ");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        Log.i("DownLoadNovelService", "onLowMemory: ");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Log.i("DownLoadNovelService", "onTrimMemory: ");
        super.onTrimMemory(level);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("DownLoadNovelService", "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i("DownLoadNovelService", "onRebind: ");
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i("DownLoadNovelService", "onTaskRemoved: ");
        super.onTaskRemoved(rootIntent);
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        Log.i("DownLoadNovelService", "dump: ");
        super.dump(fd, writer, args);
    }

    enum NovelDownTag {
        none, allTitle, allDetail, novelallchapter, singlechaptercontent, novelallchaptercontent
    }
}
