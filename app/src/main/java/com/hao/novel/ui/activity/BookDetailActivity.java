package com.hao.novel.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.hao.novel.ui.adapter.NovelChapterItemAdapter;
import com.hao.novel.ui.used.ReadInfo;

import java.util.List;

public class BookDetailActivity extends BaseActivity {

    RecyclerView novel_detail;
    LinearLayout favor_detail_state;
    TextView start_read;
    View novel_detail_view;

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
        favor_detail_state = findViewById(R.id.favor_detail_state);
        novel_detail_view = findViewById(R.id.novel_detail_view);
        start_read = findViewById(R.id.start_read);

        novel_detail = findViewById(R.id.novel_detail);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        novel_detail.setLayoutManager(linearLayoutManager);
        RecycleViewDivider recycleViewDivider = new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, (int) SystemUtil.dp2px(this, 1f), ContextCompat.getColor(App.getInstance(), R.color.line));
        novel_detail.addItemDecoration(recycleViewDivider);
        NovelChapterItemAdapter novelChapterItemAdapter = new NovelChapterItemAdapter(this);
        novelChapterItemAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BookDetailActivity.this, ReadBookActivity.class);
                intent.putExtra("novelChapter", ((NovelChapterItemAdapter) novel_detail.getAdapter()).getItem(position));
                Log.i("流程", "跳转内容页");
                startActivity(intent);
            }
        });
        novel_detail.setAdapter(novelChapterItemAdapter);

    }

    private void initDate(final long id) {
        NovelIntroduction novelIntroduction = DbManage.checkNovelInfoById(id);
        List<NovelChapter> novelChapters = DbManage.getChapterById(id);
        if (novelIntroduction.isComplete() && novelIntroduction != null && novelChapters != null && novelChapters.size() > 0) {
            ((NovelChapterItemAdapter) novel_detail.getAdapter()).setNovelChapterList(novelChapters);
            ((NovelChapterItemAdapter) novel_detail.getAdapter()).setHead(novelIntroduction);
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
                            initDate(id);
                        }
                    });
                }
            }));
        }
    }
}