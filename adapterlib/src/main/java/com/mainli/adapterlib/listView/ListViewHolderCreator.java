package com.mainli.adapterlib.listView;

import android.view.View;

import com.mainli.adapterlib.ViewHolderCreator;

/**
 * Created by Admin on 2016/4/28.
 */
public class ListViewHolderCreator implements ViewHolderCreator<ViewHolder> {
    public static ViewHolderCreator<ViewHolder> newInstance() {
        return new ListViewHolderCreator();
    }

    @Override
    public ViewHolder createHolder(View itemView, int viewSize, int itemViewType) {
        return new ViewHolder(itemView, viewSize, itemViewType);
    }
}
