package com.hao.novel.ui.activity;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hao.lib.Util.SystemUtil;
import com.hao.novel.R;
import com.hao.novel.base.App;
import com.hao.novel.base.BaseActivity;
import com.hao.novel.db.manage.DbManage;
import com.hao.novel.service.DownListener;
import com.hao.novel.service.DownLoadNovelService;
import com.hao.novel.service.NovolDownTask;
import com.hao.novel.spider.data.NovelChapter;
import com.hao.novel.ui.adapter.NovelChapterAdapter;
import com.hao.novel.ui.adapter.TextTypefaceAdapter;
import com.hao.novel.ui.used.ReadInfo;
import com.hao.novel.view.minovelshow.NovelPageInfo;
import com.hao.novel.view.minovelshow.NovelTextView;
import com.hao.novel.view.minovelshow.MiTextViewConfig;
import com.hao.novel.view.minovelshow.PullViewLayout;

import java.util.List;

public class ReadNovelActivity extends BaseActivity implements PullViewLayout.PullViewLayoutListener, View.OnClickListener {
    PullViewLayout pullViewLayout;
    NovelTextView miTextView;
    MiTextViewConfig miTextViewConfig;
    RecyclerView.Adapter adpter;
    LinearLayout text_typeface_layout;
    View readbook_config;
    MuneState muneState = MuneState.none;//当前菜单状态
    MuneState nextShow = MuneState.mune;//初始化下一个需要的界面为菜单
    TextView textsize;//字体大小
    TextView word_spac_size;//文字间距
    TextView line_spac_size;//行间距
    TextView mune_tittle;//菜单名称
    View mune;//顶部菜单

