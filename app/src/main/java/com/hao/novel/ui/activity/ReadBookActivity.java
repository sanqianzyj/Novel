package com.hao.novel.ui.activity;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hao.lib.Util.RecycleViewDivider;
import com.hao.lib.Util.SystemUtil;
import com.hao.novel.R;
import com.hao.novel.base.App;
import com.hao.novel.base.BaseActivity;
import com.hao.novel.db.manage.DbManage;
import com.hao.novel.service.DownListener;
import com.hao.novel.service.DownLoadNovelService;
import com.hao.novel.service.NovolDownTask;
import com.hao.novel.spider.data.NovelChapter;
import com.hao.novel.ui.adapter.TextTypefaceAdapter;
import com.hao.novel.ui.used.ReadInfo;
import com.hao.novel.view.minovelshow.NovelPageInfo;
import com.hao.novel.view.minovelshow.NovelTextView;
import com.hao.novel.view.minovelshow.MiTextViewConfig;
import com.hao.novel.view.minovelshow.PullViewLayout;

import java.util.List;

public class ReadBookActivity extends BaseActivity implements PullViewLayout.PullViewLayoutListener {
    PullViewLayout pullViewLayout;
    NovelTextView miTextView;
    MiTextViewConfig miTextViewConfig;
    TextTypefaceAdapter textTypefaceAdapter;
    LinearLayout text_typeface_layout;
    View readbook_config;
    MuneState muneState = MuneState.none;//当前菜单状态
    MuneState nextShow = MuneState.mune;//初始化下一个需要的界面为菜单
    TextView textsize;//字体大小
    TextView word_spac_size;//文字间距


