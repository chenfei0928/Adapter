package com.mainli.adapterlib.recyclerView;

import android.view.View;

/**
 * 用于 RecyclerView 的 ViewHolder 工厂
 * Created by MrFeng on 2016/4/29.
 */
public class RecViewHolderFactory implements RecyclerViewHolderFactory<RecViewHolder> {
    @Override
    public RecViewHolder createHolder(View itemView, int viewSize) {
        return new RecViewHolder(itemView, viewSize);
    }
}
