package com.hao.novel.base;


import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hao.lib.Util.SPUtils;
import com.hao.lib.base.MI2App;
import com.hao.novel.db.manage.DBCore;
import com.hao.novel.service.DownLoadNovelBinder;
import com.hao.novel.service.DownLoadNovelService;

public class App extends MI2App {
    //用于控制数据库更新导致序列化 ID出现错误
    public static final long serialVersionUID = 1L;
    static App app;

    private long updateTime = 0;

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

        }
    };

    public void stopService() {
        unbindService(connection);
    }

}
