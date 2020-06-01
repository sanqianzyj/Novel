package com.hao.novel.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.PopupWindow;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.hao.lib.Util.PopUtils;
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
import com.hao.novel.ui.adapter.TextNovelAdapter;

import java.util.List;

public class SearchBookActivity extends BaseActivity implements View.OnClickListener, TabLayout.BaseOnTabSelectedListener, AdapterView.OnItemClickListener {
    RecyclerView novel_list;
    TabLayout novel_type;
    EditText search_book;
    RecyclerView recyclerView;
    PopupWindow pop;

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
        search_book = findViewById(R.id.search_book);
        addViewForNotHideSoftInput(search_book);

        search_book.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    if (pop != null && pop.isShowing()) {
                        pop.dismiss();
                    }
                    return;
                }
                if (pop != null) {
                    recyclerView.getAdapter().notifyDataSetChanged();
                    ((TextNovelAdapter) recyclerView.getAdapter()).setList(DbManage.checkNovelIntrodutionByStr(s.toString()));
                    ((RecyclerView) pop.getContentView()).getAdapter().notifyDataSetChanged();
                    if (!pop.isShowing()) {
                        pop.showAsDropDown(search_book);
                    }
                } else {
                    recyclerView = (RecyclerView) LayoutInflater.from(App.getInstance()).inflate(R.layout.recycle_layout, null);
                    recyclerView.setTag("height,200");
                    recyclerView.setBackgroundResource(R.color.white);
                    recyclerView.setLayoutManager(new LinearLayoutManager(App.getInstance()));
                    recyclerView.addItemDecoration(new DividerItemDecoration(App.getInstance(), DividerItemDecoration.VERTICAL));
                    recyclerView.setAdapter(new TextNovelAdapter(App.getInstance(), DbManage.checkNovelIntrodutionByStr(s.toString()))
                            .setItemClickLisener(new TextNovelAdapter.OnItemClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void itemClick(int position, View view, Object object) {
                                    if (pop != null) {
                                        pop.dismiss();
                                    }

                                    //保存或者更新当前小说的信息
                                    NovelIntroduction novelListItemContent = ((TextNovelAdapter) recyclerView.getAdapter()).getDate().get(position);
                                    Intent intent = new Intent(SearchBookActivity.this, BookDetailActivity.class);
                                    intent.putExtra("novelId", novelListItemContent.getId());
                                    startActivity(intent);
                                }
                            }));
                    PopUtils.getInstance().createPop(search_book, recyclerView).showAsViewDown(search_book);
                }
            }
        });
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
        Intent intent = new Intent(SearchBookActivity.this, BookDetailActivity.class);
        intent.putExtra("novelId", novelIntroduction.getId());
        startActivity(intent);
//        App.getInstance().getBinder().sendCmd(new NovolDownTask(DownLoadNovelService.NovelDownTag.novelDetail, novelIntroduction, new DownListener() {
//            @Override
//            public void downInfo(long all, long now) {
//
//            }
//
//            @Override
//            public void startDown() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        showLoading();
//                    }
//                });
//            }
//
//            @Override
//            public void endDown() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        dismisLoading();
//
//                    }
//                });
//            }
//        }));


    }
}
