package com.hao.novel.spider.data;


import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 小说简介 用于存储小说的名称，作者，地址等
 */
@Entity
public class NovelIntroduction {
    @Id(autoincrement = true)
    Long id;
    @Unique
    String novelNameAndAuthot;//小说名及作者 中间用>.<隔开
    String novelCover;//封面
    String novelIntroduce;//文字介绍
    String novelNewChapterTitle;//最新章节
    @Unique
    String novelNewChapterUrl;//最新章节地址
    @Unique
    String novelChapterListUrl;//章节列表地址

    @Generated(hash = 1086496346)
    public NovelIntroduction(Long id, String novelNameAndAuthot, String novelCover,
                             String novelIntroduce, String novelNewChapterTitle,
                             String novelNewChapterUrl, String novelChapterListUrl) {
        this.id = id;
        this.novelNameAndAuthot = novelNameAndAuthot;
        this.novelCover = novelCover;
        this.novelIntroduce = novelIntroduce;
        this.novelNewChapterTitle = novelNewChapterTitle;
        this.novelNewChapterUrl = novelNewChapterUrl;
        this.novelChapterListUrl = novelChapterListUrl;
    }

    @Generated(hash = 1432430798)
    public NovelIntroduction() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNovelNameAndAuthot() {
        return this.novelNameAndAuthot;
    }

    public void setNovelNameAndAuthot(String novelNameAndAuthot) {
        this.novelNameAndAuthot = novelNameAndAuthot;
    }

    public String getNovelCover() {
        return this.novelCover;
    }

    public void setNovelCover(String novelCover) {
        this.novelCover = novelCover;
    }

    public String getNovelIntroduce() {
        return this.novelIntroduce;
    }

    public void setNovelIntroduce(String novelIntroduce) {
        this.novelIntroduce = novelIntroduce;
    }

    public String getNovelNewChapterTitle() {
        return this.novelNewChapterTitle;
    }

    public void setNovelNewChapterTitle(String novelNewChapterTitle) {
        this.novelNewChapterTitle = novelNewChapterTitle;
    }

    public String getNovelNewChapterUrl() {
        return this.novelNewChapterUrl;
    }

    public void setNovelNewChapterUrl(String novelNewChapterUrl) {
        this.novelNewChapterUrl = novelNewChapterUrl;
    }

    public String getNovelChapterListUrl() {
        return this.novelChapterListUrl;
    }

    public void setNovelChapterListUrl(String novelChapterListUrl) {
        this.novelChapterListUrl = novelChapterListUrl;
    }


}
