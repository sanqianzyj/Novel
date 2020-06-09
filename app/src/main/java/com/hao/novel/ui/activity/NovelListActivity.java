package com.hao.novel.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
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

public class NovelListActivity extends BaseActivity implements View.OnClickListener, TabLayout.BaseOnTabSelectedListener, AdapterView.OnItemClickListener {
    RecyclerView novel_list;
    TabLayout novel_type;

    /**
     * 当前显示的小说列表页数 30本为一页 同步加载网页
     */
    //TODO 通过加载的网页进行控制
    int page = 0;

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
        if (!App.getInstance().checkedDoubleClick()) {
            return;
        }
    }

    @Override
    public void onTabSelected(final TabLayout.Tab tab) {
        List<NovelIntroduction> novelIntroductions = DbManage.getNovelByType(tab.getText().toString(), page);
        if (novelIntroductions != null && novelIntroductions.size() > 0 && page >= 0) {
            ((NovelListAdapter) novel_list.getAdapter()).setDate(novelIntroductions);
        } else {
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
                            onTabSelected(tab);
                        }
                    });
                }
            }));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        onTabSelected(novel_type.getTabAt(novel_type.getSelectedTabPosition()));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!App.getInstance().checkedDoubleClick()) {
            return;
        }
        NovelIntroduction novelIntroduction = ((NovelListAdapter) novel_list.getAdapter()).getItem(i);

        Intent intent = new Intent(NovelListActivity.this, NovelDetailActivity.class);
        intent.putExtra("novelId", novelIntroduction.getNovelChapterListUrl());
        View novel_avatar = view.findViewById(R.id.novel_avatar);
        ViewCompat.setTransitionName(novel_avatar, "cover");
        Pair<View, String> pair1 = new Pair<>(novel_avatar, ViewCompat.getTransitionName(novel_avatar));
        /**
         *4、生成带有共享元素的Bundle，这样系统才会知道这几个元素需要做动画
         */
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair1);
        ActivityCompat.startActivity(this, intent, activityOptionsCompat.toBundle());

    }
}
