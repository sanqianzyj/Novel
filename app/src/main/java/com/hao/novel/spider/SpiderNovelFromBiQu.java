package com.hao.novel.spider;


import android.util.Log;

import com.hao.novel.db.manage.DbManage;
import com.hao.novel.spider.SpiderUtils;
import com.hao.novel.spider.data.NovelIntroduction;
import com.hao.novel.spider.data.NovelType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class SpiderNovelFromBiQu {
    public static final String BiQuMainUrl = "http://www.xbiquge.la";

    /**
     * 获取所有小说信息
     */
    public static void getAllNovel() {
        String html = SpiderUtils.getHtml(BiQuMainUrl + "/xiaoshuodaquan/");
        Document doc = Jsoup.parse(html);
        Elements rows = doc.select("div[class=novellist]");
        Elements list = rows.select("li");
        List<NovelIntroduction> novelIntroductions = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Elements elements = list.get(i).select("a");
            NovelIntroduction novelIntroduction = new NovelIntroduction();
            novelIntroduction.setNovelName(elements.text());
            novelIntroduction.setNovelChapterListUrl(elements.attr("href"));
            novelIntroductions.add(novelIntroduction);
            Log.i("小说", "名称=" + novelIntroduction.getNovelName() + "     主页地址：" + novelIntroduction.getNovelChapterListUrl());
        }
        DbManage.addNovelIntrodution(novelIntroductions);
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


    //用于URL的检测
    private static String CheckedUrl(String string) {
        if (string.startsWith("http")) {
            return string;
        } else {
            return BiQuMainUrl + string;
        }
    }


    public static void getNovelType() {
        String html = SpiderUtils.getHtml(BiQuMainUrl);
        Document doc = Jsoup.parse(html);
        Elements rows = doc.select("div[class=nav]");
        Elements type = rows.select("li");
        List<NovelType> novelTypes = new ArrayList<>();
        for (int i = 2; i < type.size() - 1; i++) {
            NovelType novelType = new NovelType();
            novelType.setFrom("0");
            novelType.setType(type.get(i).select("a").text());
            novelType.setUrl(type.get(i).select("a").attr("href"));
            Log.i("小说分类", "类别：" + novelType.getType() + "   地址：" + novelType.getUrl());
            novelTypes.add(novelType);
        }
        DbManage.addNovelType(novelTypes);
    }


}
