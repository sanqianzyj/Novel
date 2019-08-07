package com.hao.novel.spider;


import android.util.Log;

import com.hao.novel.db.manage.DbManage;
import com.hao.novel.spider.data.NovelIntroduction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class SpiderNovelFromBiQu {
    public static final String BiQuMainUrl = "http://www.xbiquge.la";

    public static void getAllNovel() {
        Log.i("获取小说信息", "开始");
        String html = SpiderUtils.getHtml(BiQuMainUrl + "/xiaoshuodaquan/");
        Document doc = Jsoup.parse(html);
        Elements rows = doc.select("div[class=novellist]");
        Elements list = rows.select("li");
        List<NovelIntroduction> novelIntroductions = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            NovelIntroduction novelIntroduction = new NovelIntroduction();
            novelIntroduction.setNovelNameAndAuthot(list.text());
            novelIntroduction.setNovelChapterListUrl(list.attr("href"));
            novelIntroductions.add(novelIntroduction);
        }
        DbManage.addNovelIntrodution(novelIntroductions);
        Log.i("获取小说信息", "结束");
    }


    //从笔趣阁中获取到到小说的列表数据
    public static void getNovelList(String html) {
        Document doc = Jsoup.parse(html);
        Elements rows = doc.select("div#newscontent");
        Elements list = rows.select("div[class=l]");
        Elements node = list.select("li");
        for (Element e : node) {
            Elements name = e.select("span[class=s2]").select("a");
            Elements newChapter = e.select("span[class=s3]");
            Elements author = e.select("span[class=s5]");

        }
    }


    /**
     * 获取单个小说的当前信息
     *
     * @param html 章节对应的页面
     * @return 小说章节列表
     */
    public static void getNovelDetail(String html) {
        Document doc = Jsoup.parse(html);
        Elements frist = doc.select("div[class=box_con]");
        String url = frist.select("div#fmimg").select("img").attr("src");
        String title = frist.select("div#maininfo").select("div#info").select("h1").text();
        Elements someInfo = frist.select("div#maininfo").select("div#info").select("p");
        String auther = someInfo.get(0).text();
        String type = someInfo.get(1).text();
        Elements lastChapter = someInfo.get(3).select("a");

    }


    //用于URL的检测
    private static String CheckedUrl(String string) {
        if (string.startsWith("http")) {
            return string;
        } else {
            return BiQuMainUrl + string;
        }
    }


}
