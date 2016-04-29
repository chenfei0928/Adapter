package com.mainli.adapterlib.recyclerView;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mainli.adapterlib.ItemViewCounter;

import java.util.List;

/**
 * 用于多种布局的RecyclerView 适配器
 * Created by Mainli on 2016/4/13.
 */
@SuppressWarnings("unused")
public abstract class RecyclerAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    @LayoutRes
    private final int[] mLayoutIds;
    private final int[] mViewSizes;
    private final RecyclerViewHolderFactory<VH> mViewHolderFactory;
    protected List<T> mData;

    /**
     * 若要使用此构造器，泛型第二个参数必须为{@link RecViewHolder}
     */
    public RecyclerAdapter(List<T> mList, @LayoutRes int layoutIds) {
        //noinspection unchecked
        this(mList, (RecyclerViewHolderFactory<VH>) new RecViewHolderFactory(), new int[]{layoutIds});
    }

    /**
     * 若要使用此构造器，泛型第二个参数必须为{@link RecViewHolder}
     */
    public RecyclerAdapter(List<T> mList, @LayoutRes int[] layoutIds) {
        //noinspection unchecked
        this(mList, (RecyclerViewHolderFactory<VH>) new RecViewHolderFactory(), layoutIds);
    }

    public RecyclerAdapter(List<T> mList, RecyclerViewHolderFactory<VH> creator, @LayoutRes int layoutIds) {
        this(mList, creator, new int[]{layoutIds});
    }

    public RecyclerAdapter(List<T> mList, RecyclerViewHolderFactory<VH> creator, @LayoutRes int[] layoutIds) {
        this.mData = mList;
        this.mLayoutIds = layoutIds;
        this.mViewHolderFactory = creator;
        this.mViewSizes = new int[mLayoutIds.length];
        for (int i = 0; i < this.mViewSizes.length; i++) {
            mViewSizes[i] = ItemViewCounter.viewSizeUndefined;
        }
    }

    public List<T> getList() {
        return mData;
    }

    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(T t) {
        if (mData.contains(t)) {
            remove(mData.indexOf(t));
        }
    }

    public void removeAll() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void resetData(List<T> mListData) {
        this.mData = mListData;
        notifyDataSetChanged();
    }

    public void addAll(List<T> data) {
        int size = mData.size();
        mData.addAll(data);
        notifyItemRangeInserted(size, data.size());
    }

    public void add(T t) {
        mData.add(t);
        notifyItemInserted(mData.size() - 1);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        /**
         * viewType 在父类中会自动处理
         * {@link android.support.v7.widget.RecyclerView.Adapter#createViewHolder(ViewGroup, int)}
         * {@code
         *      final VH holder = onCreateViewHolder(parent, viewType);
         *      holder.mItemViewType = viewType;
         * }
         */
        return mViewHolderFactory.createHolder(
                LayoutInflater.from(parent.getContext()).inflate(mLayoutIds[viewType], parent, false),
                mViewSizes[viewType]);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        this.onBindObject2View(holder, getItem(position), position);
        if (holder instanceof ItemViewCounter
                && mViewSizes[holder.getItemViewType()] == ItemViewCounter.viewSizeUndefined) {
            mViewSizes[holder.getItemViewType()] = ((ItemViewCounter) holder).countView();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mLayoutIds.length == 1)
            return 0;
        return getItemViewType(getItem(position), position);
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    /**
     * 如果{@link #mLayoutIds}长度只有1，此方法不会被调用
     * 否则必须重写，判断并返回LayoutId在数组中的下标
     */
    public int getItemViewType(T t, int position) {
        throw new UnsupportedOperationException("If {mLayoutIds.length > 1}" +
                " you must override getItemViewType(t, position) method" +
                " to return view type, in the layout ids array position.");
    }

    public abstract void onBindObject2View(VH vh, T t, int position);
}
