package com.hao.novel.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.hao.novel.spider.data.NovelIntroduction;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NOVEL_INTRODUCTION".
*/
public class NovelIntroductionDao extends AbstractDao<NovelIntroduction, Long> {

    public static final String TABLENAME = "NOVEL_INTRODUCTION";

    /**
     * Properties of entity NovelIntroduction.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property NovelNameAndAuthot = new Property(1, String.class, "novelNameAndAuthot", false, "NOVEL_NAME_AND_AUTHOT");
        public final static Property NovelCover = new Property(2, String.class, "novelCover", false, "NOVEL_COVER");
        public final static Property NovelIntroduce = new Property(3, String.class, "novelIntroduce", false, "NOVEL_INTRODUCE");
        public final static Property NovelNewChapterTitle = new Property(4, String.class, "novelNewChapterTitle", false, "NOVEL_NEW_CHAPTER_TITLE");
        public final static Property NovelNewChapterUrl = new Property(5, String.class, "novelNewChapterUrl", false, "NOVEL_NEW_CHAPTER_URL");
        public final static Property NovelChapterListUrl = new Property(6, String.class, "novelChapterListUrl", false, "NOVEL_CHAPTER_LIST_URL");
    }


    public NovelIntroductionDao(DaoConfig config) {
        super(config);
    }
    
    public NovelIntroductionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NOVEL_INTRODUCTION\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NOVEL_NAME_AND_AUTHOT\" TEXT UNIQUE ," + // 1: novelNameAndAuthot
                "\"NOVEL_COVER\" TEXT," + // 2: novelCover
                "\"NOVEL_INTRODUCE\" TEXT," + // 3: novelIntroduce
                "\"NOVEL_NEW_CHAPTER_TITLE\" TEXT," + // 4: novelNewChapterTitle
                "\"NOVEL_NEW_CHAPTER_URL\" TEXT UNIQUE ," + // 5: novelNewChapterUrl
                "\"NOVEL_CHAPTER_LIST_URL\" TEXT);"); // 6: novelChapterListUrl
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NOVEL_INTRODUCTION\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, NovelIntroduction entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String novelNameAndAuthot = entity.getNovelNameAndAuthot();
        if (novelNameAndAuthot != null) {
            stmt.bindString(2, novelNameAndAuthot);
        }
 
        String novelCover = entity.getNovelCover();
        if (novelCover != null) {
            stmt.bindString(3, novelCover);
        }
 
        String novelIntroduce = entity.getNovelIntroduce();
        if (novelIntroduce != null) {
            stmt.bindString(4, novelIntroduce);
        }
 
        String novelNewChapterTitle = entity.getNovelNewChapterTitle();
        if (novelNewChapterTitle != null) {
            stmt.bindString(5, novelNewChapterTitle);
        }
 
        String novelNewChapterUrl = entity.getNovelNewChapterUrl();
        if (novelNewChapterUrl != null) {
            stmt.bindString(6, novelNewChapterUrl);
        }
 
        String novelChapterListUrl = entity.getNovelChapterListUrl();
        if (novelChapterListUrl != null) {
            stmt.bindString(7, novelChapterListUrl);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, NovelIntroduction entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String novelNameAndAuthot = entity.getNovelNameAndAuthot();
        if (novelNameAndAuthot != null) {
            stmt.bindString(2, novelNameAndAuthot);
        }
 
        String novelCover = entity.getNovelCover();
        if (novelCover != null) {
            stmt.bindString(3, novelCover);
        }
 
        String novelIntroduce = entity.getNovelIntroduce();
        if (novelIntroduce != null) {
            stmt.bindString(4, novelIntroduce);
        }
 
        String novelNewChapterTitle = entity.getNovelNewChapterTitle();
        if (novelNewChapterTitle != null) {
            stmt.bindString(5, novelNewChapterTitle);
        }
 
        String novelNewChapterUrl = entity.getNovelNewChapterUrl();
        if (novelNewChapterUrl != null) {
            stmt.bindString(6, novelNewChapterUrl);
        }
 
        String novelChapterListUrl = entity.getNovelChapterListUrl();
        if (novelChapterListUrl != null) {
            stmt.bindString(7, novelChapterListUrl);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public NovelIntroduction readEntity(Cursor cursor, int offset) {
        NovelIntroduction entity = new NovelIntroduction( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // novelNameAndAuthot
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // novelCover
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // novelIntroduce
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // novelNewChapterTitle
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // novelNewChapterUrl
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // novelChapterListUrl
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, NovelIntroduction entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNovelNameAndAuthot(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNovelCover(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNovelIntroduce(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setNovelNewChapterTitle(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setNovelNewChapterUrl(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setNovelChapterListUrl(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(NovelIntroduction entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(NovelIntroduction entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(NovelIntroduction entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
