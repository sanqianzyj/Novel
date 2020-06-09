package com.hao.novel.base;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.core.content.ContextCompat;

import com.hao.lib.Util.MMKVManager;
import com.hao.lib.Util.SPUtils;
import com.hao.lib.base.MI2App;
import com.hao.novel.R;
import com.hao.novel.broadcast.AlarmReceiver;
import com.hao.novel.db.manage.DBCore;
import com.hao.novel.db.manage.DbManage;
import com.hao.novel.service.DownLoadNovelBinder;
import com.hao.novel.service.DownLoadNovelService;
import com.hao.novel.service.NovolDownTask;
import com.hao.novel.spider.data.NovelChapter;
import com.hao.novel.spider.data.NovelIntroduction;
import com.hao.novel.ui.activity.MiNovelShelfActivity;
import com.hao.novel.ui.activity.NovelDetailActivity;
import com.hao.novel.ui.activity.WelcomeActivity;
import com.hao.novel.ui.used.ReadInfo;

import java.util.List;

public class App extends MI2App {
    //用于控制数据库更新导致序列化 ID出现错误
    public static final long serialVersionUID = 1L;
    static App app;
    private DownLoadNovelBinder binder;
    private long lastClickTime = 0;
    private Notification.Builder notification;

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        SPUtils.init(this);
        DBCore.init(this);
        startBackRunService();
        init();
        creatAlarm();
        creatNotification();
    }

    private void creatNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "whatever"; //根据业务执行
            String channelName = "whatever conent"; //这个是channelid 的解释，在安装的时候会展示给用户看
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(notificationChannel);
            notification = new Notification.Builder(this, "whatever");
        } else {
            notification = new Notification.Builder(this);
        }
        String title = "暂无阅读历史";
        String content = "";
        Intent intent = new Intent(this, WelcomeActivity.class);
        List<ReadInfo> readInfos = DbManage.checkedAllReadInfo();
        if (readInfos.size() > 0) {
            try {
                NovelIntroduction novelIntroduction = DbManage.checkNovelByUrl(readInfos.get(0).getNovelChapterListUrl());
                title = novelIntroduction.getNovelName();
                NovelChapter chapter = DbManage.checkNovelChaptterByUrl(readInfos.get(0).getNovelChapterUrl());
                content = chapter.getChapterName();
                intent = new Intent(this, NovelDetailActivity.class);
                intent.putExtra("novelId", novelIntroduction.getNovelChapterListUrl());
            } catch (Exception e) {

            }
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        notification.setContentTitle(title)  //设置标题
                .setContentText(content) //设置内容
//                .setWhen(System.currentTimeMillis())  //设置时间
                .setSmallIcon(R.mipmap.shuji_black)  //设置小图标
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.shuji_black));
        notificationManager.notify(0, notification.build());
    }

    private void init() {
        MMKVManager.getInstance(getExternalFilesDir(null).getPath());
        getMi2Theme().setBackground(ContextCompat.getDrawable(this, R.color.white));
    }

    public void creatAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setAction("clock");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT < 19) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(), 1000, pendingIntent);
        } else if (android.os.Build.VERSION.SDK_INT < 23) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(), 1, pendingIntent);
        } else {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(), 1, pendingIntent);
        }
    }

    public DownLoadNovelBinder getBinder() {
        return binder;
    }

    private void startBackRunService() {
        Intent bindIntent = new Intent(this, DownLoadNovelService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }

    ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (DownLoadNovelBinder) service;
            if (binder != null) {
                NovelUsedUpdate novelUsedUpdate = DbManage.checkedNovelUpdate();
                if (novelUsedUpdate != null && System.currentTimeMillis() - novelUsedUpdate.getLastUpdatime() > 3600000) {
                    binder.sendCmd(new NovolDownTask(DownLoadNovelService.NovelDownTag.noveltype));
//                    novelUsedUpdate.setLastUpdatime(System.currentTimeMillis());
                    DbManage.saveNovelUpdate(novelUsedUpdate);
                    binder.sendCmd(new NovolDownTask(DownLoadNovelService.NovelDownTag.allTitle));
                }
                DbManage.getNovelTypeByAllNovel();
            }
        }
    };

    public void stopService() {
        unbindService(connection);
    }


    //判断是否重复点击
    public boolean checkedDoubleClick() {
        if (System.currentTimeMillis() - lastClickTime < 1000) {
            return false;
        }
        lastClickTime = System.currentTimeMillis();
        return true;
    }


}
