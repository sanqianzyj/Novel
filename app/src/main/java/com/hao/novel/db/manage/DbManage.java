package com.hao.novel.db.manage;

import com.hao.novel.base.NovelContent;
import com.hao.novel.db.dao.NovelIntroductionDao;
import com.hao.novel.db.dao.NovelTypeDao;
import com.hao.novel.spider.data.NovelIntroduction;
import com.hao.novel.spider.data.NovelType;

import java.util.ArrayList;
import java.util.List;

public class DbManage {
    /**
     * 批量添加小说数据
     *
     * @param novelIntroductionList
     */
    public static void addNovelIntrodution(List<NovelIntroduction> novelIntroductionList) {
        DBCore.getDaoSession().getNovelIntroductionDao().insertOrReplaceInTx(novelIntroductionList);
    }

    /**
     * 获取数据库中保存的小说数量
     *
     * @return 小说数量
     */
    public static long getAllNovelCount() {
        return DBCore.getDaoSession().getNovelIntroductionDao().queryBuilder().count();
    }

    /**
     * 添加单个小说数据
     *
     * @param novelIntroduction
     */
    public static void addNovelIntrodution(NovelIntroduction novelIntroduction) {
        List<NovelIntroduction> novelIntroductions = new ArrayList<>();
        novelIntroductions.add(novelIntroduction);
        addNovelIntrodution(novelIntroductions);
    }

    public static void addNovelType(List<NovelType> novelTypes) {
        DBCore.getDaoSession().getNovelTypeDao().deleteAll();
        DBCore.getDaoSession().getNovelTypeDao().insertOrReplaceInTx(novelTypes);
    }

    public static List<NovelType> getNovelType() {
        return DBCore.getDaoSession().getNovelTypeDao().queryBuilder().where(NovelTypeDao.Properties.From.eq(NovelContent.from)).list();
    }

    /**
     * 获取小说数量
     */
    public long getNovelIntrodutionCount() {
        return DBCore.getDaoSession().getNovelIntroductionDao().queryBuilder().count();
    }


    /**
     * 通过章节列表地址来区分小说
     *
     * @param url
     * @return
     */
    public static NovelIntroduction checkNovelIntrodutionByUrl(String url) {
        return DBCore.getDaoSession().getNovelIntroductionDao().queryBuilder().where(
                NovelIntroductionDao.Properties.NovelChapterListUrl.eq(url)).limit(1).unique();
    }
}
