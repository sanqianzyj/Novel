package com.hao.novel.spider.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class NovelType {
    @Id(autoincrement = true)
    Long id;
    String type;//小说类型
    String from;//0 笔趣阁
    String url;//该类型的小说地址

    @Generated(hash = 342864937)
    public NovelType(Long id, String type, String from, String url) {
        this.id = id;
        this.type = type;
        this.from = from;
        this.url = url;
    }

    @Generated(hash = 1553007784)
    public NovelType() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
