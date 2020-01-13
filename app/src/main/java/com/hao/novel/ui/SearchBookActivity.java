package com.hao.novel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.hao.lib.Util.StatusBarUtil;
import com.hao.lib.Util.SystemUtils;
import com.hao.lib.view.RecycleViewHelp.RecycleViewDivider;
import com.hao.novel.R;
import com.hao.novel.base.App;
import com.hao.novel.base.BaseActivity;
import com.hao.novel.db.manage.DbManage;
import com.hao.novel.service.DownListener;
import com.hao.novel.service.DownLoadNovelService;
import com.hao.novel.service.NovolDownTask;
import com.hao.novel.spider.data.NovelIntroduction;
import com.hao.novel.spider.data.NovelType;
import com.hao.novel.ui.adapter.NovelListAdapter;

import java.util.List;

public class SearchBookActivity extends BaseActivity implements View.OnClickListener, TabLayout.BaseOnTabSelectedListener, AdapterView.OnItemClickListener {
    RecyclerView novel_list;
    TabLayout novel_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        finidView();
        initView();
    }

    private void finidView() {
        novel_list = findViewById(R.id.novel_list);
        novel_type = findViewById(R.id.novel_type);
        addViewForNotHideSoftInput(findViewById(R.id.search_book));
    }

    private void initView() {
        novel_list.setLayoutManager(new LinearLayoutManager(this));
        RecycleViewDivider recycleViewDivider = new RecycleViewDivider(this, LinearLayoutManager.VERTICAL, SystemUtils.INSTANCE.dip2px(this, 10), R.color.transparent);
        novel_list.addItemDecoration(recycleViewDivider);
        NovelListAdapter novelListAdapter = new NovelListAdapter(this, true);
        novelListAdapter.setItemOnClickListener(this);
        novel_list.setAdapter(novelListAdapter);

        novel_type.addOnTabSelectedListener(this);
        List<NovelType> novelTypeList = DbManage.getNovelType();
        for (int i = 0; i < novelTypeList.size(); i++) {
            TabLayout.Tab tab = novel_type.newTab();
            tab.setTag(novelTypeList.get(i));
            tab.setText(novelTypeList.get(i).getType());
            novel_type.addTab(tab);
        }

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onTabSelected(final TabLayout.Tab tab) {
        ((NovelListAdapter) novel_list.getAdapter()).setType(tab.getText().toString());
        App.getInstance().getBinder().sendCmd(new NovolDownTask(DownLoadNovelService.NovelDownTag.noveltypelist, tab.getTag(), new DownListener() {
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
                        ((NovelListAdapter) novel_list.getAdapter()).notifyData();
                    }
                });
            }
        }));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final NovelIntroduction novelIntroduction = ((NovelListAdapter) novel_list.getAdapter()).getItem(i);
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
                        Intent intent = new Intent(SearchBookActivity.this, BookDetailActivity.class);
                        intent.putExtra("novelId",novelIntroduction.getId());
                        startActivity(intent);
                    }
                });
            }
        }));


    }
}
