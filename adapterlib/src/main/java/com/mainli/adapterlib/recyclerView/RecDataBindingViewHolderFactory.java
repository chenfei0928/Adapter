package com.mainli.adapterlib.recyclerView;

import android.databinding.DataBindingUtil;
import android.view.View;

/**
 * 用于 DataBinding 的 RecyclerView ViewHolder
 * Created by MrFeng on 2016/4/29.
 */
public class RecDataBindingViewHolderFactory implements RecyclerViewHolderFactory<RecDataBindingViewHolder> {
    @Override
    public RecDataBindingViewHolder createHolder(View itemView, int viewSize) {
        return new RecDataBindingViewHolder(DataBindingUtil.bind(itemView), viewSize);
    }
}
