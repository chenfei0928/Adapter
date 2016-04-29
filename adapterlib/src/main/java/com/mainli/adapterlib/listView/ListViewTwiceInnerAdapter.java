package com.mainli.adapterlib.listView;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;

/**
 * 可以容纳两个内部实现适配器，实现当一个主适配器无内容时显示副适配器的内容
 * Created by MrFeng on 2015/12/10.
 */
public class ListViewTwiceInnerAdapter<Main extends ListAdapter, Minor extends ListAdapter>
        extends BaseAdapter implements Filterable {
    private Context context;
    @NonNull
    private Minor mMinor;
    private Main mMain;
    /**
     * 副结果的台头，为0则表示无台头
     */
    @LayoutRes
    private int mMinorHeader;


    public ListViewTwiceInnerAdapter(Context context, @NonNull Main mAdapter, @NonNull Minor minor, @LayoutRes int minorHeader) {
        this.context = context;
        this.mMain = mAdapter;
        this.mMinor = minor;
        this.mMinorHeader = minorHeader;
    }

    private boolean hasMainResult() {
        return !mMain.isEmpty();
    }

    private int getMinorHeaderOffset() {
        return mMinorHeader != 0 ? 1 : 0;
    }

    @SuppressWarnings("SimplifiableConditionalExpression")
    @Override
    public boolean areAllItemsEnabled() {
        return hasMainResult() ? mMain.areAllItemsEnabled() : false;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasMainResult()) {
            // 如果有主结果，仅显示主结果
            return mMain.getItemViewType(position);
        } else {
            // 没有主结果时，显示没有副结果提示，下方显示副适配器列表
            if (position == 0 && mMinorHeader != 0) {
                return mMain.getViewTypeCount();
            } else {
                return mMinor.getItemViewType(position) + 1 + mMain.getViewTypeCount();
            }
        }
    }

    @Override
    public int getViewTypeCount() {
        return mMinor.getViewTypeCount() + getMinorHeaderOffset() + mMain.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return !hasMainResult() && mMinor.isEmpty();
    }

    @Override
    public boolean isEnabled(int position) {
        if (hasMainResult()) {
            // 如果有主结果，仅显示主结果
            return mMain.isEnabled(position);
        } else {
            // 没有主结果时，显示没有主结果提示，下方显示副列表
            if (position == 0) {
                return false;
            } else {
                return mMinor.isEnabled(position);
            }
        }
    }

    @Override
    public boolean hasStableIds() {
        if (hasMainResult()) {
            // 如果有主结果，仅显示主结果
            return mMain.hasStableIds();
        } else {
            // 没有主结果时，显示没有主结果提示，下方显示副列表
            return mMinor.hasStableIds();
        }
    }

    @Override
    public int getCount() {
        if (hasMainResult()) {
            return mMain.getCount();
        } else {
            return mMinor.getCount() + getMinorHeaderOffset();
        }
    }

    @Override
    public Object getItem(int position) {
        if (hasMainResult()) {
            return mMain.getItem(position);
        } else {
            return position == 0 ? null : mMinor.getItem(position - getMinorHeaderOffset());
        }
    }

    @Override
    public long getItemId(int position) {
        if (hasMainResult()) {
            return mMain.getItemId(position);
        } else {
            return position == 0 ? -1 : mMinor.getItemId(position - getMinorHeaderOffset());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (hasMainResult()) {
            return mMain.getView(position, convertView, parent);
        } else {
            if (position == 0) {
                return genEmptyContentHint(convertView, parent);
            } else {
                return mMinor.getView(position - getMinorHeaderOffset(), convertView, parent);
            }
        }
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
        if (mMain != null) {
            mMain.registerDataSetObserver(observer);
        }
        mMinor.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
        if (mMain != null) {
            mMain.unregisterDataSetObserver(observer);
        }
        mMinor.unregisterDataSetObserver(observer);
    }

    @Override
    public Filter getFilter() {
        if (mMain instanceof Filterable) {
            return ((Filterable) mMain).getFilter();
        }
        return null;
    }

    private View genEmptyContentHint(View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(mMinorHeader, parent, false);
        }
        return convertView;
    }
}
