package com.sponsor.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.nineoldandroids.animation.ObjectAnimator;
import com.orhanobut.logger.Logger;
import com.shizhefei.mvc.IDataAdapter;
import com.sponsor.android.R;
import com.sponsor.android.entity.SponsorshipEntity;
import com.sponsor.android.manager.PhoneManager;
import com.sponsor.android.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MySponsorshipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IDataAdapter<List<SponsorshipEntity>> {
    private List<SponsorshipEntity> list = new ArrayList<SponsorshipEntity>();
    private Context context;
    private int lastAnimatedPosition = -1; //上一个执行动画的位置

    public MySponsorshipAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_sponsorship, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position > 5) {
            runEnterAnimation(holder.itemView, position);
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        final SponsorshipEntity entity = list.get(position);
        Logger.i(JSON.toJSONString(entity));

        viewHolder.tvTitle.setText(entity.getName());
        viewHolder.tvSponsorName.setText(entity.getSponsorName());
        String updatedAt = DateUtil.convert(entity.getUpdatedAt(), null, "yyyy/MM/dd");
        viewHolder.tvUpdatedAt.setText(updatedAt);
        viewHolder.tvIntro.setText(entity.getIntro());
        String applicationCondition = String.format(context.getString(R.string.format_sponsorship_intro), entity.getApplicationCondition());
        SpannableStringBuilder applicationConditionStyle = new SpannableStringBuilder(applicationCondition);
        applicationConditionStyle.setSpan(new BackgroundColorSpan(Color.GREEN), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.tvApplyCondition.setText(applicationConditionStyle);

        if (entity.isApplied()) {
            viewHolder.tvApplyStatus.setVisibility(View.VISIBLE);
            viewHolder.tvApplyStatus.setText(R.string.applied);
            if (entity.isApprovedApply()) {
                viewHolder.tvApplyStatus.setText(R.string.approved);
            } else if (entity.isRejectedApply()) {
                viewHolder.tvApplyStatus.setText(R.string.rejected);
            }
        } else {
            viewHolder.tvApplyStatus.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void notifyDataChanged(List<SponsorshipEntity> data, boolean isRefresh) {
        if (isRefresh) {
            lastAnimatedPosition = -1;
            list.clear();
        }
        list.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public List<SponsorshipEntity> getData() {
        return list;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * 每个Item进来的时候都执行动画
     *
     * @param itemView
     * @param position
     */
    public void runEnterAnimation(View itemView, int position) {
        if (position > lastAnimatedPosition) { // 只有在下滑的时候才执行动画
            lastAnimatedPosition = position;
            itemView.setTranslationY(PhoneManager.getScreenHeight());
            ObjectAnimator enterAnim = ObjectAnimator.ofFloat(itemView, "translationY", 0);
            enterAnim.setDuration(500);
            enterAnim.setInterpolator(new DecelerateInterpolator(3));
            enterAnim.start();
        }
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_sponsorship.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tvTitle)
        TextView tvTitle;
        @Bind(R.id.tvSponsorName)
        TextView tvSponsorName;
        @Bind(R.id.tvUpdatedAt)
        TextView tvUpdatedAt;
        @Bind(R.id.tvApplyStatus)
        TextView tvApplyStatus;
        @Bind(R.id.tvIntro)
        TextView tvIntro;
        @Bind(R.id.tvApplyCondition)
        TextView tvApplyCondition;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
