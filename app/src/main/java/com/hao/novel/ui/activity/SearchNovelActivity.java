package com.hao.novel.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.hao.lib.Util.PopUtils;
import com.hao.novel.R;
import com.hao.novel.base.App;
import com.hao.novel.base.BaseActivity;
import com.hao.novel.db.manage.DbManage;
import com.hao.novel.spider.data.NovelIntroduction;
import com.hao.novel.ui.adapter.TextNovelAdapter;

import java.util.List;

public class SearchNovelActivity extends BaseActivity {

    private RecyclerView novel_list;
    private EditText search_novel_ed;
    List<NovelIntroduction> novelIntroductionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_novel);
        initView();
    }

    private void initView() {
        search_novel_ed = findViewById(R.id.search_novel_ed);
        addViewForNotHideSoftInput(search_novel_ed);
        search_novel_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                novelIntroductionList = DbManage.checkNovelIntrodutionByStr(s.toString());
                ((TextNovelAdapter) (novel_list.getAdapter())).setmNovelPage(novelIntroductionList);
            }
        });


        novel_list = findViewById(R.id.novel_list);
        novel_list.setBackgroundResource(R.color.white);
        novel_list.setLayoutManager(new LinearLayoutManager(App.getInstance()));
        novel_list.addItemDecoration(new DividerItemDecoration(App.getInstance(), DividerItemDecoration.VERTICAL));
        novel_list.setAdapter(new TextNovelAdapter(App.getInstance(), novelIntroductionList)
                .setItemClickLisener(new TextNovelAdapter.OnItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void itemClick(int position, View view, Object object) {
                        //保存或者更新当前小说的信息
                        NovelIntroduction novelListItemContent = ((TextNovelAdapter) novel_list.getAdapter()).getDate().get(position);
                        Intent intent = new Intent(SearchNovelActivity.this, BookDetailActivity.class);
                        intent.putExtra("novelId", novelListItemContent.getId());
                        startActivity(intent);
                    }
                }));
    }


}
