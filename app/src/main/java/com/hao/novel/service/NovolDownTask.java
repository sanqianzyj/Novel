package com.hao.novel.service;

public class NovolDownTask {

    DownLoadNovelService.NovelDownTag novelDownTag;
    Object object;
    DownListener downListener;
    boolean isneedListener;

    public NovolDownTask(DownLoadNovelService.NovelDownTag novelDownTag) {
        this.novelDownTag = novelDownTag;
        this.isneedListener=false;
    }

    public NovolDownTask(DownLoadNovelService.NovelDownTag novelDownTag, Object object) {
        this.novelDownTag = novelDownTag;
        this.object = object;
        this.isneedListener=false;
    }

    public NovolDownTask(DownLoadNovelService.NovelDownTag novelDownTag, Object object, DownListener downListener) {
        this.novelDownTag = novelDownTag;
        this.object = object;
        this.downListener = downListener;
        this.isneedListener=true;
    }

    public NovolDownTask(DownLoadNovelService.NovelDownTag novelDownTag, Object object, boolean isneed) {
        this.novelDownTag = novelDownTag;
        this.object = object;
        this.downListener = downListener;
        this.isneedListener=isneed;
    }

    public NovolDownTask(DownLoadNovelService.NovelDownTag novelDownTag, DownListener downListener) {
        this.novelDownTag = novelDownTag;
        this.downListener = downListener;
        this.isneedListener=true;
    }

    public DownLoadNovelService.NovelDownTag getNovelDownTag() {
        return novelDownTag;
    }


    public DownListener getDownListener() {
        return downListener;
    }

    public Object getObject() {
        return object;
    }


}
