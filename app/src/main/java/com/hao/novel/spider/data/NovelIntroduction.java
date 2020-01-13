package com.hao.novel.spider.data;


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
    String novelName;//小说名
    String novelAutho;//作者
    String novelCover;//封面
    String novelIntroduce;//文字介绍
    String novelType;//小说类型
    String novelNewChapterTitle;//最新章节
    String novelNewChapterUrl;//最新章节地址
    @Unique
    String novelChapterListUrl;//章节列表地址
    String nowRead;//当前阅读的章节
    String nowReadID;//当前阅读的章节Id
    boolean isComplete;//信息是否完善
    boolean isFav;//是否收藏


    @Generated(hash = 1742870737)
    public NovelIntroduction(Long id, String novelName, String novelAutho, String novelCover,
            String novelIntroduce, String novelType, String novelNewChapterTitle,
            String novelNewChapterUrl, String novelChapterListUrl, String nowRead,
            String nowReadID, boolean isComplete, boolean isFav) {
        this.id = id;
        this.novelName = novelName;
        this.novelAutho = novelAutho;
        this.novelCover = novelCover;
        this.novelIntroduce = novelIntroduce;
        this.novelType = novelType;
        this.novelNewChapterTitle = novelNewChapterTitle;
        this.novelNewChapterUrl = novelNewChapterUrl;
        this.novelChapterListUrl = novelChapterListUrl;
        this.nowRead = nowRead;
        this.nowReadID = nowReadID;
        this.isComplete = isComplete;
        this.isFav = isFav;
    }


    @Generated(hash = 1432430798)
    public NovelIntroduction() {
    }


    @Override
    public String toString() {
        return "NovelIntroduction{" +
                "小说名='" + novelName + '\n' +
                ", 作者='" + novelAutho + '\n' +
                ", 封面='" + novelCover + '\n' +
                ", 小说类型='" + novelType + '\n' +
                ", 介绍='" + novelIntroduce + '\n' +
                ", 最新章节='" + novelNewChapterTitle + '\n' +
                ", 最新章节地址='" + novelNewChapterUrl + '\n' +
                ", 章节列表地址='" + novelChapterListUrl + '\n' +
                ", 信息是否完善='" + isComplete + '\n' +
                '}';
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNovelName() {
        return this.novelName;
    }

    public void setNovelName(String novelName) {
        this.novelName = novelName;
    }

    public String getNovelAutho() {
        return this.novelAutho;
    }

    public void setNovelAutho(String novelAutho) {
        this.novelAutho = novelAutho;
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


    public boolean getIsComplete() {
        return this.isComplete;
    }


    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public String getNovelType() {
        return novelType;
    }

    public void setNovelType(String novelType) {
        this.novelType = novelType;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }


    public String getNowRead() {
        return this.nowRead;
    }


    public void setNowRead(String nowRead) {
        this.nowRead = nowRead;
    }


    public String getNowReadID() {
        return this.nowReadID;
    }


    public void setNowReadID(String nowReadID) {
        this.nowReadID = nowReadID;
    }


    public boolean getIsFav() {
        return this.isFav;
    }


    public void setIsFav(boolean isFav) {
        this.isFav = isFav;
    }
}
