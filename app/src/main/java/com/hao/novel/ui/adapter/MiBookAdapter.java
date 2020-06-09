package com.hao.novel.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hao.novel.R;
import com.hao.novel.base.App;
import com.hao.novel.db.manage.DbManage;
import com.hao.novel.spider.data.NovelChapter;
import com.hao.novel.spider.data.NovelIntroduction;
import com.hao.novel.ui.activity.NovelDetailActivity;
import com.hao.novel.ui.activity.NovelListActivity;
import com.hao.novel.ui.activity.ReadNovelActivity;
import com.hao.novel.ui.used.ReadInfo;

import java.util.List;


public class MiBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private boolean showDelete = false;

    public void setShowDelete(boolean showDelete) {
        this.showDelete = showDelete;
    }

    private List<ReadInfo> readInfos = DbManage.checkedAllReadInfo();

    public MiBookAdapter(Context context) {
        this.mContext = context;
    }

    public void refresh() {
        readInfos = DbManage.checkedAllReadInfo();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_bookshelf_lastest, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new MiNearReadHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_bookshelf_other, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new OtherNovelHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder novelHolder, int i) {
        if (i == 0) {
            ((MiNearReadHolder) novelHolder).setDate(readInfos.get(i), showDelete);
            ((MiNearReadHolder) novelHolder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    MiBookAdapter.this.showDelete = true;
                    MiBookAdapter.this.notifyDataSetChanged();
                    return true;
                }
            });
        } else {
            if (i * 3 + 1 > readInfos.size()) {
                ((OtherNovelHolder) novelHolder).setDate(readInfos.subList((i - 1) * 3 + 1, readInfos.size()), showDelete);
            } else {
                ((OtherNovelHolder) novelHolder).setDate(readInfos.subList((i - 1) * 3 + 1, i * 3 + 1), showDelete);
            }
            ((OtherNovelHolder) novelHolder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    MiBookAdapter.this.showDelete = true;
                    MiBookAdapter.this.notifyDataSetChanged();
                    return true;
                }
            });
        }


