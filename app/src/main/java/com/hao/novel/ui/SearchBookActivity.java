package com.hao.novel.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.hao.lib.Util.StatusBarUtil;
import com.hao.lib.Util.SystemUtils;
import com.hao.lib.Util.ToastUtils;
import com.hao.lib.view.RecycleViewHelp.RecycleViewDivider;
import com.hao.novel.R;
import com.hao.novel.base.App;
import com.hao.novel.db.manage.DbManage;
import com.hao.novel.service.DownListener;
import com.hao.novel.service.DownLoadNovelService;
import com.hao.novel.service.NovolDownTask;
import com.hao.novel.spider.data.NovelType;
import com.hao.novel.ui.adapter.MuneAdapter;
import com.hao.novel.ui.adapter.NovelListAdapter;

import java.util.List;

public class SearchBookActivity extends AppCompatActivity implements View.OnClickListener, TabLayout.BaseOnTabSelectedListener {
    long backPressTime;
    RecyclerView novel_list;
    TabLayout novel_type;
    List<NovelType> novelTypeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarUtil.setTranslucent(this);
        super.onCreate(savedInstanceState);
        App.getInstance().addActivity(this);
        setContentView(R.layout.activity_main);

        finidView();
        initView();


    }


    private void finidView() {
        novel_list = findViewById(R.id.novel_list);
        novel_type = findViewById(R.id.novel_type);
    }

    private void initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.gray_11));
        novel_list.setLayoutManager(new LinearLayoutManager(this));
        RecycleViewDivider recycleViewDivider = new RecycleViewDivider(this, LinearLayoutManager.VERTICAL, SystemUtils.INSTANCE.dip2px(this, 10), R.color.transparent);
        novel_list.addItemDecoration(recycleViewDivider);
        novel_list.setAdapter(new NovelListAdapter(this, true));

        List<NovelType> novelTypeList = DbManage.getNovelType();
        for (int i = 0; i <novelTypeList.size() ; i++) {
            TabLayout.Tab tab=novel_type.newTab();
            tab.setTag(i);
            tab.setText(novelTypeList.get(i).getType());
            novel_type.addTab(tab);
        }
        novel_type.addOnTabSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - backPressTime < 1500) {
            App.getInstance().finishAll();
        } else {
            backPressTime = System.currentTimeMillis();
            ToastUtils.INSTANCE.showMessage("再次点击退出");
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
