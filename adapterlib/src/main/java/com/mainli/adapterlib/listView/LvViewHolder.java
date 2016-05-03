package com.mainli.adapterlib.listView;

import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.View;

import com.mainli.adapterlib.ItemViewCounter;
import com.mainli.adapterlib.ViewHolderHelper;

/**
 * ListView GridView 通用ViewHolder
 */
public class LvViewHolder implements ItemViewCounter {
    private int itemViewType;
    private View itemView;
    private SparseArray<View> viewList;

    /* package */ LvViewHolder(View itemView, int viewSize, int itemViewType) {
        this.itemView = itemView;
        this.viewList = new SparseArray<>(viewSize == viewSizeUndefined ? 10 : viewSize);
        this.itemViewType = itemViewType;
    }

    @Override
    public int countView() {
        return viewList.size();
    }

    public <T extends View> T get(@IdRes int id) {
        return get(id, null);
    }

    public <T extends View> T get(@IdRes int id, Class<T> viewType) {
        return ViewHolderHelper.get(itemView, viewList, id, viewType);
    }

    public int getItemViewType() {
        return itemViewType;
    }
}