//        novelHolder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onItemClickListener != null) {
////                    onItemClickListener.itemClick(i, novelHolder.view, typefaces.get(i));
//                }
//            }
//        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    private OnItemClickListener onItemClickListener;

    public MiBookAdapter setItemClickLisener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (readInfos == null) {
            size = 0;
        } else {
            if (readInfos.size() > 1) {
                size = (readInfos.size() - 1) % 3 == 0 ? ((readInfos.size() - 1) / 3 + 1) : ((readInfos.size() - 1) / 3 + 2);
            } else {
                size = readInfos.size();
            }
        }
        Log.i("mibookadpter", size + "");
        return size;
    }

    public ReadInfo getItem(int index) {
        try {
            return readInfos.get(index);
        } catch (Exception e) {
            return null;
        }
    }

    public void removePosition(int position) {
        readInfos.remove(position);
        notifyItemRemoved(position);
    }

    public void removeReadInfo(ReadInfo readInfo) {
        for (int i = 0; i < readInfos.size(); i++) {
            if (readInfos.get(i).getNovelChapterListUrl().equals(readInfo.getNovelChapterListUrl())) {
                removePosition(i);
            }
        }
    }


    class MiNearReadHolder extends RecyclerView.ViewHolder {
        ImageView iv_cover;
        TextView novel_name;
        TextView tv_durprogress;
        TextView tv_watch;
        View itemView;
        View delete;

        public MiNearReadHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            iv_cover = itemView.findViewById(R.id.iv_cover);
            novel_name = itemView.findViewById(R.id.novel_name);
            tv_durprogress = itemView.findViewById(R.id.tv_durprogress);
            tv_watch = itemView.findViewById(R.id.tv_watch);
            delete = itemView.findViewById(R.id.delete);
        }

        public void setDate(ReadInfo readInfo, boolean showDelete) {
            if (showDelete) {
                delete.setVisibility(View.VISIBLE);
            } else {
                delete.setVisibility(View.GONE);
            }
            NovelIntroduction novelIntroduction = DbManage.checkNovelByUrl(readInfo.getNovelChapterListUrl());
            NovelChapter novelChapter = DbManage.checkNovelChaptterById(readInfo.getNovelChapterListUrl(), readInfo.getNovelChapterUrl());
            if (novelIntroduction != null) {
                novel_name.setText("《" + novelIntroduction.getNovelName() + "》");
                Glide.with(App.getInstance()).load(novelIntroduction.getNovelCover()).error(R.mipmap.back_1).into(iv_cover);
            }
            if (novelChapter != null) {
                tv_durprogress.setText(novelChapter.getChapterName());
            }
            tv_watch.setTag(readInfo);
            tv_watch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReadInfo readInfo = (ReadInfo) v.getTag();
                    if (tv_watch.getText().equals("继续阅读")) {
                        Intent intent = new Intent(mContext, ReadNovelActivity.class);
                        intent.putExtra("novelChapter", DbManage.checkNovelChaptterById(readInfo.getNovelChapterListUrl(), readInfo.getNovelChapterUrl()));
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, NovelListActivity.class);
                        mContext.startActivity(intent);
                    }
                }
            });
            itemView.setTag(readInfo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReadInfo readInfo = (ReadInfo) v.getTag();
                    Intent intent = new Intent(mContext, NovelDetailActivity.class);
                    intent.putExtra("novelId", readInfo.getNovelChapterListUrl());
                    ViewCompat.setTransitionName(iv_cover, "cover");
                    Pair<View, String> pair1 = new Pair<>((View) iv_cover, ViewCompat.getTransitionName(iv_cover));
                    /**
                     *4、生成带有共享元素的Bundle，这样系统才会知道这几个元素需要做动画
                     */
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, pair1);
                    ActivityCompat.startActivity(mContext, intent, activityOptionsCompat.toBundle());
                }
            });


            if (showDelete) {
                delete.setVisibility(View.VISIBLE);
            }
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DbManage.removeReadInfo(DbManage.checkedAllReadInfo().get(0));
                    removePosition(0);
                }
            });

        }
    }

    private class OtherNovelHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View itemView;
        RelativeLayout fl_content_1;
        ImageView iv_cover_1;
        TextView tv_name_1;
        ImageView delete_1;
        RelativeLayout fl_content_2;
        ImageView iv_cover_2;
        TextView tv_name_2;
        ImageView delete_2;
        RelativeLayout fl_content_3;
        ImageView iv_cover_3;
        TextView tv_name_3;
        ImageView delete_3;

        public OtherNovelHolder(@NonNull View itemView) {
            super(itemView);
            fl_content_1 = itemView.findViewById(R.id.fl_content_1);
            iv_cover_1 = itemView.findViewById(R.id.iv_cover_1);
            tv_name_1 = itemView.findViewById(R.id.tv_name_1);
            delete_1 = itemView.findViewById(R.id.delete_1);
            fl_content_2 = itemView.findViewById(R.id.fl_content_2);
            iv_cover_2 = itemView.findViewById(R.id.iv_cover_2);
            tv_name_2 = itemView.findViewById(R.id.tv_name_2);
            delete_2 = itemView.findViewById(R.id.delete_2);
            fl_content_3 = itemView.findViewById(R.id.fl_content_3);
            iv_cover_3 = itemView.findViewById(R.id.iv_cover_3);
            tv_name_3 = itemView.findViewById(R.id.tv_name_3);
            delete_3 = itemView.findViewById(R.id.delete_3);
        }

        public void setDate(List<ReadInfo> readInfo, boolean showDelete) {
            iv_cover_1.setOnClickListener(this);
            iv_cover_2.setOnClickListener(this);
            iv_cover_3.setOnClickListener(this);
            if (showDelete) {
                delete_1.setVisibility(View.VISIBLE);
                delete_2.setVisibility(View.VISIBLE);
                delete_3.setVisibility(View.VISIBLE);
            } else {
                delete_1.setVisibility(View.GONE);
                delete_2.setVisibility(View.GONE);
                delete_3.setVisibility(View.GONE);
            }
            for (int j = 0; j < readInfo.size(); j++) {
                NovelIntroduction novelIntroduction = DbManage.checkNovelByUrl(readInfo.get(j).getNovelChapterListUrl());
                TextView title = null;
                ImageView cover = null;
                RelativeLayout layout = null;
                ImageView delete = null;
                switch (j) {
                    case 0:
                        iv_cover_1.setTag(readInfo.get(j));
                        title = tv_name_1;
                        cover = iv_cover_1;
                        layout = fl_content_1;
                        delete_1.setTag(readInfo.get(j));
                        delete = delete_1;
                        break;
                    case 1:
                        iv_cover_2.setTag(readInfo.get(j));
                        title = tv_name_2;
                        cover = iv_cover_2;
                        layout = fl_content_2;
                        delete_2.setTag(readInfo.get(j));
                        delete = delete_2;
                        break;
                    case 2:
                        iv_cover_3.setTag(readInfo.get(j));
                        title = tv_name_3;
                        cover = iv_cover_3;
                        layout = fl_content_3;
                        delete_1.setTag(readInfo.get(j));
                        delete = delete_3;
                        break;
                    default:
                        break;
                }

                if (novelIntroduction != null) {
                    if (title != null) {
                        title.setText(novelIntroduction.getNovelName());
                    }
                    if (cover != null) {
                        Glide.with(App.getInstance()).load(novelIntroduction.getNovelCover()).error(R.mipmap.back_1).into(cover);
                    }
                    if (layout != null) {
                        layout.setVisibility(View.VISIBLE);
                    }
                }
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeReadInfo((ReadInfo) v.getTag());
                    }
                });
            }
        }

        @Override
        public void onClick(View v) {
            ReadInfo readInfo = (ReadInfo) v.getTag();
            NovelChapter novelChapter = DbManage.getChapterById(readInfo.getNovelChapterListUrl(), readInfo.getNovelChapterUrl());
            Intent intent = new Intent(mContext, ReadNovelActivity.class);
            intent.putExtra("novelChapter", novelChapter);
            mContext.startActivity(intent);
//            Intent intent = new Intent(mContext, NovelDetailActivity.class);
//            intent.putExtra("novelId", readInfo.getNovelChapterListUrl());
//            Pair<View, String> pair1 = null;
//            switch (v.getId()) {
//                case R.id.iv_cover_1:
//                    ViewCompat.setTransitionName(iv_cover_1, "cover");
//                    pair1 = new Pair<>((View) iv_cover_1, ViewCompat.getTransitionName(iv_cover_1));
//                    break;
//                case R.id.iv_cover_2:
//                    ViewCompat.setTransitionName(iv_cover_2, "cover");
//                    pair1 = new Pair<>((View) iv_cover_2, ViewCompat.getTransitionName(iv_cover_2));
//                    break;
//                case R.id.iv_cover_3:
//                    ViewCompat.setTransitionName(iv_cover_3, "cover");
//                    pair1 = new Pair<>((View) iv_cover_3, ViewCompat.getTransitionName(iv_cover_3));
//                    break;
//            }
//            if (pair1 != null) {
//                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, pair1);
//                ActivityCompat.startActivity(mContext, intent, activityOptionsCompat.toBundle());
//            } else {
//                App.getInstance().startActivity(intent);
//            }
        }
    }

    public interface OnItemClickListener {
        void itemClick(int position, View view, Object object);
    }


}
