package com.hao.novel.db.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.hao.novel.spider.data.NovelType;
import com.hao.novel.spider.data.NovelIntroduction;

import com.hao.novel.db.dao.NovelTypeDao;
import com.hao.novel.db.dao.NovelIntroductionDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig novelTypeDaoConfig;
    private final DaoConfig novelIntroductionDaoConfig;

    private final NovelTypeDao novelTypeDao;
    private final NovelIntroductionDao novelIntroductionDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        novelTypeDaoConfig = daoConfigMap.get(NovelTypeDao.class).clone();
        novelTypeDaoConfig.initIdentityScope(type);

        novelIntroductionDaoConfig = daoConfigMap.get(NovelIntroductionDao.class).clone();
        novelIntroductionDaoConfig.initIdentityScope(type);

        novelTypeDao = new NovelTypeDao(novelTypeDaoConfig, this);
        novelIntroductionDao = new NovelIntroductionDao(novelIntroductionDaoConfig, this);

        registerDao(NovelType.class, novelTypeDao);
        registerDao(NovelIntroduction.class, novelIntroductionDao);
    }
    
    public void clear() {
        novelTypeDaoConfig.clearIdentityScope();
        novelIntroductionDaoConfig.clearIdentityScope();
    }

    public NovelTypeDao getNovelTypeDao() {
        return novelTypeDao;
    }

    public NovelIntroductionDao getNovelIntroductionDao() {
        return novelIntroductionDao;
    }

}
