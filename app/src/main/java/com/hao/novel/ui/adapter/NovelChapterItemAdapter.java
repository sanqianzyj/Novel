package com.hao.novel.ui.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hao.novel.R;
import com.hao.novel.base.App;
import com.hao.novel.db.manage.DbManage;
import com.hao.novel.spider.data.NovelChapter;
import com.hao.novel.spider.data.NovelIntroduction;

import java.util.ArrayList;
import java.util.List;


public class NovelChapterItemAdapter extends RecyclerView.Adapter {
    Activity mcontext;
    List<NovelChapter> novelChapterList = new ArrayList<>();
    NovelIntroduction novelIntroduction;
    AdapterView.OnItemClickListener onItemClickListener;


    public void setNovelChapterList(List<NovelChapter> novelChapterList) {
        this.novelChapterList = novelChapterList;
        notifyDataSetChanged();
    }

    public NovelChapterItemAdapter(Activity context) {
        mcontext = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeadViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.novel_detail_head, null));
        } else {
            return new ChapterListViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.text_item, null));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ((HeadViewHolder) holder).initDate(novelIntroduction);
        } else {
            ((ChapterListViewHolder) holder).initDate(novelChapterList.get(position - 1), position);
            if (onItemClickListener != null) {
                ((ChapterListViewHolder) holder).setOnItemClickListener(onItemClickListener);
            }
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public int getItemCount() {
        return novelChapterList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setHead(NovelIntroduction novelIntroduction) {
        this.novelIntroduction = novelIntroduction;
        notifyDataSetChanged();
    }

    public NovelChapter getItem(int position) {
        return novelChapterList.get(position);
    }


    class ChapterListViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        AdapterView.OnItemClickListener onItemClickListener;
        View itemView;

        public ChapterListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            textView = itemView.findViewById(R.id.title_item);
        }

        public void initDate(NovelChapter novelChapter, int position) {
            textView.setText(novelChapter.getChapterName());
            textView.setTag(position - 1);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null, v, (int) v.getTag(), v.getId());
                }
            });
        }

        public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {
        TextView novel_title;
        TextView novel_author;
        TextView novel_type;
        TextView novel_lenth;
        TextView novel_introduce;
        TextView novel_now_read;
        TextView novel_last_chaper;
        ImageView novel_detail_image;
        LinearLayout novel_now_read_ll;


        public HeadViewHolder(@NonNull View itemView) {
            super(itemView);
            novel_title = itemView.findViewById(R.id.novel_title);
            novel_author = itemView.findViewById(R.id.novel_author);
            novel_type = itemView.findViewById(R.id.novel_type);
            novel_lenth = itemView.findViewById(R.id.novel_lenth);
            novel_introduce = itemView.findViewById(R.id.novel_introduce);
            novel_now_read = itemView.findViewById(R.id.novel_now_read);
            novel_last_chaper = itemView.findViewById(R.id.novel_last_chaper);
            novel_detail_image = itemView.findViewById(R.id.novel_detail_image);
            novel_now_read_ll = itemView.findViewById(R.id.novel_now_read_ll);
        }

        private void initDate(NovelIntroduction novelIntroduction) {
            if (novelIntroduction != null) {
                novel_title.setText(novelIntroduction.getNovelName());
                novel_author.setText(novelIntroduction.getNovelAutho());
                novel_type.setText(novelIntroduction.getNovelType());
                novel_introduce.setText("\t\t" + novelIntroduction.getNovelIntroduce());
                novel_last_chaper.setText(novelIntroduction.getNovelNewChapterTitle());
                Glide.with(App.getInstance()).load(novelIntroduction.getNovelCover()).error(R.mipmap.novel_normal_cover).into(novel_detail_image);
                if (TextUtils.isEmpty(novelIntroduction.getNowReadID()) || TextUtils.isEmpty(novelIntroduction.getNowRead())) {
                    novel_now_read_ll.setVisibility(View.GONE);
                } else {
                    novel_now_read.setText(novelIntroduction.getNowRead());
                }
            }
        }
    }
}
