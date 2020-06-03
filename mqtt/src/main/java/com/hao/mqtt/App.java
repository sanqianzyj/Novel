package com.hao.mqtt;


import android.app.Application;
import android.util.Log;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 *@author sun
 *
 * TODO:一句话描述
 */

public class App extends Application {
//    private DaoMaster.DevOpenHelper mHelper;
//    private SQLiteDatabase db;
//    private DaoMaster mDaoMaster;
//    private DaoSession mDaoSession;
    private final static String DB_NAME = "ZY_INFO";
    //****线程管理*****
    private ExecutorService ex = Executors.newFixedThreadPool(3);
    //机器号
    private String snCode;

    private static final String TAG = "App";
    private boolean updateTime = false;

    public boolean isUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(boolean updateTime) {
        this.updateTime = updateTime;
    }

    private static App instance = null;


    private boolean saveLog = false;

    public static App getInstance() {
        synchronized (App.class) {
            if (instance == null) {
                instance = new App();
            }
        }
        return instance;
    }

    public String getSnCode() {
        return snCode;
    }

    public void setSnCode(String snCode) {
        this.snCode = snCode;
    }



    //返回线程池对象
    public ExecutorService getEx() {
        return ex;
    }



    //    /**
//     * 设置greenDao
//     */
//    private void setDatabase() {
//        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
//        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
//        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
//        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
//        mHelper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
//        db = mHelper.getWritableDatabase();
//        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
//        mDaoMaster = new DaoMaster(db);
//        mDaoSession = mDaoMaster.newSession();
//    }
//
//    public DaoSession getDaoSession() {
//        return mDaoSession;
//    }
//
//    public SQLiteDatabase getDb() {
//        return db;
//    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e(TAG, "onTerminate: "+"程序退出" );
    }
}
