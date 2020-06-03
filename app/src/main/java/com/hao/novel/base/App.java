package com.hao.novel.base;


import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;

import androidx.core.content.ContextCompat;

import com.hao.lib.Util.MMKVManager;
import com.hao.lib.Util.SPUtils;
import com.hao.lib.base.MI2App;
import com.hao.novel.R;
import com.hao.novel.db.manage.DBCore;
import com.hao.novel.service.DownLoadNovelBinder;
import com.hao.novel.service.DownLoadNovelService;
import com.hao.novel.service.NovolDownTask;

public class App extends MI2App {
    //用于控制数据库更新导致序列化 ID出现错误
    public static final long serialVersionUID = 1L;
    static App app;
    private DownLoadNovelBinder binder;

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
    }

    private void init() {
        MMKVManager.getInstance(getExternalFilesDir(null).getPath());
        getMi2Theme().setBackground(ContextCompat.getDrawable(this, R.color.white));
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
                binder.sendCmd(new NovolDownTask(DownLoadNovelService.NovelDownTag.noveltype));
                binder.sendCmd(new NovolDownTask(DownLoadNovelService.NovelDownTag.allTitle));
            }
        }
    };

    public void stopService() {
        unbindService(connection);
    }


}
