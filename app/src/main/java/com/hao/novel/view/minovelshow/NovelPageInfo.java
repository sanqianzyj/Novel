package com.hao.novel.view.minovelshow;

import java.util.ArrayList;
import java.util.List;

public class NovelPageInfo {
    int page;//当前页所属章节页数
    List<String> pagecontent;//当前页所属的章节内容
    long novelCId;//当前章节小说所属id 用于获取下一章或上一章内容
    long novelNId;//当前小说所属id 用于获取下一章或上一章内容


    public void addContent(String s) {
        if (pagecontent == null) {
            pagecontent = new ArrayList<>();
        }
        pagecontent.add(s);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<String> getPagecontent() {
        return pagecontent;
    }

    public void setPagecontent(List<String> pagecontent) {
        this.pagecontent = pagecontent;
    }

    public long getNovelCId() {
        return novelCId;
    }

    public void setNovelCId(long novelCId) {
        this.novelCId = novelCId;
    }

    public long getNovelNId() {
        return novelNId;
    }

    public void setNovelNId(long novelNId) {
        this.novelNId = novelNId;
    }
}
