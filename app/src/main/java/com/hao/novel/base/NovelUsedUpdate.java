package com.hao.novel.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class NovelUsedUpdate {
    @Unique
    long id = 1;
    long lastUpdatime;
    @Generated(hash = 414267955)
    public NovelUsedUpdate(long id, long lastUpdatime) {
        this.id = id;
        this.lastUpdatime = lastUpdatime;
    }
    @Generated(hash = 595802861)
    public NovelUsedUpdate() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getLastUpdatime() {
        return this.lastUpdatime;
    }
    public void setLastUpdatime(long lastUpdatime) {
        this.lastUpdatime = lastUpdatime;
    }
}
