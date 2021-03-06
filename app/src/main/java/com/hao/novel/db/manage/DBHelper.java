package com.hao.novel.db.manage;

import android.content.Context;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.hao.novel.spider.data.NovelChapterDao;
import com.hao.novel.spider.data.NovelIntroductionDao;
import com.hao.novel.spider.data.NovelTypeDao;
import com.hao.novel.ui.used.DaoMaster;

import org.greenrobot.greendao.database.Database;


/**
 * 作者：Tangren_ on 2017/3/23 0023.
 * 邮箱：wu_tangren@163.com
 * TODO：更新数据库
 */


public class DBHelper extends DaoMaster.DevOpenHelper {

    DBHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        update(db, oldVersion, newVersion);
    }

    private void update(Database db, int oldVersion, int newVersion) {
        //把需要管理的数据库表DAO作为最后一个参数传入到方法中
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
                    @Override
                    public void onCreateAllTables(Database db, boolean ifNotExists) {
                        DaoMaster.createAllTables(db, ifNotExists);
                    }

                    @Override
                    public void onDropAllTables(Database db, boolean ifExists) {
                        DaoMaster.dropAllTables(db, ifExists);
                    }
                },
                NovelChapterDao.class,
                NovelIntroductionDao.class,
                NovelTypeDao.class
        );
    }
}
