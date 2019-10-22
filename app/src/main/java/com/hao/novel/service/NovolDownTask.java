package com.hao.novel.service;

public class NovolDownTask {

    DownLoadNovelService.NovelDownTag novelDownTag;
    Object object;
    DownListener downListener;

    public NovolDownTask(DownLoadNovelService.NovelDownTag novelDownTag) {
        this.novelDownTag = novelDownTag;
    }

    public NovolDownTask(DownLoadNovelService.NovelDownTag novelDownTag, Object object) {
        this.novelDownTag = novelDownTag;
        this.object = object;
    }

    public NovolDownTask(DownLoadNovelService.NovelDownTag novelDownTag, Object object, DownListener downListener) {
        this.novelDownTag = novelDownTag;
        this.object = object;
        this.downListener = downListener;
    }

    public NovolDownTask(DownLoadNovelService.NovelDownTag novelDownTag, DownListener downListener) {
        this.novelDownTag = novelDownTag;
        this.downListener = downListener;
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
