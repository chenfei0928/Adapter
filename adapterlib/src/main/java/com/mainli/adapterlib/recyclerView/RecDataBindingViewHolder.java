package com.mainli.adapterlib.recyclerView;

import android.databinding.ViewDataBinding;

/**
 * 用于 DataBinding 的 RecyclerView ViewHolder
 * Created by MrFeng on 2016/4/29.
 */
public class RecDataBindingViewHolder extends RecViewHolder {
    ViewDataBinding dataBinding;

    RecDataBindingViewHolder(ViewDataBinding binding, int viewSize) {
        super(binding.getRoot(), viewSize);
        this.dataBinding = binding;
    }

    public <T extends ViewDataBinding> T getDataBinding() {
        //noinspection unchecked
        return (T) dataBinding;
    }
}
