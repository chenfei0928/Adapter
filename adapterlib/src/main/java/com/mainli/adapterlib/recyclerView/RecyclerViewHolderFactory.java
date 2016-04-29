package com.mainli.adapterlib.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 创建用于RecyclerView的ViewHolder接口
 * 可以通过重写此接口，支持更多的布局View绑定框架
 * Created by MrFeng on 2016/4/28.
 */
public interface RecyclerViewHolderFactory<T extends RecyclerView.ViewHolder> {
    T createHolder(View itemView, int viewSize);
}