    NovelChapter novelChapter;//当前页下载的小说内容
    ValueAnimator valueAnimator;
    Animation topIn;
    Animation topOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);
        novelChapter = getIntent().getParcelableExtra("novelChapter");
        findView();
    }

    public void initAnimal() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(0, 1f);
        }
        valueAnimator.setDuration(100);
        valueAnimator.removeAllUpdateListeners();
        valueAnimator.removeAllListeners();
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (muneState == MuneState.none) {//表示菜单关闭动画完成
                    if (nextShow == MuneState.typefacelistshow || nextShow == MuneState.selecchapter) {
                        doTypefaceAnimal();
                    } else if (nextShow == MuneState.none) {
                        nextShow = MuneState.mune;
                    } else if (nextShow == MuneState.mune) {
                        doMuneAnimal();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        topIn = AnimationUtils.loadAnimation(this, R.anim.anim_item_top_in);
        topIn.setDuration(100);
        topIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mune.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        topOut = AnimationUtils.loadAnimation(this, R.anim.anim_item_top_out);
        topOut.setDuration(100);
        topOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mune.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void initDate() {
        initDate(false);
    }

    private void initDate(final boolean ishead) {
        miTextViewConfig = MiTextViewConfig.getDefoutConfig(miTextView.getWidth(), miTextView.getHeight());
        textsize.setText((int) SystemUtil.px2sp(App.getInstance(), miTextViewConfig.getTextSize()) + "");
        word_spac_size.setText((int) SystemUtil.px2sp(App.getInstance(), miTextViewConfig.getWordSpacingExtra()) + "");
        line_spac_size.setText((int) SystemUtil.px2sp(App.getInstance(), miTextViewConfig.getLineSpacingExtra()) + "");
        Log.i("流程", "宽：" + miTextViewConfig.getViewWidth() + " 高：" + miTextViewConfig.getViewhigh() + " 行字数：" + miTextViewConfig.getLineTextNum() + " 行数：" + miTextViewConfig.getLineNum());
        if (novelChapter == null) {
            return;
        }
        novelChapter = DbManage.checkNovelChaptterByUrl(novelChapter.getChapterUrl());
        if (novelChapter.getIsComplete()) {
            if (miTextViewConfig != null) {
                List<View> novelPageInfos = miTextViewConfig.initText(novelChapter);
                ReadInfo readInfo = DbManage.checkedReadInfo(novelChapter.getNovelChapterListUrl());
                if (readInfo != null && novelChapter.getChapterUrl().equals(readInfo.getNovelChapterUrl())) {
                    Log.i("阅读", "页数：" + readInfo.getPage());
                    pullViewLayout.setPage(readInfo.getPage(), novelPageInfos);
                } else {
                    if (ishead) {
                        pullViewLayout.addListViewHead(novelPageInfos);
                    } else {
                        pullViewLayout.addListViewEnd(novelPageInfos);
                    }
                }
            }
        } else {
            App.getInstance().getBinder().sendCmd(new NovolDownTask(DownLoadNovelService.NovelDownTag.singlechaptercontent, novelChapter, new DownListener() {
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
                            if (miTextViewConfig != null) {
                                List<View> novelPageInfos = miTextViewConfig.initText(novelChapter);
                                ReadInfo readInfo = DbManage.checkedReadInfo(novelChapter.getNovelChapterListUrl());
                                if (readInfo != null && novelChapter.getChapterUrl().equals(readInfo.getNovelChapterUrl())) {
                                    pullViewLayout.setPage(readInfo.getPage(), novelPageInfos);
                                } else {
                                    if (ishead) {
                                        pullViewLayout.addListViewHead(novelPageInfos);
                                    } else {
                                        pullViewLayout.addListViewEnd(novelPageInfos);
                                    }
                                }
                            }
                        }
                    });
                }
            }));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initConfigView();
    }

    private void findView() {
        Log.i("流程", "初始化view");
        pullViewLayout = findViewById(R.id.novel_show);
        View novel_content_layout = LayoutInflater.from(this).inflate(R.layout.novel_content_layout, null);
        pullViewLayout.addView(novel_content_layout);
        miTextView = novel_content_layout.findViewById(R.id.novel_content);


        pullViewLayout.setBackgroundResource(R.mipmap.bg_readbook_yellow);
        pullViewLayout.setPullViewLayoutListener(this);
        text_typeface_layout = findViewById(R.id.text_typeface_layout);


        textsize = findViewById(R.id.textsize);
        findViewById(R.id.add_textsize).setOnClickListener(this);
        findViewById(R.id.reduce_textsize).setOnClickListener(this);


        word_spac_size = findViewById(R.id.word_spac_size);
        findViewById(R.id.word_spac_add).setOnClickListener(this);
        findViewById(R.id.word_spac_reduce).setOnClickListener(this);


        line_spac_size = findViewById(R.id.line_spac_size);
        findViewById(R.id.line_spac_add).setOnClickListener(this);
        findViewById(R.id.line_spac_reduce).setOnClickListener(this);


        readbook_config = findViewById(R.id.readbook_config);
        readbook_config.findViewById(R.id.none).setOnClickListener(this);
        findViewById(R.id.select_typeface).setOnClickListener(this);

        mune_tittle = findViewById(R.id.mune_title);
        findViewById(R.id.readbook_config).setOnClickListener(this);

        mune = findViewById(R.id.mune);
        findViewById(R.id.selec_chapter).setOnClickListener(this);
    }

    //加载菜单相关的配置信息
    private void initConfigView() {
        Log.i("流程", "加载配置信息");
        RecyclerView text_typeface = findViewById(R.id.text_typeface);
        text_typeface.setLayoutManager(new LinearLayoutManager(App.getInstance()));
        text_typeface.addItemDecoration(new DividerItemDecoration(App.getInstance(), DividerItemDecoration.VERTICAL));
//        RecycleViewDivider recycleViewDivider = new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(this, R.color.line));
//        text_typeface.addItemDecoration(recycleViewDivider);
        if (nextShow == MuneState.typefacelistshow) {
            adpter = new TextTypefaceAdapter(this).setItemClickLisener(new TextTypefaceAdapter.OnItemClickListener() {
                @Override
                public void itemClick(int position, View view, Object object) {
                    miTextView.getMiTextViewConfig().setTypeface(((TextTypefaceAdapter) adpter).getItem(position).getTypeface());
                    pullViewLayout.refreshView();
                }
            });
            text_typeface.setAdapter(adpter);
        } else if (nextShow == MuneState.selecchapter) {
            adpter = new NovelChapterAdapter(this).setItemClickLisener(new NovelChapterAdapter.OnItemClickListener() {
                @Override
                public void itemClick(int position, View view, Object object) {
                    novelChapter = ((NovelChapterAdapter) adpter).getItem(position);
                    pullViewLayout.clear();
                    initDate();
                }
            });
            List<NovelChapter> novelChapters = DbManage.checkedNovelList(novelChapter.getNovelChapterListUrl());
            ((NovelChapterAdapter) adpter).setNovelChapters(novelChapters);
            text_typeface.setAdapter(adpter);
            ((NovelChapterAdapter) adpter).setSelect(novelChapter.getId().intValue());
            text_typeface.scrollToPosition(novelChapter.getId().intValue());
        }

        Log.i("流程", "加载配置信息完成");
    }

    @Override
    public void noDate() {

    }

    @Override
    public void nextPage(View v) {
        if (v instanceof NovelTextView) {
            ReadInfo readInfo = new ReadInfo();
            readInfo.setNovelChapterUrl(((NovelTextView) v).getNovelPageInfo().getNovelChapterUrl());
            readInfo.setNovelChapterListUrl(((NovelTextView) v).getNovelPageInfo().getNoveChapterListUrl());
            readInfo.setPage(((NovelTextView) v).getNovelPageInfo().getPage());
            readInfo.setDate(System.currentTimeMillis());
            DbManage.saveReadInfo(readInfo);
        } else if (v instanceof ViewGroup) {
            for (int j = 0; j < ((ViewGroup) v).getChildCount(); j++) {
                nextPage(((ViewGroup) v).getChildAt(j));
            }
        }
    }

    @Override
    public void lastPage(View v) {
        if (v instanceof NovelTextView) {
            ReadInfo readInfo = new ReadInfo();
            readInfo.setNovelChapterUrl(((NovelTextView) v).getNovelPageInfo().getNovelChapterUrl());
            readInfo.setNovelChapterListUrl(((NovelTextView) v).getNovelPageInfo().getNoveChapterListUrl());
            readInfo.setPage(((NovelTextView) v).getNovelPageInfo().getPage());
            readInfo.setDate(System.currentTimeMillis());
            DbManage.saveReadInfo(readInfo);
        } else if (v instanceof ViewGroup) {
            for (int j = 0; j < ((ViewGroup) v).getChildCount(); j++) {
                lastPage(((ViewGroup) v).getChildAt(j));
            }
        }
    }

    @Override
    public void needLoadDate(boolean isHead) {
        if (isHead) {
            NovelPageInfo novelPageInfo = (pullViewLayout.getContentMiTextPage()).getNovelPageInfo();
            novelChapter = DbManage.getChapterById(novelPageInfo.getNoveChapterListUrl(), novelPageInfo.getNovelChapterUrl());
            novelChapter = DbManage.checkNovelChaptterByUrl(novelChapter.getBeforChapterUrl());
            if (novelChapter != null) {
                initDate(isHead);
            }
        } else {
            NovelPageInfo novelPageInfo = (pullViewLayout.getContentMiTextPage()).getNovelPageInfo();
            novelChapter = DbManage.getChapterById(novelPageInfo.getNoveChapterListUrl(), novelPageInfo.getNovelChapterUrl());
            novelChapter = DbManage.checkNovelChaptterByUrl(novelChapter.getNextChapterUrl());
            if (novelChapter != null) {
                initDate(isHead);
            }
        }
    }

    @Override
    public void noLast() {

    }

    @Override
    public void noNext() {

    }

    @Override
    public void onClickCenter() {
        doMuneAnimal();
    }

    @Override
    public boolean onTouch() {
        return clearView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && novelChapter != null) {
            Log.i("流程", "计算view");
            initDate();
            Log.i("流程", "计算view完成");
        }
    }

    private void doMuneAnimal() {
        initAnimal();
        Log.i("事件", "当前状态：" + nextShow.toString() + "     now:" + muneState);
        if (!valueAnimator.isRunning()) {
            if (nextShow == MuneState.mune) {
                mune.startAnimation(topIn);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float translateY = -(float) animation.getAnimatedValue() * readbook_config.getHeight();
                        readbook_config.setTranslationY(translateY);
                        if ((float) animation.getAnimatedValue() == 1) {
                            muneState = MuneState.mune;
                            nextShow = MuneState.none;
                        }
                    }
                });
                valueAnimator.start();
            } else if (muneState == MuneState.mune) {//已显示字体类型列表
                mune.startAnimation(topOut);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float translatex = ((float) animation.getAnimatedValue() - 1) * readbook_config.getHeight();
                        readbook_config.setTranslationY(translatex);
                        if ((float) animation.getAnimatedValue() == 1) {
                            muneState = MuneState.none;
                        }
                    }
                });
                valueAnimator.start();
            } else if (muneState == MuneState.typefacelistshow || muneState == MuneState.selecchapter) {
                doTypefaceAnimal();
            }
        }
    }

    private void doTypefaceAnimal() {
        initAnimal();
        if (!valueAnimator.isRunning()) {
            if (nextShow == MuneState.typefacelistshow || nextShow == MuneState.selecchapter) {
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float translatex = -(float) animation.getAnimatedValue() * text_typeface_layout.getWidth();
                        Log.i("位置  进", "translatex=" + translatex);
                        text_typeface_layout.setTranslationX(translatex);
                        if ((float) animation.getAnimatedValue() == 1) {
                            muneState = nextShow;
                            nextShow = MuneState.none;
                        }
                    }
                });
                valueAnimator.start();
            } else if (muneState == MuneState.typefacelistshow || muneState == MuneState.selecchapter) {//已显示字体类型列表
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float translatex = ((float) animation.getAnimatedValue() - 1) * text_typeface_layout.getWidth();
                        Log.i("位置  出", "translatex=" + translatex);
                        text_typeface_layout.setTranslationX(translatex);
                        if ((float) animation.getAnimatedValue() == 1) {
                            muneState = MuneState.none;
                            nextShow = MuneState.none;
                        }
                    }
                });
                valueAnimator.start();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (!App.getInstance().checkedDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.add_textsize:
                float textSizeadd = Float.parseFloat(textsize.getText().toString());
                textSizeadd = textSizeadd + 1;
                textsize.setText(textSizeadd + "");
                textSizeadd = SystemUtil.sp2px(App.getInstance(), (float) textSizeadd);
                if (miTextViewConfig != null) {
                    miTextViewConfig.setTextSize(textSizeadd);
                    pullViewLayout.clear();
                    initDate();
                }
                break;
            case R.id.reduce_textsize:
                float textSizeRefuse = Float.parseFloat(textsize.getText().toString());
                if (textSizeRefuse > 13) {
                    textSizeRefuse = textSizeRefuse - 1;
                    textsize.setText(textSizeRefuse + "");
                    textSizeRefuse = SystemUtil.sp2px(App.getInstance(), (float) textSizeRefuse);
                }

                if (miTextViewConfig != null) {
                    miTextViewConfig.setTextSize(textSizeRefuse);
                    pullViewLayout.clear();
                    initDate();
                }
                break;
            case R.id.word_spac_add:
                float wordspcadd = Float.parseFloat(word_spac_size.getText().toString());
                wordspcadd = wordspcadd + 1;
                word_spac_size.setText(wordspcadd + "");
                wordspcadd = SystemUtil.sp2px(App.getInstance(), (float) wordspcadd);
                if (miTextViewConfig != null) {
                    miTextViewConfig.setWordSpacingExtra(wordspcadd);
                    pullViewLayout.clear();
                    initDate();
                }
                break;
            case R.id.word_spac_reduce:
                float wordspcReduce = Float.parseFloat(word_spac_size.getText().toString());
                if (wordspcReduce > 0) {
                    wordspcReduce = wordspcReduce - 1;
                    word_spac_size.setText(wordspcReduce + "");
                    wordspcReduce = SystemUtil.sp2px(App.getInstance(), (float) wordspcReduce);
                }

                if (miTextViewConfig != null) {
                    miTextViewConfig.setWordSpacingExtra(wordspcReduce);
                    pullViewLayout.clear();
                    initDate();
                }
                break;
            case R.id.line_spac_add:
                float linespcadd = Float.parseFloat(line_spac_size.getText().toString());
                linespcadd = linespcadd + 1;
                word_spac_size.setText(linespcadd + "");
                linespcadd = SystemUtil.sp2px(App.getInstance(), (float) linespcadd);
                if (miTextViewConfig != null) {
                    miTextViewConfig.setLineSpacingExtra(linespcadd);
                    pullViewLayout.clear();
                    initDate();
                }
                break;
            case R.id.line_spac_reduce:
                float linespcReduce = Float.parseFloat(line_spac_size.getText().toString());
                if (linespcReduce > 0) {
                    linespcReduce = linespcReduce - 1;
                    word_spac_size.setText(linespcReduce + "");
                    linespcReduce = SystemUtil.sp2px(App.getInstance(), (float) linespcReduce);
                }

                if (miTextViewConfig != null) {
                    miTextViewConfig.setLineSpacingExtra(linespcReduce);
                    pullViewLayout.clear();
                    initDate();
                }
                break;
            case R.id.none:
                doMuneAnimal();
                break;
            case R.id.select_typeface:
                nextShow = MuneState.typefacelistshow;
                initConfigView();
                mune_tittle.setText("选择字体");
                doMuneAnimal();
                break;
            case R.id.selec_chapter:
                nextShow = MuneState.selecchapter;
                initConfigView();
                doMuneAnimal();
                mune_tittle.setText("作品章节");
                break;
        }
    }

    enum MuneState {
        typefacelistshow, none, mune, selecchapter
    }

    private boolean clearView() {
        if (muneState == MuneState.mune) {
            doMuneAnimal();
            return true;
        }

        if (muneState == MuneState.typefacelistshow) {
            doTypefaceAnimal();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (muneState == MuneState.none) {
            finish();
        } else if (muneState == MuneState.typefacelistshow || muneState == MuneState.selecchapter) {
            nextShow = MuneState.none;
            doTypefaceAnimal();
        } else if (muneState == MuneState.mune) {
            nextShow = MuneState.none;
            doMuneAnimal();
        }
    }
}
