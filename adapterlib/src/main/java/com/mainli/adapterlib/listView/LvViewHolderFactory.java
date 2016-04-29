package com.mainli.adapterlib.listView;

import android.view.View;

/**
 * ListView的普通ViewHolder工厂
 * Created by MrFeng on 2016/4/28.
 */
public class LvViewHolderFactory implements ListViewHolderFactory<LvViewHolder> {
    @Override
    public LvViewHolder createHolder(View itemView, int viewSize, int itemViewType) {
        return new LvViewHolder(itemView, viewSize, itemViewType);
    }
}
