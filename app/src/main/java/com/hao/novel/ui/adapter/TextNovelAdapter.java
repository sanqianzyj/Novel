package com.hao.novel.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hao.novel.R;
import com.hao.novel.spider.data.NovelIntroduction;

import java.util.List;


public class TextNovelAdapter extends RecyclerView.Adapter<TextNovelAdapter.NovelTextHolder> {
    private Context mContext;

    private List<NovelIntroduction> mNovelPage;

    public TextNovelAdapter(Context context, List<NovelIntroduction> novelPage) {
        this.mContext = context;
        this.mNovelPage = novelPage;
    }

    public List<NovelIntroduction> getDate() {
        return mNovelPage;
    }

    public void setmNovelPage(List<NovelIntroduction> mNovelPage) {
        this.mNovelPage = mNovelPage;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NovelTextHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.text_item, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new NovelTextHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NovelTextHolder novelHolder, final int i) {
        novelHolder.setDate(mNovelPage.get(i));
        novelHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.itemClick(i, novelHolder.view, mNovelPage.get(i));
                }
            }
        });
    }

    private OnItemClickListener onItemClickListener;

    public TextNovelAdapter setItemClickLisener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    public int getItemCount() {
        return mNovelPage == null ? 0 : mNovelPage.size();
    }

    public void setList(List<NovelIntroduction> novelListItemContents) {
        mNovelPage = novelListItemContents;
        notifyDataSetChanged();
    }

    class NovelTextHolder extends RecyclerView.ViewHolder {

        TextView novel_name;
        View view;

        public NovelTextHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            novel_name = itemView.findViewById(R.id.title_item);
        }

        public void setDate(NovelIntroduction novelClassify) {
            if (novelClassify.getNovelAutho() == null) {
                novelClassify.setNovelAutho("");
            }
            String auther = novelClassify.getNovelAutho() == null ? "" : ("(" + novelClassify.getNovelAutho() + ")");
            novel_name.setText(novelClassify.getNovelName());
        }
    }

    public interface OnItemClickListener {
        void itemClick(int position, View view, Object object);
    }
}
