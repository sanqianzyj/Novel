package com.hao.novel.service;

import android.os.Binder;

public abstract class DownLoadNovelBinder extends Binder {
    public abstract void sendCmd(DownLoadNovelService.NovelDownTag o);

    public abstract void sendCmd(DownLoadNovelService.NovelDownTag o, int index);

    public abstract Object getMassage();

    public abstract void setDownListener(DownListener downListener);


}
