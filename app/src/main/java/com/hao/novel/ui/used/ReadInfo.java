package com.hao.novel.ui.used;

import com.hao.lib.Util.MMKVManager;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ReadInfo {
    @Unique
    long Nid;//上一次阅读的小说id
    long Cid;//上一次阅读的小说章节
    int page;//上一次阅读的小说当前章节的页数
    long date;//阅读时间


    @Generated(hash = 516535754)
    public ReadInfo(long Nid, long Cid, int page, long date) {
        this.Nid = Nid;
        this.Cid = Cid;
        this.page = page;
        this.date = date;
    }

    @Generated(hash = 687160188)
    public ReadInfo() {
    }


    public long getNid() {
        return Nid = (long) MMKVManager.getInstance().get("Nid", -1);
    }

    public void setNid(long nid) {
        MMKVManager.getInstance().put("Nid", nid);
    }

    public long getCid() {
        return Cid = (long) MMKVManager.getInstance().get("Cid", -1);
    }

    public void setCid(long cid) {
        Cid = cid;
        MMKVManager.getInstance().put("Cid", cid);
    }

    public int getPage() {
        return page = (int) MMKVManager.getInstance().get("page", -1);
    }

    public void setPage(int page) {
        MMKVManager.getInstance().put("page", page);
        this.page = page;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
