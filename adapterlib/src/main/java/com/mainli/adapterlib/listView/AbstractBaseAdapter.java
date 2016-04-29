package com.mainli.adapterlib.listView;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mainli.adapterlib.ViewHolderCreator;
import com.mainli.adapterlib.recyclerView.RViewHolder;

import java.util.List;

/**
 * 基类adapter
 */
@SuppressWarnings("unused")
public abstract class AbstractBaseAdapter<T, VH extends ViewHolder> extends BaseAdapter {
    @LayoutRes
    private final int[] mLayoutIds;
    private final int[] mViewSizes;
    protected List<T> mListData;
    private ViewHolderCreator<VH> mCreator;

//    public AbstractBaseAdapter(List<T> mListData, @LayoutRes int layoutId) {
//        this(mListData, ListViewHolderCreator.newInstance(), new int[]{layoutId});
//    }

//    public AbstractBaseAdapter(List<T> mListData, @LayoutRes int[] layoutIds) {
//        this(mListData, ListViewHolderCreator.newInstance(), layoutIds);
//    }

    public AbstractBaseAdapter(List<T> mListData, ViewHolderCreator<VH> creator, @LayoutRes int layoutId) {
        this(mListData, creator, new int[]{layoutId});
    }

    public AbstractBaseAdapter(List<T> mListData, ViewHolderCreator<VH> creator, @LayoutRes int[] layoutIds) {
        this.mListData = mListData;
        this.mLayoutIds = layoutIds;
        this.mCreator = creator;
        this.mViewSizes = new int[mLayoutIds.length];
        for (int i = 0; i < this.mViewSizes.length; i++) {
            mViewSizes[i] = ViewHolder.viewSizeUndefined;
        }
    }

    public List<T> getList() {
        return mListData;
    }

    public void remove(int position) {
        mListData.remove(position);
        notifyDataSetChanged();
    }

    public void remove(T t) {
        if (mListData.contains(t)) {
            mListData.remove(t);
            notifyDataSetChanged();
        }
    }

    public void removeAll() {
        mListData.clear();
        notifyDataSetChanged();
    }

    public void resetData(List<T> mListData) {
        this.mListData = mListData;
        notifyDataSetChanged();
    }

    public void addAll(List<T> data) {
        mListData.addAll(data);
        notifyDataSetChanged();
    }

    public void add(T t) {
        mListData.add(t);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public int getViewTypeCount() {
        return mLayoutIds.length;
    }

    @Override
    public int getItemViewType(int position) {
        if (mLayoutIds.length == 1)
            return 0;
        return getItemViewType(getItem(position), position);
    }

    @Override
    public T getItem(int position) {
        if (position > mListData.size()) {
            return null;
        }
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        VH vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(mLayoutIds[viewType], parent, false);
            vh = mCreator.createHolder(convertView, mViewSizes[viewType], viewType);
            convertView.setTag(vh);
        } else {
            //noinspection unchecked
            vh = (VH) convertView.getTag();
        }
        getItemView(position, vh, getItem(position));
        // 如果没有缓存到其ViewSize，将其缓存
        if (mViewSizes[viewType] == ViewHolder.viewSizeUndefined) {
            mViewSizes[viewType] = vh.countView();
        }
        return convertView;
    }

    /**
     * 如果{@link #mLayoutIds}长度只有1，此方法不会被调用
     * 否则必须重写，判断并返回LayoutId在数组中的下标
     */
    public int getItemViewType(T t, int position) {
        throw new UnsupportedOperationException("If {mLayoutIds.length > 1} you must override getItemViewType(t, position) method to return view type, in the layout ids array position.");
    }

    public abstract void getItemView(int position, VH holder, T t);
}
