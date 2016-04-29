package com.mainli.adapterlib;

/**
 * ListView或RecyclerView的ViewHolder用于缓存item的View数量使用
 * Created by MrFeng on 2016/4/29.
 */
public interface ItemViewCounter {
    int viewSizeUndefined = -1;
    int countView();
}
