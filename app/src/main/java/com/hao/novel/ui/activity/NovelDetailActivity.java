package com.hao.novel.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hao.lib.Util.RecycleViewDivider;
import com.hao.lib.Util.SystemUtil;
import com.hao.lib.Util.ToastUtils;
import com.hao.novel.R;
import com.hao.novel.base.App;
import com.hao.novel.base.BaseActivity;
import com.hao.novel.db.manage.DbManage;
import com.hao.novel.service.DownListener;
import com.hao.novel.service.DownLoadNovelService;
import com.hao.novel.service.NovolDownTask;
import com.hao.novel.spider.data.NovelChapter;
import com.hao.novel.spider.data.NovelIntroduction;
import com.hao.novel.ui.used.ReadInfo;

import java.util.List;

public class NovelDetailActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView novel_detail;
    LinearLayout favor_detail_state;
    TextView start_read;
    View novel_detail_view;
    String initDate;//当前页小说id的章节目录url
    ReadInfo readInfo;//当前小说的阅读记录

    TextView novel_title;
    TextView novel_author;
    TextView novel_type;
    TextView novel_lenth;
    TextView novel_introduce;
    TextView novel_now_read;
    TextView novel_last_chaper;
    ImageView novel_detail_image;
    LinearLayout novel_now_read_ll;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_detail);
        initDate = getIntent().getStringExtra("novelId");
        readInfo = DbManage.checkedReadInfo(initDate);
        findView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (initDate != null && !"".equals(initDate)) {
            initDate();
        } else {
            ToastUtils.INSTANCE.showMessage("暂未获取到小说数据");
            finish();
        }
    }

    private void findView() {
        favor_detail_state = findViewById(R.id.favor_detail_state);
        novel_detail_view = findViewById(R.id.novel_detail_view);
        start_read = findViewById(R.id.start_read);
        novel_title = findViewById(R.id.novel_title);
        novel_author = findViewById(R.id.novel_author);
        novel_type = findViewById(R.id.novel_type);
        novel_lenth = findViewById(R.id.novel_lenth);
        novel_introduce = findViewById(R.id.novel_introduce);
        novel_now_read = findViewById(R.id.novel_now_read);
        novel_last_chaper = findViewById(R.id.novel_last_chaper);
        novel_detail_image = findViewById(R.id.novel_detail_image);
        novel_now_read_ll = findViewById(R.id.novel_now_read_ll);

        if (readInfo != null) {
            start_read.setText("继续阅读");
        } else {
            start_read.setText("开始阅读");
        }
        start_read.setOnClickListener(this);
    }

    private void initDate(NovelIntroduction novelIntroduction) {
        if (novelIntroduction != null) {
            ReadInfo readInfo = DbManage.checkedReadInfo(novelIntroduction.getNovelChapterListUrl());
            if (readInfo != null) {
                NovelChapter novelChapter = DbManage.checkNovelChaptterByUrl(readInfo.getNovelChapterUrl());
                novel_now_read_ll.setVisibility(View.VISIBLE);
                novel_now_read.setText(novelChapter.getChapterName());
                novel_now_read.setOnClickListener(this);
            }
            novel_title.setText(novelIntroduction.getNovelName());
            novel_author.setText(novelIntroduction.getNovelAutho());
            novel_type.setText(novelIntroduction.getNovelType());
            novel_introduce.setText("\t\t" + novelIntroduction.getNovelIntroduce());
            novel_last_chaper.setText(novelIntroduction.getNovelNewChapterTitle());
            Glide.with(App.getInstance()).load(novelIntroduction.getNovelCover()).error(R.mipmap.novel_normal_cover).into(novel_detail_image);
        }
    }


    private void initDate() {
        NovelIntroduction novelIntroduction = DbManage.checkNovelByUrl(initDate);
        List<NovelChapter> novelChapters = DbManage.getChapterById(initDate);
        if (novelIntroduction.isComplete() && novelIntroduction != null && novelChapters != null && novelChapters.size() > 0) {
            initDate(novelIntroduction);
        } else {
            App.getInstance().getBinder().sendCmd(new NovolDownTask(DownLoadNovelService.NovelDownTag.novelDetail, novelIntroduction, new DownListener() {
                @Override
                public void downInfo(long all, long now) {

                }

                @Override
                public void startDown() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showLoading();
                        }
                    });
                }

                @Override
                public void endDown() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismisLoading();
                            initDate();
                        }
                    });
                }
            }));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.novel_now_read:
                break;
            case R.id.start_read:
                Intent intent = new Intent(NovelDetailActivity.this, ReadNovelActivity.class);
                if (((TextView) v).getText().toString().equals("继续阅读")) {
                    NovelChapter novelChapter = DbManage.checkNovelChaptterById(readInfo.getNovelChapterListUrl(), readInfo.getNovelChapterUrl());
                    intent.putExtra("novelChapter", novelChapter);
                    startActivity(intent);
                } else if (((TextView) v).getText().toString().equals("开始阅读")) {
                    List<NovelChapter> novelChapters = DbManage.getChapterById(initDate);
                    if (novelChapters.size() > 0) {
                        intent.putExtra("novelChapter", novelChapters.get(0));
                        startActivity(intent);
                    } else {
                        initDate();
                    }
                }
                break;
        }
    }
}