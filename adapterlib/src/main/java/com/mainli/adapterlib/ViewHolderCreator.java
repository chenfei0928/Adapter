package com.mainli.adapterlib;

import android.view.View;

/**
 * 创建ViewHolder
 * Created by Admin on 2016/4/28.
 */
public interface ViewHolderCreator<T> {
    T createHolder(View itemView, int viewSize, int itemViewType);
}
