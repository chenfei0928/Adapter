package com.mainli.adapterlib.util;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * View设置工具类
 * Created by MrFeng on 2015/8/19.
 */
@SuppressWarnings("unused")
public class ViewUtil {
    private static final String TAG = "ViewUtil";

    @IntDef({STATUS_LIST_IDLE, STATUS_LIST_LOADMORE, STATUS_LIST_REFRESH, STATUS_LIST_DONT_LOADMORE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ListViewState {
    }

    public static final int STATUS_LIST_IDLE = 0;
    public static final int STATUS_LIST_REFRESH = 1;
    public static final int STATUS_LIST_LOADMORE = 2;
    // 标记：请勿加载
    public static final int STATUS_LIST_DONT_LOADMORE = 3;

    /**
     * 移除上拉加载
     *
     * @param lv            要移除上拉加载的ListView
     * @param addLoadNoMore 是否要添加无更多的提示
     */
    public static void removeFooterView(ListView lv, boolean addLoadNoMore) {
        View v = getLoadMoreFooter(lv);
        if (v != null) {
            Log.d(TAG, "为 " + lv.getClass().getName() + " " + getViewIdResString(lv) + " 移除底部 FooterView 和滑动监听器");
            if (!addLoadNoMore) {
                lv.removeFooterView(v);
                // 如果不需要添加无更多的提示，则为ListView移除FooterView之后移除其Tag
                // 否则这个View将会在再次添加加载更多时使用
                lv.setTag(null);
            }
            OnScrollLisSet onScrollLisSet = genListViewScrollListeners(lv);
            onScrollLisSet.removeScrollLoadMoreListener();
        }
    }

    /**
     * 为ListView添加FootView方式添加加载中提示
     * 如果ListView已经有FooterView则可能是已经执行过该方法，添加过FooterView
     * 即只为没有FooterView的ListView添加滑动加载
     * <p>
     * 将上拉刷新的监听器放在footerView的Tag中
     * 并将footerView放在ListView中以备移除监听器使用
     *
     * @param lv               要添加上拉加载的ListView对象
     * @param loadingLayoutRes 加载中的FooterView布局文件
     * @param lis              上拉加载监听器
     * @param emptyAutoLoad    在列表为空或列表不满一屏时是否加载更多
     */
    public static View addScrollLoadMore(final ListView lv, @LayoutRes int loadingLayoutRes,
                                         final OnListViewLoadMoreListener lis, boolean emptyAutoLoad) {
        View footer = getLoadMoreFooter(lv);
        OnScrollLisSet onScrollLisSet = genListViewScrollListeners(lv);
        if (onScrollLisSet.containsScrollLoadMore()) {
            Log.w(TAG, "ListView " + getViewIdResString(lv) + " 中已经有上拉加载了 " + footer);
            return footer;
        }

        // 获取或Inflate FooterView
        // 如果之前有设置移除监听器，但是保留加载无更多提示，则不重新InflateView，将其提示文字更新复用
        if (footer == null) {
            footer = View.inflate(lv.getContext(), loadingLayoutRes, null);
        }

        // 获取或新建监听器
        AbsListView.OnScrollListener listener;
        Object tag = footer.getTag();
        if (tag != null && tag instanceof ScrollLoadMoreListener) {
            listener = (AbsListView.OnScrollListener) footer.getTag();
        } else {
            listener = new ScrollLoadMoreListener(lv, lis, emptyAutoLoad);
            // 此处的FooterView的Tag，为上拉加载更多的监听器
            footer.setTag(onScrollLisSet);
        }

        // 添加FooterView和监听器
        lv.addFooterView(footer, TAG, false);
        lv.setTag(footer);
        onScrollLisSet.addOnScrollListener(listener);
        return footer;
    }

    public interface OnListViewLoadMoreListener {
        void onLoadMore(ListView lv);

        /**
         * 询问ListView的加载状态，空闲、载入更多或刷新中
         *
         * @param lv 触发上拉加载的ListView
         * @return 返回ListView的状态，值在本类中有静态常量
         */
        @ListViewState
        int getListViewStatus(ListView lv);
    }

    /**
     * 通过ListView的Tag或反射获取其FooterView
     *
     * @param lv 添加过加载更多的 ListView
     * @return 加载更多的itemView
     */
    private static View getLoadMoreFooter(ListView lv) {
        View v = getLoadMoreFooterByTag(lv);
        if (v == null)
            v = getLoadMoreFooterByReflect(lv);
        return v;
    }

    private static View getLoadMoreFooterByTag(ListView lv) {
        if (lv.getTag() != null && lv.getTag() instanceof View) {
            View footer = (View) lv.getTag();
            // 上拉加载提示的FooterView，其Tag为上拉加载以加载更多的监听器
            if (footer.getTag() != null && footer.getTag() instanceof ScrollLoadMoreListener)
                return footer;
        }
        return null;
    }

    private static View getLoadMoreFooterByReflect(ListView lv) {
        try {
            Field mFooterViewInfos = ListView.class.getDeclaredField("mFooterViewInfos");
            mFooterViewInfos.setAccessible(true);
            Object o = mFooterViewInfos.get(lv);
            if (o != null && o instanceof List) {
                @SuppressWarnings("unchecked") List<ListView.FixedViewInfo> list = (List<ListView.FixedViewInfo>) o;
                for (ListView.FixedViewInfo info : list)
                    if (TAG.equals(info.data))
                        return info.view;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过反射获取ListView的OnScrollListener监听器，并包装为监听器集合使用
     *
     * @param lv 要获取或生成监听器的ListView
     * @return 返回生成的滑动监听器集合
     */
    public static OnScrollLisSet genListViewScrollListeners(AbsListView lv) {
        OnScrollLisSet lisSet = null;
        Field mOnScrollListener = null;
        // 获取字段反射，以用来设置监听器，不使用ListView子类继承方式获取是因为字段是private，防止像 XListView 之类
        // 继承重写 setOnScrollListener 方法，自身监听并转发保存监听器，与本监听器集合转发执行监听器冲突，导致的堆栈溢出异常
        try {
            mOnScrollListener = AbsListView.class.getDeclaredField("mOnScrollListener");
            mOnScrollListener.setAccessible(true);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "NoSuchFieldException", e);
        }
        // 先从ListView的Tag中获取，用于由本类添加的上拉加载使用
        if (lv.getTag() instanceof OnScrollLisSet)
            lisSet = (OnScrollLisSet) lv.getTag();
        else if (lv.getTag() instanceof View && ((View) lv.getTag()).getTag() instanceof OnScrollLisSet)
            lisSet = (OnScrollLisSet) ((View) lv.getTag()).getTag();
        else if (mOnScrollListener != null) {
            try {
                // 无法从Tag中获取，使用反射从对象中获取
                AbsListView.OnScrollListener listener = (AbsListView.OnScrollListener) mOnScrollListener.get(lv);
                // 获取到之后判断其类型，如果不为监听器集合则新建一个监听器集合并将获取到的监听器存入集合中
                if (listener != null && listener instanceof OnScrollLisSet) {
                    lisSet = (OnScrollLisSet) listener;
                } else {
                    lisSet = new OnScrollLisSet();
                    if (listener != null)
                        lisSet.addOnScrollListener(listener);
                }
            } catch (IllegalAccessException e) {
                Log.e(TAG, "IllegalAccessException", e);
            } catch (NullPointerException e) {
                Log.e(TAG, "NullPointerException", e);
            }
        }
        if (lisSet == null)
            lisSet = new OnScrollLisSet();
        // 将获取到的监听器集合通过反射设置进ListView对象中并执行监听器，使用反射方式实现，以避免XListView问题
        if (mOnScrollListener != null) {
            try {
                mOnScrollListener.set(lv, lisSet);

                Method invokeOnItemScrollListener = AbsListView.class.getDeclaredMethod("invokeOnItemScrollListener");
                invokeOnItemScrollListener.setAccessible(true);
                invokeOnItemScrollListener.invoke(lv);
            } catch (IllegalAccessException e) {
                Log.e(TAG, "NullPointerException", e);
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "NoSuchMethodException", e);
            } catch (InvocationTargetException e) {
                Log.e(TAG, "InvocationTargetException", e);
            }
        }
        return lisSet;
    }

    public static class OnScrollLisSet implements AbsListView.OnScrollListener {
        private final Set<AbsListView.OnScrollListener> listeners = new HashSet<>();

        /*package*/ OnScrollLisSet() {
            // 构造器只会被在ViewUtil这个外部类中被使用
        }

        public void addOnScrollListener(AbsListView.OnScrollListener lis) {
            if (this != lis) {
                if (lis instanceof OnScrollLisSet) {
                    for (AbsListView.OnScrollListener l : ((OnScrollLisSet) lis).listeners)
                        addOnScrollListener(l);
                } else {
                    if (!listeners.contains(lis))
                        listeners.add(lis);
                }
            }
        }

        public void removeOnScrollListener(AbsListView.OnScrollListener lis) {
            listeners.remove(lis);
        }

        private boolean containsScrollLoadMore() {
            for (AbsListView.OnScrollListener listener : listeners) {
                if (listener instanceof ScrollLoadMoreListener)
                    return true;
            }
            return false;
        }

        private void removeScrollLoadMoreListener() {
            Iterator<AbsListView.OnScrollListener> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                AbsListView.OnScrollListener next = iterator.next();
                if (next instanceof ScrollLoadMoreListener) {
                    iterator.remove();
                    break;
                }
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            for (AbsListView.OnScrollListener lis : listeners)
                lis.onScrollStateChanged(view, scrollState);
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            for (AbsListView.OnScrollListener lis : listeners)
                lis.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    private static class ScrollLoadMoreListener implements AbsListView.OnScrollListener {
        private ListView lv;
        private OnListViewLoadMoreListener lis;
        private boolean needRefresh = false;
        private boolean emptyAutoCall = true;

        private ScrollLoadMoreListener(ListView lv, OnListViewLoadMoreListener lis, boolean emptyAutoCall) {
            this.lv = lv;
            this.lis = lis;
            this.emptyAutoCall = emptyAutoCall;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // 不需要刷新时，直接 return
            if (!needRefresh)
                return;
            // 滑动状态结束时去网络请求数据刷新
            boolean touchIsIdle = scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
            boolean canRefresh = lis.getListViewStatus(lv) == STATUS_LIST_IDLE;
            if (touchIsIdle && lis != null && canRefresh) {
                Log.i(TAG, view.getClass().getName() + " 上拉加载：" + getViewIdResString(view));
                lis.onLoadMore(lv);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            // 如果最后一条在ListView中在最下方5行之内，或总数据不满5行，则进行加载
            needRefresh = (firstVisibleItem + visibleItemCount > totalItemCount - 5) || totalItemCount <= 21;
            // 内容判空、内容一页之内显示完
            if (lv.getAdapter() == null || lv.getAdapter().isEmpty()) {
                // 如果在内容为空时
                if (emptyAutoCall && lis != null && lis.getListViewStatus(lv) == STATUS_LIST_IDLE) {
                    // 自动加载更多
                    lis.onLoadMore(lv);
                }
            } else if (firstVisibleItem == 0 && totalItemCount == visibleItemCount && lis.getListViewStatus(lv) == STATUS_LIST_IDLE) {
                // 如果有数据，并且所有数据都在一屏幕内显示，onScrollStateChanged不会被调用，则立即去请求数据
                if (emptyAutoCall)
                    lis.onLoadMore(lv);
            }
        }
    }

    /**
     * 通过API获得屏幕方向，如果是横屏或主动设置了屏幕方向为横屏的话，返回true
     * SCREEN_ORIENTATION_PORTRAIT 为竖屏
     * SCREEN_ORIENTATION_LANDSCAPE 为横屏
     * 正方形返回竖屏、false
     */
    public static boolean isDisplayLandscape(Activity activity) {
        // 取得当前屏幕方向
        int orient = activity.getRequestedOrientation();
        // 若非明确的landscape或portrait时 再透过宽高比例的方法来确认实际显示方向
        // 这会保证orient最终值会是明确的横屏landscape或竖屏portrait
        if (orient != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                && orient != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            // 宽>高为横屏,反正为竖屏
            Point point = new Point();
            if (Build.VERSION.SDK_INT > 17)
                activity.getWindowManager().getDefaultDisplay().getRealSize(point);
            else if (Build.VERSION.SDK_INT >= 13)
                activity.getWindowManager().getDefaultDisplay().getSize(point);
            else {
                // 3.2 以下时此方法没有测试过
                DisplayMetrics dm = activity.getResources().getDisplayMetrics();
                point.set(dm.widthPixels, dm.heightPixels);
            }
            Log.i(TAG, "主屏幕长 " + point.x + " 高 " + point.y);
            orient = point.x > point.y ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
        Log.i(TAG, orient == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ? "横屏 true" : "竖屏或正方形 false");
        return orient == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    private static String getViewIdResString(View v) {
        StringBuilder out = new StringBuilder();
        final int id = v.getId();
        if (id != View.NO_ID) {
            out.append(" #");
            out.append(Integer.toHexString(id));
            final Resources r = v.getContext().getResources();
            try {
                String pkgname;
                switch (id & 0xff000000) {
                    case 0x7f000000:
                        pkgname = "app";
                        break;
                    case 0x01000000:
                        pkgname = "android";
                        break;
                    default:
                        pkgname = r.getResourcePackageName(id);
                        break;
                }
                String typename = r.getResourceTypeName(id);
                String entryname = r.getResourceEntryName(id);
                out.append(" ");
                out.append(pkgname);
                out.append(":");
                out.append(typename);
                out.append("/");
                out.append(entryname);
            } catch (Resources.NotFoundException e) {
                out.append(v.toString());
            }
        }
        return out.toString();
    }
}
