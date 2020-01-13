package com.hao.novel.spider;


import android.util.Log;

import com.hao.novel.db.manage.DbManage;
import com.hao.novel.spider.data.NovelChapter;
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
     * 获取当前站所有小说信息
     */
    public static void getAllNovel() {
        String html = SpiderUtils.getHtml(BiQuMainUrl + "/xiaoshuodaquan/");
        Document doc = Jsoup.parse(html);
        Elements rows = doc.select("div[class=novellist]");
        List<NovelIntroduction> novelIntroductions = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            Elements list = rows.select("li");
            String novelType = rows.select("h2").text();
            for (int j = 0; j < list.size(); j++) {
                Elements elements = list.get(j).select("a");
                NovelIntroduction novelIntroduction = new NovelIntroduction();
                novelIntroduction.setNovelName(elements.text());
                novelIntroduction.setNovelChapterListUrl(elements.attr("href"));
                novelIntroduction.setIsComplete(false);
                novelIntroduction.setNovelType(novelType);
                novelIntroductions.add(novelIntroduction);
                Log.i("小说", "名称=" + novelIntroduction.getNovelName() + "   作者：" + novelIntroduction.getNovelAutho() + "     主页地址：" + novelIntroduction.getNovelChapterListUrl());
            }
        }
        DbManage.addNovelIntrodution(novelIntroductions);
    }


    public static void getAllNovelDetailInfo(NovelIntroduction novelIntroduction) {
        String htmlNovelChapterList = SpiderUtils.getHtml(novelIntroduction.getNovelChapterListUrl());
        getAllNovelDetailInfo(novelIntroduction, htmlNovelChapterList);
//        getNovelAllChapterTitle(novelIntroduction);
    }

    //通过小说详情
    public static void getAllNovelDetailInfo(NovelIntroduction novelIntroduction, String html) {
        String htmlNovelChapterList = html;
        Document htmlNovelChapterListDoc = Jsoup.parse(htmlNovelChapterList);
        novelIntroduction.setNovelAutho(htmlNovelChapterListDoc.select("div#info").select("p").get(0).text().replace("作", "").replace("者", "").replace("：", "").replace(" ", ""));
        novelIntroduction.setNovelCover(htmlNovelChapterListDoc.select("div#sidebar").select("div#fmimg").select("img").attr("src"));
        novelIntroduction.setNovelNewChapterUrl(htmlNovelChapterListDoc.select("div#info").select("p").get(3).select("a").attr("href"));
        novelIntroduction.setNovelNewChapterTitle(htmlNovelChapterListDoc.select("div#info").select("p").get(3).select("a").text());
        novelIntroduction.setNovelIntroduce(htmlNovelChapterListDoc.select("div#intro").select("p").get(1).text());
        novelIntroduction.setIsComplete(true);
        Log.i("小说",  novelIntroduction.toString());
        DbManage.addNovelIntrodution(novelIntroduction);
        getNovelAllChapterTitle(novelIntroduction,htmlNovelChapterListDoc);
    }


    //
    public static void getNovelAllChapterTitle(NovelIntroduction novelIntroduction,Document html) {
        Elements frist = html.select("div[class=box_con]");
        List<NovelChapter> chapters = new ArrayList<>();
        Elements selectChapter = frist.select("div#list").select("dd");
        for (int i = 0; i < selectChapter.size(); i++) {
            String chapterUrl = selectChapter.get(i).select("a").attr("href");
            String chapterName = selectChapter.get(i).select("a").text();
            chapters.add(new NovelChapter(Long.parseLong(i + ""), novelIntroduction.getId(), chapterName, CheckedUrl(chapterUrl), "", "", "", false));
            Log.i("下载章节", "chapterName=" + chapterName);
        }
        DbManage.addNovelChapter(chapters);
    }

    public static void getNovelContent(NovelChapter novelChapter) {
        String htmlNovelChapterList = SpiderUtils.getHtml(novelChapter.getChapterUrl());
        Document htmlNovelChapterListDoc = Jsoup.parse(htmlNovelChapterList);
        Elements frist = htmlNovelChapterListDoc.select("div[class=box_con]");
        Elements second = frist.select("div[class=bookname]");
        novelChapter.setChapterName(second.select("h1").text());
        Elements thrid = second.select("div[class=bottem1]").select("a");
        novelChapter.setBeforChapterUrl(CheckedUrl(thrid.get(1).attr("href")));
        novelChapter.setNextChapterUrl(CheckedUrl(thrid.select("a").get(3).attr("href")));
        novelChapter.setChapterContent(frist.select("div#content").html().replace(" ", "").replace("\n", "").replace("<br>&nbsp;&nbsp;&nbsp;&nbsp;", "\n  ").replace("<br>", "").replace("&nbsp;", "").split("<p>")[0]);
        DbManage.updateNovelChapter(novelChapter);
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
        for (int i = 2; i < type.size() - 2; i++) {
            NovelType novelType = new NovelType();
            novelType.setFrom("0");
            novelType.setType(type.get(i).select("a").text());
            novelType.setUrl(type.get(i).select("a").attr("href"));
            Log.i("小说分类", "类别：" + novelType.getType() + "   地址：" + novelType.getUrl());
            novelTypes.add(novelType);
        }
        DbManage.addNovelType(novelTypes);
    }


    //查询未完善的章节 进行数据完善
    public static void getAllNovelContent(NovelIntroduction novelIntroduction) {
        NovelChapter novelChapter = DbManage.checkNovelAllNoChapterContent(novelIntroduction);
        if (novelChapter != null) {
            getNovelContent(novelChapter);
        }
    }


    public static void getTypeNovelList(String url, String tag) {
        String html = SpiderUtils.getHtml(BiQuMainUrl + url);
        Document doc = Jsoup.parse(html);
        Elements rows = doc.select("div#newscontent");
        Elements list = rows.select("div[class=l]");
        Elements node = list.select("li");
        List<NovelIntroduction> novelIntroductions = new ArrayList<>();
        for (Element e : node) {
            String name = e.select("span[class=s2]").select("a").text();
            String novelUrl = e.select("span[class=s2]").select("a").attr("href");
            String newChaptername = e.select("span[class=s3]").text();
            String newChapterUrl = e.select("span[class=s3]").attr("href");
            String author = e.select("span[class=s5]").text();
            NovelIntroduction novelIntroduction = DbManage.checkNovelIntrodutionByUrl(novelUrl);
            if (novelIntroduction == null) {
                novelIntroduction = new NovelIntroduction();
                novelIntroduction.setNovelName(name);
                novelIntroduction.setNovelType(tag);
            }
            novelIntroduction.setNovelNewChapterTitle(newChaptername);
            novelIntroduction.setNovelNewChapterUrl(newChapterUrl);
            novelIntroduction.setNovelAutho(author);
            novelIntroduction.setNovelChapterListUrl(novelUrl);
            novelIntroductions.add(novelIntroduction);
            Log.i("小说", "名称=" + novelIntroduction.getNovelName() + "   作者：" + novelIntroduction.getNovelAutho() + "     主页地址：" + novelIntroduction.getNovelChapterListUrl() + "  " + novelIntroduction.getNovelType());
        }
        DbManage.addNovelIntrodution(novelIntroductions);
    }
}
