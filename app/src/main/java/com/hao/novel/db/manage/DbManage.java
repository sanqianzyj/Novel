package com.hao.novel.db.manage;

import android.util.Log;

import com.hao.novel.base.NovelContent;
import com.hao.novel.db.dao.NovelChapterDao;
import com.hao.novel.db.dao.NovelIntroductionDao;
import com.hao.novel.db.dao.NovelTypeDao;
import com.hao.novel.spider.data.NovelChapter;
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

    public static void addNovelChapter(List<NovelChapter> chapters) {
        DBCore.getDaoSession().getNovelChapterDao().insertOrReplaceInTx(chapters);
    }

    public static void updateNovelChapter(NovelChapter chapters) {
        DBCore.getDaoSession().getNovelChapterDao().insertOrReplaceInTx(chapters);
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


    public static List<NovelIntroduction> getAllNovelNoDetail() {
        return DBCore.getDaoSession().getNovelIntroductionDao().queryBuilder().list();
    }

    //获取单个未完成详细信息的小说
    public static NovelIntroduction getNoCompleteDetailNovelInfo() {
        return DBCore.getDaoSession().getNovelIntroductionDao().queryBuilder().where(NovelIntroductionDao.Properties.IsComplete.eq(false)).limit(1).unique();
    }

    //查询单个未加载的章节内容
    public static NovelChapter checkNovelAllNoChapterContent(NovelIntroduction novelIntroduction) {
        return DBCore.getDaoSession().getNovelChapterDao().queryBuilder().where(NovelChapterDao.Properties.Nid.eq(novelIntroduction.getId()), NovelChapterDao.Properties.IsComplete.eq(false)).limit(1).unique();
    }

    public static List<NovelIntroduction> getNovelByType(String type, int page) {
        Log.i("小说", "查询" + type);
        List<NovelIntroduction> introductions = DBCore.getDaoSession().getNovelIntroductionDao().queryBuilder().where(NovelIntroductionDao.Properties.NovelType.like("%" + type + "%")).offset(10 * page).limit(10).list();
        Log.i("小说", "查询=" + introductions.size());
        return introductions;
    }

    public static NovelIntroduction checkNovelInfoById(long id) {
      return  DBCore.getDaoSession().getNovelIntroductionDao().queryBuilder().where(NovelIntroductionDao.Properties.Id.eq(id)).limit(1).unique();
    }

    public static List<NovelChapter> getChapterById(long id) {
        return  DBCore.getDaoSession().getNovelChapterDao().queryBuilder().where(NovelChapterDao.Properties.Nid.eq(id)).list();

    }
}
