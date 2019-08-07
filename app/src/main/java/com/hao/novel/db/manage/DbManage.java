package com.hao.novel.db.manage;

import com.hao.novel.db.dao.NovelIntroductionDao;
import com.hao.novel.spider.data.NovelIntroduction;

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
     * 添加单个小说数据
     *
     * @param novelIntroduction
     */
    public static void addNovelIntrodution(NovelIntroduction novelIntroduction) {
        List<NovelIntroduction> novelIntroductions = new ArrayList<>();
        novelIntroductions.add(novelIntroduction);
        addNovelIntrodution(novelIntroductions);
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
