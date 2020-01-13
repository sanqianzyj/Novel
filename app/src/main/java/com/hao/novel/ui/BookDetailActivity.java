package com.hao.novel.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.hao.lib.Util.ToastUtils;
import com.hao.lib.view.MiNoScrollListView;
import com.hao.novel.R;
import com.hao.novel.base.App;
import com.hao.novel.base.BaseActivity;
import com.hao.novel.db.manage.DbManage;
import com.hao.novel.spider.data.NovelChapter;
import com.hao.novel.spider.data.NovelIntroduction;
import com.hao.novel.ui.adapter.NovelChapterItemAdapter;

import java.util.List;

public class BookDetailActivity extends BaseActivity {

    private TextView novel_title;
    private TextView novel_author;
    private TextView novel_type;
    private TextView novel_lenth;
    private TextView novel_introduce;
    private TextView novel_now_read;
    private TextView novel_last_chaper;
    private MiNoScrollListView novel_chaper_list;
    private ImageView novel_image;
    private LinearLayout favor_state;
    private LinearLayout novel_now_read_ll;
    private TextView start_read;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_detail);
        findView();
        long initDate = getIntent().getLongExtra("novelId", -1);
        if (initDate != -1) {
            initDate(initDate);
        } else {
            ToastUtils.INSTANCE.showMessage("暂未获取到小说数据");
            finish();
        }
    }

    private void findView() {
        novel_title = findViewById(R.id.novel_title);
        novel_author = findViewById(R.id.novel_author);
        novel_type = findViewById(R.id.novel_type);
        novel_lenth = findViewById(R.id.novel_lenth);
        novel_introduce = findViewById(R.id.novel_introduce);
        novel_now_read = findViewById(R.id.novel_now_read);
        novel_last_chaper = findViewById(R.id.novel_last_chaper);
        novel_chaper_list = findViewById(R.id.novel_chaper_list);
        novel_image = findViewById(R.id.novel_detail_image);
        favor_state = findViewById(R.id.favor_detail_state);
        start_read = findViewById(R.id.start_read);
        novel_now_read_ll = findViewById(R.id.novel_now_read_ll);

        novel_chaper_list.setAdapter(new NovelChapterItemAdapter(this));
    }

    private void initDate(long id) {
        NovelIntroduction novelIntroduction = DbManage.checkNovelInfoById(id);
        List<NovelChapter> novelChapters = DbManage.getChapterById(id);
        if (novelIntroduction != null && novelChapters != null) {
            novel_title.setText(novelIntroduction.getNovelName());
            novel_author.setText(novelIntroduction.getNovelAutho());
            novel_type.setText(novelIntroduction.getNovelType());
            novel_introduce.setText("\t\t" + novelIntroduction.getNovelIntroduce());
            novel_last_chaper.setText(novelIntroduction.getNovelNewChapterTitle());
            Glide.with(App.getInstance()).load(novelIntroduction.getNovelCover()).error(R.mipmap.novel_normal_cover).into(novel_image);
            ((NovelChapterItemAdapter) novel_chaper_list.getAdapter()).setNovelChapterList(novelChapters);
            if (TextUtils.isEmpty(novelIntroduction.getNowReadID()) || TextUtils.isEmpty(novelIntroduction.getNowRead())) {
                novel_now_read_ll.setVisibility(View.GONE);
            } else {
                novel_now_read.setText(novelIntroduction.getNowRead());
            }
        }
    }
}
