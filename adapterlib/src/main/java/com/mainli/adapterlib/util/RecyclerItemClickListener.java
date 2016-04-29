package com.mainli.adapterlib.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * RecyclerView的点击监听器
 * http://stackoverflow.com/questions/24471109/recyclerview-onclick
 * Created by Admin on 2016/4/7.
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mClick;
    private OnItemLongClickListener mLongClick;
    private GestureDetector mGestureDetector;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public RecyclerItemClickListener(Context context, final RecyclerView view,
                                     OnItemClickListener listener, OnItemLongClickListener longClick) {
        mClick = listener;
        mLongClick = longClick;
        mGestureDetector = new GestureDetector(context.getApplicationContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        View childView = view.findChildViewUnder(e.getX(), e.getY());

                        if (childView != null && mLongClick != null) {
                            mLongClick.onItemLongClick(childView, view.getChildAdapterPosition(childView));
                        }
                    }
                });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mClick != null && mGestureDetector.onTouchEvent(e)) {
            mClick.onItemClick(childView, view.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}