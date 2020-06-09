package com.hao.novel.db.manage;

import android.database.Cursor;
import android.util.Log;

import com.hao.novel.base.NovelContent;
import com.hao.novel.base.NovelUsedUpdate;
import com.hao.novel.base.NovelUsedUpdateDao;
import com.hao.novel.spider.data.NovelChapter;
import com.hao.novel.spider.data.NovelChapterDao;
import com.hao.novel.spider.data.NovelIntroduction;
import com.hao.novel.spider.data.NovelIntroductionDao;
import com.hao.novel.spider.data.NovelType;
import com.hao.novel.spider.data.NovelTypeDao;
import com.hao.novel.ui.used.ReadInfo;
import com.hao.novel.ui.used.ReadInfoDao;

import org.greenrobot.greendao.query.WhereCondition;

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
        DBCore.getDaoSession().getNovelTypeDao().insertOrReplaceInTx(novelTypes);
    }

    public static void addNovelType(NovelType novelType) {
        DBCore.getDaoSession().getNovelTypeDao().insertOrReplace(novelType);
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
     * 通过小说的章节地址来获取小说信息
     *
     * @param chapterListurl
     * @return
     */
    public static NovelIntroduction checkNovelByUrl(String chapterListurl) {
        return DBCore.getDaoSession().getNovelIntroductionDao().queryBuilder().where(
                NovelIntroductionDao.Properties.NovelChapterListUrl.eq(chapterListurl)).limit(1).unique();
    }

    /**
     * 通过章节地址获取小说章节详情
     *
     * @param url
     * @return
     */
    public static NovelChapter checkNovelChaptterByUrl(String url) {
        return DBCore.getDaoSession().getNovelChapterDao().queryBuilder().where(
                NovelChapterDao.Properties.ChapterUrl.eq(url)).limit(1).unique();
    }

    /**
     * 通过小说ID 和章节id获取小说章节详情
     *
     * @return
     */
    public static NovelChapter checkNovelChaptterById(String chapterListUrl, String chapterUrl) {
        return DBCore.getDaoSession().getNovelChapterDao().queryBuilder().where(
                NovelChapterDao.Properties.NovelChapterListUrl.eq(chapterListUrl), NovelChapterDao.Properties.ChapterUrl.eq(chapterUrl)).limit(1).unique();
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
        return DBCore.getDaoSession().getNovelChapterDao().queryBuilder().where(NovelChapterDao.Properties.NovelChapterListUrl.eq(novelIntroduction.getNovelChapterListUrl()), NovelChapterDao.Properties.IsComplete.eq(false)).limit(1).unique();
    }

    public static List<NovelIntroduction> getNovelByType(String type, int page) {
        List<NovelIntroduction> introductions = DBCore.getDaoSession().getNovelIntroductionDao().queryBuilder().where(NovelIntroductionDao.Properties.NovelType.like("%" + type + "%")).offset(10 * page).orderDesc(NovelIntroductionDao.Properties.CreatTime).limit(10).list();
        return introductions;
    }


    public static List<NovelChapter> getChapterById(String chaptyrlerList) {
        return DBCore.getDaoSession().getNovelChapterDao().queryBuilder().where(NovelChapterDao.Properties.NovelChapterListUrl.eq(chaptyrlerList)).list();

    }

    //通过小说id和章节id查询章节信息
    public static NovelChapter getChapterById(String chapterListUrl, String chapterUrl) {
        return DBCore.getDaoSession().getNovelChapterDao().queryBuilder().where(NovelChapterDao.Properties.NovelChapterListUrl.eq(chapterListUrl), NovelChapterDao.Properties.ChapterUrl.eq(chapterUrl)).limit(1).unique();

    }

    public static List<NovelIntroduction> checkNovelIntrodutionByStr(String string) {
        NovelIntroductionDao novelIntroductionDao = DBCore.getDaoSession().getNovelIntroductionDao();
        List<NovelIntroduction> novelIntroductions = novelIntroductionDao.queryBuilder().where(novelIntroductionDao.queryBuilder()
                .or(NovelIntroductionDao.Properties.NovelName.like("%" + string + "%"),
                        NovelIntroductionDao.Properties.NovelAutho.like("%" + string + "%"))).list();
        return novelIntroductions;
    }

    public static void saveReadInfo(ReadInfo readInfo) {
        ReadInfoDao readInfoDao = DBCore.getDaoSession().getReadInfoDao();
        readInfoDao.insertOrReplace(readInfo);
    }

    public static ReadInfo checkedReadInfo(String novelChapterlistUrl) {
        ReadInfoDao readInfoDao = DBCore.getDaoSession().getReadInfoDao();
        return readInfoDao.queryBuilder().where(ReadInfoDao.Properties.NovelChapterListUrl.eq(novelChapterlistUrl)).limit(1).unique();
    }

    public static NovelUsedUpdate checkedNovelUpdate() {
        NovelUsedUpdateDao novelUsedUpdateDao = DBCore.getDaoSession().getNovelUsedUpdateDao();
        NovelUsedUpdate novelUsedUpdate = novelUsedUpdateDao.queryBuilder().limit(1).unique();
        if (novelUsedUpdate == null) {
            novelUsedUpdate = new NovelUsedUpdate();
            novelUsedUpdate.setId(1);
        }
        return novelUsedUpdate;
    }

    public static void saveNovelUpdate(NovelUsedUpdate novelUsedUpdate) {
        NovelUsedUpdateDao novelUsedUpdateDao = DBCore.getDaoSession().getNovelUsedUpdateDao();
        novelUsedUpdateDao.insertOrReplace(novelUsedUpdate);
    }

    public static void getNovelTypeByAllNovel() {
        NovelIntroductionDao novelIntroductionDao = DBCore.getDaoSession().getNovelIntroductionDao();
        String strSql = "select * from NOVEL_INTRODUCTION order by NOVEL_TYPE";
        Cursor c = DBCore.getDaoSession().getDatabase().rawQuery(strSql, null);
        if (c.moveToFirst()) {
            String fromId = c.getString(c.getColumnIndex("NOVEL_TYPE"));
            Log.i("分组", "fromId=" + fromId);
        }

        c.close();
//        List<NovelIntroduction> list = novelIntroductionDao.queryBuilder().where(new WhereCondition.StringCondition("1=1 GROUP BY " + NovelIntroductionDao.Properties.NovelType)).list();
//        for (int i = 0; i < list.size(); i++) {
//            Log.i("分组", "i=" + i + "        " + list.get(i).getNovelType() + "\n");
//        }
        Log.i("分组", "完成");
    }

    public static List<ReadInfo> checkedAllReadInfo() {
        ReadInfoDao readInfoDao = DBCore.getDaoSession().getReadInfoDao();
        return readInfoDao.queryBuilder().orderDesc(ReadInfoDao.Properties.Date).list();
    }

    public static void removeReadInfo(ReadInfo readInfo) {
        ReadInfoDao readInfoDao = DBCore.getDaoSession().getReadInfoDao();
        readInfoDao.delete(readInfo);
    }

    public static List<NovelChapter> checkedNovelList(String novelChapterListUrl) {
        NovelChapterDao novelChapterDao = DBCore.getDaoSession().getNovelChapterDao();
        return novelChapterDao.queryBuilder().where(NovelChapterDao.Properties.NovelChapterListUrl.eq(novelChapterListUrl)).orderAsc(NovelChapterDao.Properties.CreateTime).list();
    }


    public static void groupNovelType() {
        String sql = "SELECT * FROM NOVEL_TYPE GROUP BY NOVEL_TYPE.TYPE";
        Cursor c = DBCore.getDaoSession().getDatabase().rawQuery(sql, null);

        while (c.moveToNext()) {
            Log.i("查询", c.getString(0) + "  " + c.getString(1) + "  " + c.getString(2) + "     " + c.getString(3) + " " + c.getString(4));
        }
    }
}