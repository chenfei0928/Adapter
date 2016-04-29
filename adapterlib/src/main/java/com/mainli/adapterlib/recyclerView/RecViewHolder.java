package com.mainli.adapterlib.recyclerView;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.mainli.adapterlib.ItemViewCounter;
import com.mainli.adapterlib.ViewHolderHelper;

/**
 * RecyclerView - LvViewHolder
 * Created by Mainli on 2016/4/13.
 */
public class RecViewHolder extends RecyclerView.ViewHolder implements ItemViewCounter {
    private SparseArray<View> mViews;

    /* package */ RecViewHolder(View itemView, int viewSize) {
        super(itemView);
        mViews = new SparseArray<>(viewSize > 0 ? viewSize : 10);
    }

    public <T extends View> T getView(@IdRes int id) {
        return get(id, null);
    }

    public <T extends View> T get(@IdRes int id, Class<T> viewType) {
        return ViewHolderHelper.get(itemView, mViews, id, viewType);
    }

    public int countView() {
        return mViews.size();
    }

    //------------------------------辅助方法---------------------------------------------------------------------
    public void setText(@IdRes int id, CharSequence text) {
        TextView view = getView(id);
        view.setText(text);
    }

    public void setOnClickListenr(@IdRes int id, View.OnClickListener linstener) {
        getView(id).setOnClickListener(linstener);
    }

    public void setOnLongClickListener(@IdRes int id, View.OnLongClickListener linstener) {
        getView(id).setOnLongClickListener(linstener);
    }
}
