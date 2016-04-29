package com.mainli.adapterlib.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * 简单的RecyclerView上拉加载监听器，由{@link OnRcvScrollListener} 简单修改而来
 * Created by Admin on 2016/1/19.
 */
public class RecyclerViewScrollLoadMoreListener extends RecyclerView.OnScrollListener {
    private final String TAG = "RecyclerViewScrollLoadMoreLis";
    private OnRecyclerViewLoadMoreListener lis;
    private boolean emptyAutoCall = true;

    public RecyclerViewScrollLoadMoreListener(boolean emptyAutoCall, OnRecyclerViewLoadMoreListener lis) {
        this.emptyAutoCall = emptyAutoCall;
        this.lis = lis;
    }

    public RecyclerViewScrollLoadMoreListener(OnRecyclerViewLoadMoreListener lis) {
        this(true, lis);
    }

    public interface OnRecyclerViewLoadMoreListener {
        void onLoadMore(RecyclerView lv);

        /**
         * 询问ListView的加载状态，空闲、载入更多或刷新中
         *
         * @param lv 触发上拉加载的ListView
         * @return 返回ListView的状态，值在本类中有静态常量
         */
        @ViewUtil.ListViewState
        int getListViewStatus(RecyclerView lv);
    }

    /**
     * 最后一个的位置，仅用于GridLayoutManager
     */
    private int[] mLastPositions;
    /**
     * 最后一个可见的item的位置
     */
    private int mLastVisibleItemPosition;

    /**
     * 第一个可见的item的位置
     */
    private int mFirstVisibleItemPosition;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE && lis != null
                && lis.getListViewStatus(recyclerView) == ViewUtil.STATUS_LIST_IDLE) {
            // 预计算RecyclerView内容数量
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            // 可见Item数量统计
            int visibleItemCount = layoutManager.getChildCount();
            // 总内容数量统计，等同于：recyclerView.getAdapter().getCount()
            int totalItemCount = layoutManager.getItemCount();
            // 获取或计算第一个和最后一个Item的Position
            calculateFirstVisibleItemPos(recyclerView);
            checkLoadMore(lis, recyclerView, mFirstVisibleItemPosition, visibleItemCount,
                    mLastVisibleItemPosition, totalItemCount);
        }
    }

    /**
     * 检查是否需要进行刷新并调用监听器
     *
     * @param lis              监听器
     * @param recyclerView     RecyclerView 视图对象
     * @param firstVisibleItem 第一个可见的Item
     * @param visibleItemCount 可见Item的总和
     * @param lastVisibleItem  最后一个可见的item
     * @param totalItemCount   适配器内总计有的item总和
     */
    private void checkLoadMore(@NonNull OnRecyclerViewLoadMoreListener lis, @NonNull RecyclerView recyclerView,
                               int firstVisibleItem, int visibleItemCount, int lastVisibleItem, int totalItemCount) {
//        Log.i(TAG, "first " + firstVisibleItem + " total " + totalItemCount + " last " + lastVisibleItem + " visible " + visibleItemCount);
        // 内容判空、内容一页之内显示完
        if (recyclerView.getAdapter() == null || totalItemCount == 0) {
            // 如果在内容为空时
            if (emptyAutoCall) {
                // 自动加载更多
                lis.onLoadMore(recyclerView);
            }
        } else if (firstVisibleItem == 0 && totalItemCount == visibleItemCount) {
            // 如果有数据，并且所有数据都在一屏幕内显示
            if (emptyAutoCall) {
                lis.onLoadMore(recyclerView);
            }
        } else if (visibleItemCount > 0 && (lastVisibleItem > totalItemCount - 5) || totalItemCount < 20) {
            // 如果最后一条在ListView中在最下方5行之内，或总数据不满5行，则进行加载
            lis.onLoadMore(recyclerView);
        }
    }

    /**
     * 计算第一个元素的位置
     */
    private void calculateFirstVisibleItemPos(RecyclerView rec) {
        if (rec.getChildCount() == 0) {
            mFirstVisibleItemPosition = 0;
            mLastVisibleItemPosition = 0;
            return;
        }
        RecyclerView.LayoutManager layoutManager = rec.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            mFirstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
        if (layoutManager instanceof GridLayoutManager) {
            mLastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            mFirstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            if (mLastPositions == null) {
                mLastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
            }
            mLastPositions = staggeredGridLayoutManager.findLastVisibleItemPositions(mLastPositions);
            mLastVisibleItemPosition = findMax(mLastPositions);
            staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(mLastPositions);
            mFirstVisibleItemPosition = findMin(mLastPositions);
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            max = Math.max(max, value);
        }
        return max;
    }

    private int findMin(int[] lastPositions) {
        int min = lastPositions[0];
        for (int value : lastPositions) {
            min = Math.min(min, value);
        }
        return min;
    }
}
