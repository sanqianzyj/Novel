package com.hao.novel.service;

public interface DownListener {
    void downInfo(long all,long now);

    void startDown();

    void endDown();
}
