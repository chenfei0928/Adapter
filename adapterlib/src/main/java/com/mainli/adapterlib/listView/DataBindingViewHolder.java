package com.mainli.adapterlib.listView;

import android.databinding.ViewDataBinding;

/**
 * Created by Admin on 2016/4/29.
 */
public class DataBindingViewHolder<T extends ViewDataBinding> extends ViewHolder {
    T dataBinding;

    DataBindingViewHolder(T binding, int viewSize, int itemViewType) {
        super(binding.getRoot(), viewSize, itemViewType);
        dataBinding = binding;
    }

    public T getDataBinding() {
        return dataBinding;
    }
}
