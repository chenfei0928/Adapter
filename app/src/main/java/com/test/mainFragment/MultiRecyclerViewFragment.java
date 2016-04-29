package com.test.mainFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mainli.adapterlib.recyclerView.RecViewHolder;
import com.mainli.adapterlib.recyclerView.RecViewHolderFactory;
import com.mainli.adapterlib.recyclerView.RecyclerAdapter;
import com.test.mainli.R;

import java.util.Arrays;
import java.util.List;

/**
 * 多 ViewType 的 RecyclerView 示例
 * Created by MrFeng on 2016/4/15.
 */
public class MultiRecyclerViewFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<String> list = Arrays.asList("1", "2", "3", "1", "2", "3", "1", "2", "3", "1", "2", "3", "1", "2",//
                "3", "1", "2", "3", "1", "2", "3", "1", "2", "3", "1", "2", "3", "1", "2", "3", "1", "2", "3",//
                "1", "2", "3", "1", "2", "3", "1", "2", "3", "1", "2", "3");
        RecyclerAdapter<String, RecViewHolder> adapter = new RecyclerAdapter<String, RecViewHolder>(
                list, new RecViewHolderFactory(), new int[]{R.layout.item1, R.layout.item}) {
            @Override
            public int getItemViewType(String s, int position) {
                return position % 2;
            }

            @Override
            public void onBindObject2View(RecViewHolder vh, String s, int position) {
                vh.get(R.id.text, TextView.class).setText(s);
            }
        };
        ((RecyclerView) view).setAdapter(adapter);
    }
}
