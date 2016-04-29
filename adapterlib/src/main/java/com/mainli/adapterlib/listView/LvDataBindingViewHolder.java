package com.mainli.adapterlib.listView;

import android.databinding.ViewDataBinding;

/**
 * 用于 DataBinding 的 ListView ViewHolder
 * Created by Admin on 2016/4/29.
 */
public class LvDataBindingViewHolder extends LvViewHolder {
    ViewDataBinding dataBinding;

    LvDataBindingViewHolder(ViewDataBinding binding, int viewSize, int itemViewType) {
        super(binding.getRoot(), viewSize, itemViewType);
        dataBinding = binding;
    }

    public <T extends ViewDataBinding> T getDataBinding() {
        //noinspection unchecked
        return (T) dataBinding;
    }
}
