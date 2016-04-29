package com.mainli.adapterlib.listView;

import android.databinding.DataBindingUtil;
import android.view.View;

/**
 * 用于 DataBinding View 的 LvViewHolder 创建器
 * Created by MrFeng on 2016/4/29.
 */
public class LvDataBindingViewHolderFactory implements ListViewHolderFactory<LvDataBindingViewHolder> {
    @Override
    public LvDataBindingViewHolder createHolder(View itemView, int viewSize, int itemViewType) {
        return new LvDataBindingViewHolder(DataBindingUtil.bind(itemView), viewSize, itemViewType);
    }
}
