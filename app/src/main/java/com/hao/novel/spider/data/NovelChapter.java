package com.hao.novel.spider.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.hao.novel.base.App;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class NovelChapter implements Parcelable {
    private static final long serialVersionUID = App.serialVersionUID;
    @Id(autoincrement = true)
    Long Cid;//小说章节的唯一标示
    Long Nid;//小说的id
    private String chapterName;
    private String chapterUrl;
    String chapterContent;//本章小说内容
    String nextChapterUrl;//下一章
    String beforChapterUrl;//上一章
    boolean isComplete;//信息是否完善





    @Generated(hash = 891849972)
    public NovelChapter(Long Cid, Long Nid, String chapterName, String chapterUrl,
            String chapterContent, String nextChapterUrl, String beforChapterUrl,
            boolean isComplete) {
        this.Cid = Cid;
        this.Nid = Nid;
        this.chapterName = chapterName;
        this.chapterUrl = chapterUrl;
        this.chapterContent = chapterContent;
        this.nextChapterUrl = nextChapterUrl;
        this.beforChapterUrl = beforChapterUrl;
        this.isComplete = isComplete;
    }
    @Generated(hash = 922107813)
    public NovelChapter() {
    }


    protected NovelChapter(Parcel in) {
        if (in.readByte() == 0) {
            Cid = null;
        } else {
            Cid = in.readLong();
        }
        if (in.readByte() == 0) {
            Nid = null;
        } else {
            Nid = in.readLong();
        }
        chapterName = in.readString();
        chapterUrl = in.readString();
        chapterContent = in.readString();
        nextChapterUrl = in.readString();
        beforChapterUrl = in.readString();
        isComplete = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (Cid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(Cid);
        }
        if (Nid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(Nid);
        }
        dest.writeString(chapterName);
        dest.writeString(chapterUrl);
        dest.writeString(chapterContent);
        dest.writeString(nextChapterUrl);
        dest.writeString(beforChapterUrl);
        dest.writeByte((byte) (isComplete ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NovelChapter> CREATOR = new Creator<NovelChapter>() {
        @Override
        public NovelChapter createFromParcel(Parcel in) {
            return new NovelChapter(in);
        }

        @Override
        public NovelChapter[] newArray(int size) {
            return new NovelChapter[size];
        }
    };

    public Long getCid() {
        return this.Cid;
    }
    public void setCid(Long Cid) {
        this.Cid = Cid;
    }
    public Long getNid() {
        return this.Nid;
    }
    public void setNid(Long Nid) {
        this.Nid = Nid;
    }
    public String getChapterName() {
        return this.chapterName;
    }
    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }
    public String getChapterUrl() {
        return this.chapterUrl;
    }
    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }
    public String getChapterContent() {
        return this.chapterContent;
    }
    public void setChapterContent(String chapterContent) {
        this.chapterContent = chapterContent;
    }
    public String getNextChapterUrl() {
        return this.nextChapterUrl;
    }
    public void setNextChapterUrl(String nextChapterUrl) {
        this.nextChapterUrl = nextChapterUrl;
    }
    public String getBeforChapterUrl() {
        return this.beforChapterUrl;
    }
    public void setBeforChapterUrl(String beforChapterUrl) {
        this.beforChapterUrl = beforChapterUrl;
    }
    public boolean getIsComplete() {
        return this.isComplete;
    }
    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }
}
