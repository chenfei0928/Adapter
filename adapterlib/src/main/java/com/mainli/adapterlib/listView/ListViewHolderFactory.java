package com.mainli.adapterlib.listView;

import android.view.View;

/**
 * 创建用于 ListView 的 ViewHolder 接口
 * 可以通过重写此接口，支持更多的布局View绑定框架
 * Created by MrFeng on 2016/4/28.
 */
public interface ListViewHolderFactory<T> {
    T createHolder(View itemView, int viewSize, int itemViewType);
}
