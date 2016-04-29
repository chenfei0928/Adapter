package com.mainli.adapterlib.listView;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.mainli.adapterlib.ViewHolderCreator;

/**
 * 用于 DataBinding View 的 ViewHolder 创建器
 * 由于泛型擦除机制原因，构造器只能像Gson的TypeToken一样
 * Created by Admin on 2016/4/29.
 */
public class ListViewBindingCreator<T extends ViewDataBinding>
        implements ViewHolderCreator<DataBindingViewHolder<T>> {
    ListViewBindingCreator() {
    }

    @Override
    public DataBindingViewHolder<T> createHolder(View itemView, int viewSize, int itemViewType) {
        return new DataBindingViewHolder<>(DataBindingUtil.<T>bind(itemView), viewSize, itemViewType);
    }
}
