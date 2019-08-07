package com.hao.lib.base;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.hao.lib.base.theme.AppThemeSetting;

import java.util.ArrayList;
import java.util.List;

public class MI2App extends Application {
    static MI2App app;

    public static MI2App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }


    private List<Activity> activities = new ArrayList<>();
    private Activity activitie = null;

    public Activity getNowActivitie() {
        return activitie;
    }

    public void addActivity(Activity baseActivity) {
        activities.add(baseActivity);
        activitie = baseActivity;
    }

    public void removeActivity(MI2Activity baseActivity) {
        activities.add(baseActivity);
    }


    //finish所有的存于APP的activity
    public void finishAll() {
        finishActivitys(null);
    }

    //finish掉带有同一tag的activity
    public void finishActivitys(String tag) {
        for (int i = 0; i < activities.size(); i++) {
            if (tag == null || tag.equals("")) {
                activities.get(i).finish();
            } else {
                if (activities.get(i) instanceof MI2Activity) {
                    if (((MI2Activity) activities.get(i)).getMI2TAG().equals(tag)) {
                        activities.get(i).finish();
                    }
                }
            }
        }
    }


    //记录最后一次点击事件的时间
    private long beforClickTime = 0;

    //判断是否在1秒之内重复点击
    public boolean checkFastClick() {
        long time = System.currentTimeMillis() - beforClickTime;
        beforClickTime = System.currentTimeMillis();
        return time < 1000;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setWindowsBackground(Drawable resouce) {
        if (activitie != null) {
            activitie.getWindow().getDecorView().setBackground(resouce);
        }
    }


    public AppThemeSetting getMi2Theme() {
        return AppThemeSetting.getInstance();
    }

    public void gotoSetting() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(intent);
    }
}
