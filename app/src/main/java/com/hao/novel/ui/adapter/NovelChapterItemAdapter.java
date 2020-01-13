package com.hao.novel.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hao.novel.R;
import com.hao.novel.spider.data.NovelChapter;

import java.util.ArrayList;
import java.util.List;


public class NovelChapterItemAdapter extends BaseAdapter {
    Activity mcontext;
    List<NovelChapter> novelChapterList=new ArrayList<>();


    public void setNovelChapterList(List<NovelChapter> novelChapterList) {
        this.novelChapterList = novelChapterList;
        notifyDataSetChanged();
    }

    public NovelChapterItemAdapter(Activity context) {
        mcontext = context;
    }

    @Override
    public int getCount() {
        return novelChapterList.size();
    }

    @Override
    public NovelChapter getItem(int position) {
        return novelChapterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.text_item, null);
            viewHolder.textView = convertView.findViewById(R.id.title_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(novelChapterList.get(position).getChapterName());
        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }
}
