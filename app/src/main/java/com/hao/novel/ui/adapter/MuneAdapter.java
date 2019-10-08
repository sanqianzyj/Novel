package com.hao.novel.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hao.novel.R;
import com.hao.novel.db.manage.DbManage;
import com.hao.novel.spider.data.NovelType;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MuneAdapter extends RecyclerView.Adapter<MuneAdapter.MuneViewHolder> {
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
    boolean isNeedLoadAnimal = true;

    MuneAdapterListener muneAdapterListener;
    List<NovelType> novelTypes;

    public MuneAdapter(Context context, boolean isNeedLoadAnimal) {
        this.context = context;
        if (isNeedLoadAnimal) {
            isOpen = true;
        }
        novelTypes = DbManage.getNovelType();
        count = novelTypes == null ? 0 : novelTypes.size();
    }


    public boolean isOpen() {
        return isOpen;
    }

    @NonNull
    @Override
    public MuneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MuneViewHolder(LayoutInflater.from(context).inflate(R.layout.mune_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MuneViewHolder holder, int position) {
        if (isNeedLoadAnimal) {
            startAnimal(holder, position);
        }
        holder.setDate(novelTypes.get(position));
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

    class MuneViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView text;

        public MuneViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            text = view.findViewById(R.id.title);
        }

        public void setDate(NovelType novelType) {
            text.setText(novelType.getType());
        }
    }

    public void setMuneAdapterListener(MuneAdapterListener muneAdapterListener) {
        this.muneAdapterListener = muneAdapterListener;
    }

    public interface MuneAdapterListener {
        void animalEnd();
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
                        muneAdapterListener.animalEnd();
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
                        muneAdapterListener.animalEnd();
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
