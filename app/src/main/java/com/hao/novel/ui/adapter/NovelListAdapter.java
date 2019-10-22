package com.hao.novel.ui.adapter;

import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hao.novel.R;
import com.hao.novel.base.App;
import com.hao.novel.db.manage.DbManage;
import com.hao.novel.spider.data.NovelIntroduction;
import com.hao.novel.spider.data.NovelType;

import org.w3c.dom.Text;

import java.util.List;

/**
 * 小说列表
 */
public class NovelListAdapter extends RecyclerView.Adapter<NovelListAdapter.MuneViewHolder> {
    Context context;

    /**
     * view是否显示
     */
    boolean isOpen = false;

    int count;

    /**
     * 是否正在执行动画
     */
    boolean isInAnimal = false;

    /**
     * 是否需要动画
     */
    boolean isNeedLoadAnimal = false;

    /**
     * 当前显示的小说列表页数 30本为一页 同步加载网页
     */
    //TODO 通过加载的网页进行控制
    int page=0;

    MuneAdapterListener muneAdapterListener;
    List<NovelIntroduction> novelIntroductions;

    public NovelListAdapter(Context context, boolean isNeedLoadAnimal) {
        this.context = context;
        if (isNeedLoadAnimal) {
            isOpen = true;
        }
    }


    public void setDate(List<NovelIntroduction> novelIntroductions) {
        this.novelIntroductions = novelIntroductions;
    }


    public NovelIntroduction getItem(int i) {
        return novelIntroductions.get(i);
    }


    public boolean isOpen() {
        return isOpen;
    }

    @NonNull
    @Override
    public MuneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MuneViewHolder(LayoutInflater.from(context).inflate(R.layout.novel_list_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MuneViewHolder holder, int position) {
        if (isNeedLoadAnimal) {
            startAnimal(holder, position);
        }
        holder.setDate(novelIntroductions.get(position));
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public void changeDisOrHide() {
        if (!isInAnimal) {
            isOpen = !isOpen;
            notifyDataSetChanged();
        }
    }

    public void notifyData(String type) {
        if(page>=0){
            novelIntroductions=DbManage.getNovelByType(type,page);
            count=novelIntroductions.size();
        }
        notifyDataSetChanged();
    }


    class MuneViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView novel_avatar;
        TextView novel_name;
        TextView novel_author;
        TextView novel_new;

        public MuneViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            novel_avatar = view.findViewById(R.id.novel_avatar);
            novel_name = view.findViewById(R.id.novel_name);
            novel_author = view.findViewById(R.id.novel_author);
            novel_new = view.findViewById(R.id.novel_new);
        }

        public void setDate(NovelIntroduction novelIntroductions) {
            Glide.with(App.getInstance()).load(novelIntroductions.getNovelCover()).error(R.mipmap.novel_normal_cover).into(novel_avatar);
            novel_name.setText(novelIntroductions.getNovelName());
            novel_author.setText("作者：" + novelIntroductions.getNovelAutho());
            novel_new.setText("最新章节：" + novelIntroductions.getNovelNewChapterTitle());
        }
    }

    public void setMuneAdapterListener(MuneAdapterListener muneAdapterListener) {
        this.muneAdapterListener = muneAdapterListener;
    }

    public interface MuneAdapterListener {
        void animalInEnd();

        void animalOutEnd();
    }


    public void startAnimal(@NonNull final MuneViewHolder holder, final int position) {
        isInAnimal = true;
        final Animation animationin = AnimationUtils.loadAnimation(holder.view.getContext(), R.anim.anim_item_in);
        animationin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (position == count - 1) {
                    isInAnimal = false;
                    if (muneAdapterListener != null)
                        muneAdapterListener.animalInEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        final Animation animationout = AnimationUtils.loadAnimation(holder.view.getContext(), R.anim.anim_item_out);
        animationout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (position == 0) {
                    isInAnimal = false;
                    if (muneAdapterListener != null)
                        muneAdapterListener.animalOutEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if (isOpen) {
            holder.view.setVisibility(View.INVISIBLE);
        } else {
            holder.view.setVisibility(View.VISIBLE);
        }
        animationin.setDuration(100);
        animationout.setDuration(100);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isOpen) {
                    holder.view.startAnimation(animationin);
                    holder.view.setVisibility(View.VISIBLE);
                } else {
                    holder.view.startAnimation(animationout);
                    holder.view.setVisibility(View.GONE);
                }
            }
        }, isOpen ? position * 100 : (count - position - 1) * 100);
    }


}