    NovelChapter novelChapter;//当前页下载的小说内容
    int i = 0;
    ValueAnimator valueAnimator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);
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
                    if (nextShow == MuneState.typefacelistshow) {
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
    }

    public void initDate() {
        miTextViewConfig = MiTextViewConfig.getDefoutConfig(miTextView.getWidth(), miTextView.getHeight());
        textsize.setText((int) SystemUtil.px2dip(App.getInstance(), miTextViewConfig.getTextSize()) + "");
        word_spac_size.setText((int) SystemUtil.px2dip(App.getInstance(), miTextViewConfig.getWordSpacingExtra()) + "");
        Log.i("流程", "宽：" + miTextViewConfig.getViewWidth() + " 高：" + miTextViewConfig.getViewhigh() + " 行字数：" + miTextViewConfig.getLineTextNum() + " 行数：" + miTextViewConfig.getLineNum());
        if (novelChapter == null) {
            return;
        }
        if (novelChapter.getIsComplete()) {
            if (miTextViewConfig != null) {
                List<View> novelPageInfos = miTextViewConfig.initText(novelChapter);
                pullViewLayout.addListViewEnd(novelPageInfos);
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
                                pullViewLayout.addListViewEnd(novelPageInfos);
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
        initConfigView();
    }

    private void findView() {
        Log.i("流程", "初始化view");
        pullViewLayout = findViewById(R.id.novel_show);
        View novel_content_layout = LayoutInflater.from(this).inflate(R.layout.novel_content_layout, null);
        pullViewLayout.addView(novel_content_layout);
        miTextView = novel_content_layout.findViewById(R.id.novel_content);
        novelChapter = getIntent().getParcelableExtra("novelChapter");
        pullViewLayout.setBackgroundResource(R.mipmap.bg_readbook_yellow);
        pullViewLayout.setPullViewLayoutListener(this);
        text_typeface_layout = findViewById(R.id.text_typeface_layout);


        textsize = findViewById(R.id.textsize);
        findViewById(R.id.add_textsize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float textSizeadd = Float.parseFloat(textsize.getText().toString());
                textSizeadd = textSizeadd + 1;
                textsize.setText(textSizeadd + "");
                textSizeadd = SystemUtil.dp2px(App.getInstance(), (float) textSizeadd);
                if (miTextViewConfig != null) {
                    miTextViewConfig.setTextSize(textSizeadd);
                    pullViewLayout.clear();
                    initDate();
                }
            }
        });

        findViewById(R.id.refuse_textsize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float textSizeRefuse = Float.parseFloat(textsize.getText().toString());
                if (textSizeRefuse > 13) {
                    textSizeRefuse = textSizeRefuse - 1;
                    textsize.setText(textSizeRefuse + "");
                    textSizeRefuse = SystemUtil.dp2px(App.getInstance(), (float) textSizeRefuse);
                }

                if (miTextViewConfig != null) {
                    miTextViewConfig.setTextSize(textSizeRefuse);
                    pullViewLayout.clear();
                    initDate();
                }
            }
        });


        word_spac_size = findViewById(R.id.word_spac_size);
        findViewById(R.id.word_spac_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float textSizeadd = Float.parseFloat(word_spac_size.getText().toString());
                textSizeadd = textSizeadd + 1;
                word_spac_size.setText(textSizeadd + "");
                textSizeadd = SystemUtil.dp2px(App.getInstance(), (float) textSizeadd);
                if (miTextViewConfig != null) {
                    miTextViewConfig.setTextSize(textSizeadd);
                    pullViewLayout.clear();
                    initDate();
                }
            }
        });

        findViewById(R.id.word_spac_refuse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float textSizeRefuse = Float.parseFloat(word_spac_size.getText().toString());
                if (textSizeRefuse > 13) {
                    textSizeRefuse = textSizeRefuse - 1;
                    word_spac_size.setText(textSizeRefuse + "");
                    textSizeRefuse = SystemUtil.dp2px(App.getInstance(), (float) textSizeRefuse);
                }

                if (miTextViewConfig != null) {
                    miTextViewConfig.setTextSize(textSizeRefuse);
                    pullViewLayout.clear();
                    initDate();
                }
            }
        });


        readbook_config = findViewById(R.id.readbook_config);
        findViewById(R.id.select_typeface).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextShow = MuneState.typefacelistshow;
                doMuneAnimal();
            }
        });
    }

    //加载菜单相关的配置信息
    private void initConfigView() {
        Log.i("流程", "加载配置信息");
        RecyclerView text_typeface = findViewById(R.id.text_typeface);
        text_typeface.setLayoutManager(new LinearLayoutManager(App.getInstance()));
        text_typeface.addItemDecoration(new DividerItemDecoration(App.getInstance(), DividerItemDecoration.VERTICAL));
        RecycleViewDivider recycleViewDivider = new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(this, R.color.line));
        text_typeface.addItemDecoration(recycleViewDivider);
        textTypefaceAdapter = new TextTypefaceAdapter(this).setItemClickLisener(new TextTypefaceAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position, View view, Object object) {
                miTextView.getMiTextViewConfig().setTypeface(textTypefaceAdapter.getItem(position).getTypeface());
                pullViewLayout.refreshView();
            }
        });
        text_typeface.setAdapter(textTypefaceAdapter);
        Log.i("流程", "加载配置信息完成");
    }

    @Override
    public void noDate() {

    }

    @Override
    public void nextPage(View v) {
        if (v instanceof NovelTextView) {
            ReadInfo readInfo = new ReadInfo();
            readInfo.setCid(((NovelTextView) v).getNovelPageInfo().getNovelCId());
            readInfo.setNid(((NovelTextView) v).getNovelPageInfo().getNovelNId());
            readInfo.setPage(((NovelTextView) v).getNovelPageInfo().getPage());
            DbManage.saveReadInfo(readInfo);
        } else if (v instanceof ViewGroup) {
            for (int j = 0; j < ((ViewGroup) v).getChildCount(); j++) {
                nextPage(((ViewGroup) v).getChildAt(i));
            }
        }
    }

    @Override
    public void lastPage(View v) {
        if (v instanceof NovelTextView) {
            ReadInfo readInfo = new ReadInfo();
            readInfo.setCid(((NovelTextView) v).getNovelPageInfo().getNovelCId());
            readInfo.setNid(((NovelTextView) v).getNovelPageInfo().getNovelNId());
            readInfo.setPage(((NovelTextView) v).getNovelPageInfo().getPage());
            DbManage.saveReadInfo(readInfo);
        } else if (v instanceof ViewGroup) {
            for (int j = 0; j < ((ViewGroup) v).getChildCount(); j++) {
                nextPage(((ViewGroup) v).getChildAt(i));
            }
        }
    }

    @Override
    public void needLoadDate(boolean isHead) {
        if (isHead) {
            NovelPageInfo novelPageInfo = (pullViewLayout.getContentMiTextPage()).getNovelPageInfo();
            novelChapter = DbManage.getChapterById(novelPageInfo.getNovelNId(), novelPageInfo.getNovelCId());
            novelChapter = DbManage.checkNovelChaptterByUrl(novelChapter.getBeforChapterUrl());
            if (novelChapter != null) {
                initDate();
            }
        } else {
            NovelPageInfo novelPageInfo = (pullViewLayout.getContentMiTextPage()).getNovelPageInfo();
            novelChapter = DbManage.getChapterById(novelPageInfo.getNovelNId(), novelPageInfo.getNovelCId());
            novelChapter = DbManage.checkNovelChaptterByUrl(novelChapter.getNextChapterUrl());
            if (novelChapter != null) {
                initDate();
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
        if (!valueAnimator.isRunning()) {
            if (nextShow == MuneState.mune) {
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
            } else if (muneState == MuneState.typefacelistshow) {
                doTypefaceAnimal();
            }
        }
    }


    private void doTypefaceAnimal() {
        initAnimal();
        if (!valueAnimator.isRunning()) {
            if (nextShow == MuneState.typefacelistshow) {
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float translatex = -(float) animation.getAnimatedValue() * text_typeface_layout.getWidth();
                        Log.i("位置  进", "translatex=" + translatex);
                        text_typeface_layout.setTranslationX(translatex);
                        if ((float) animation.getAnimatedValue() == 1) {
                            muneState = MuneState.typefacelistshow;
                            nextShow = MuneState.none;
                        }
                    }
                });
                valueAnimator.start();
            } else if (muneState == MuneState.typefacelistshow) {//已显示字体类型列表
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


    enum MuneState {
        typefacelistshow, none, mune
    }

}
