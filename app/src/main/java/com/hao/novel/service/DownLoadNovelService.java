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
import com.hao.novel.db.manage.DbManage;
import com.hao.novel.spider.SpiderNovelFromBiQu;
import com.hao.novel.spider.data.NovelChapter;
import com.hao.novel.spider.data.NovelIntroduction;
import com.hao.novel.spider.data.NovelType;
import com.hao.novel.ui.activity.SearchBookActivity;

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
    List<NovolDownTask> tag = new ArrayList<NovolDownTask>() {
        @Override
        public boolean add(NovolDownTask novolDownTask) {
            if (novolDownTask.novelDownTag.equals(NovelDownTag.novelDetail)
                    || novolDownTask.novelDownTag.equals(NovelDownTag.novelallchapterTitle)
                    || novolDownTask.novelDownTag.equals(NovelDownTag.novelallchaptercontent)) {
                if (!(novolDownTask.object instanceof NovelIntroduction)) {
                    throw new NumberFormatException("添加任务异常，类型不匹配 novolDownTask.object 不是 NovelIntroduction类型");
                }
            } else if (novolDownTask.novelDownTag.equals(NovelDownTag.singlechaptercontent)) {
                if (!(novolDownTask.object instanceof NovelChapter)) {
                    throw new NumberFormatException("添加任务异常，类型不匹配 novolDownTask.object 不是 NovelChapter¬类型");
                }
            }
            return super.add(novolDownTask);
        }

        @Override
        public String toString() {
            String cont = "暂无内容";
            for (int i = 0; i < this.size(); i++) {
                cont = cont + "     " + get(i).novelDownTag;
            }
            return cont;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    DownLoadNovelBinder binder = new DownLoadNovelBinder() {


        @Override
        public void sendCmd(NovolDownTask o) {
            tag.add(o);
        }

        @Override
        public void sendCmd(NovolDownTask o, int index) {
            try {
                Log.i("小说", "添加任务=" + o.getNovelDownTag());
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
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, SearchBookActivity.class);
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
                    if (tag.get(0) != null && tag.get(0).downListener != null && tag.get(0).isneedListener) {
                        tag.get(0).downListener.startDown();
                    }
                    Log.i("小说", "当前执行任务" + tag.get(0).getNovelDownTag() + "    任务数:" + tag.size() + "  任务列表：" + tag.toString());
                    switch (tag.get(0).getNovelDownTag()) {
                        case none://不做任何操作
                            break;
                        case allTitle://下载所有的小说标题
                            SpiderNovelFromBiQu.getAllNovel();
                            tag.add(new NovolDownTask(NovelDownTag.allDetail, ""));
                            break;
                        case allDetail://完善所有的小说的介绍信息
                            NovelIntroduction novelIntroduction = DbManage.getNoCompleteDetailNovelInfo();
                            if (novelIntroduction != null) {
                                SpiderNovelFromBiQu.getAllNovelDetailInfo(novelIntroduction);
                            }
                            break;
                        case novelDetail://下载单本小说的信息 包含章节信息
                            SpiderNovelFromBiQu.getAllNovelDetailInfo((NovelIntroduction) tag.get(0).getObject());
                            break;
                        case singlechaptercontent://下载单章内容
                            SpiderNovelFromBiQu.getNovelContent((NovelChapter) tag.get(0).getObject());
                            break;
                        case novelallchaptercontent://下载小说的所有内容
                            SpiderNovelFromBiQu.getAllNovelContent((NovelIntroduction) tag.get(0).getObject());
                            break;
                        case noveltype://获取小说分类
                            SpiderNovelFromBiQu.getNovelType();
                            break;
                        case noveltypelist://通过类别来获取小说列表
                            NovelType novelType = (NovelType) tag.get(0).getObject();
                            SpiderNovelFromBiQu.getTypeNovelList(novelType.getUrl(), novelType.getType());
                            break;
                    }
                    if (tag.get(0).downListener != null) {
                        tag.get(0).downListener.endDown();
                    }
                    Log.i("小说", "删除任务=" + tag.get(0).getNovelDownTag());
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

    public enum NovelDownTag {
        none, allTitle, allDetail, novelDetail, novelallchapterTitle, singlechaptercontent, novelallchaptercontent, noveltype, noveltypelist
    }


}
