package android.support.v7.widget;

/**
 * 由于字段访问权限问题，部分功能需要在同包下创见辅助类完成
 *
 * @hide Created by MrFeng on 2016/4/29.
 */
public class RecViewHolderUtil {
    /**
     * @hide
     */
    public static RecyclerView.ViewHolder changedMinorViewType(RecyclerView.ViewHolder holder, int mainTypeCount) {
        holder.mItemViewType -= mainTypeCount;
        return holder;
    }

    /**
     * @hide
     */
    public static RecyclerView.ViewHolder restoreMinorViewType(RecyclerView.ViewHolder holder, int mainTypeCount) {
        holder.mItemViewType += mainTypeCount;
        return holder;
    }
}
