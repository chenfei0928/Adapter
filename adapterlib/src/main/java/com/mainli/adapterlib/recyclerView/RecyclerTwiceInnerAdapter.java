package com.mainli.adapterlib.recyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecViewHolderUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * 可以容纳两个内部实现适配器，实现当一个主适配器无内容时显示副适配器的内容
 * 暂未作测试
 * Created by MrFeng on 2016/4/29.
 */
public class RecyclerTwiceInnerAdapter<Main extends RecyclerView.Adapter, Minor extends RecyclerView.Adapter>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @NonNull
    private Minor mMinor;
    private Main mMain;
    private int mMainTypeCount;

    public RecyclerTwiceInnerAdapter(Main mMain, int mainViewTypeCount, @NonNull Minor mMinor) {
        this.mMain = mMain;
        this.mMainTypeCount = mainViewTypeCount;
        this.mMinor = mMinor;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder.getItemViewType() < mMainTypeCount) {
            mMain.onViewAttachedToWindow(holder);
        } else {
            mMinor.onViewAttachedToWindow(
                    RecViewHolderUtil.changedMinorViewType(holder, mMainTypeCount));
            RecViewHolderUtil.restoreMinorViewType(holder, mMainTypeCount);
        }
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
        if (hasMainResult()) {
            mMain.setHasStableIds(hasStableIds);
        } else {
            mMinor.setHasStableIds(hasStableIds);
        }
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        mMain.registerAdapterDataObserver(observer);
        mMinor.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        mMain.unregisterAdapterDataObserver(observer);
        mMinor.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (holder.getItemViewType() < mMainTypeCount) {
            mMain.onViewRecycled(holder);
        } else {
            mMinor.onViewRecycled(
                    RecViewHolderUtil.changedMinorViewType(holder, mMainTypeCount));
            RecViewHolderUtil.restoreMinorViewType(holder, mMainTypeCount);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mMain.onAttachedToRecyclerView(recyclerView);
        mMinor.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<
            Object> payloads) {
        if (holder.getItemViewType() < mMainTypeCount) {
            mMain.onBindViewHolder(holder, position, payloads);
        } else {
            mMinor.onBindViewHolder(
                    RecViewHolderUtil.changedMinorViewType(holder, mMainTypeCount),
                    position, payloads);
            RecViewHolderUtil.restoreMinorViewType(holder, mMainTypeCount);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mMain.onDetachedFromRecyclerView(recyclerView);
        mMinor.onDetachedFromRecyclerView(recyclerView);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        if (holder.getItemViewType() < mMainTypeCount) {
            return mMain.onFailedToRecycleView(holder);
        } else {
            boolean b = mMinor.onFailedToRecycleView(
                    RecViewHolderUtil.changedMinorViewType(holder, mMainTypeCount));
            RecViewHolderUtil.restoreMinorViewType(holder, mMainTypeCount);
            return b;
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder.getItemViewType() < mMainTypeCount) {
            mMain.onViewDetachedFromWindow(holder);
        } else {
            mMinor.onViewDetachedFromWindow(RecViewHolderUtil.changedMinorViewType(holder, mMainTypeCount));
            RecViewHolderUtil.restoreMinorViewType(holder, mMainTypeCount);
        }
    }

    @Override
    public long getItemId(int position) {
        return hasMainResult() ? mMain.getItemId(position) : mMinor.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return hasMainResult() ? mMain.getItemViewType(position)
                : mMinor.getItemViewType(position) + mMainTypeCount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return hasMainResult() ? mMain.onCreateViewHolder(parent, viewType)
                : mMinor.onCreateViewHolder(parent, viewType - mMainTypeCount);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (hasMainResult()) {
            mMain.onBindViewHolder(holder, position);
        } else {
            mMinor.onBindViewHolder(RecViewHolderUtil.changedMinorViewType(holder, mMainTypeCount), position);
            RecViewHolderUtil.restoreMinorViewType(holder, mMainTypeCount);
        }
    }

    @Override
    public int getItemCount() {
        return hasMainResult() ? mMain.getItemCount() : mMinor.getItemCount();
    }

    private boolean hasMainResult() {
        return mMain.getItemCount() != 0;
    }
}
