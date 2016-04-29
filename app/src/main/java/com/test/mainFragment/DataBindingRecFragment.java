package com.test.mainFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mainli.adapterlib.recyclerView.RecDataBindingViewHolder;
import com.mainli.adapterlib.recyclerView.RecDataBindingViewHolderFactory;
import com.mainli.adapterlib.recyclerView.RecyclerAdapter;
import com.test.mainli.BR;
import com.test.mainli.R;

import java.util.Arrays;
import java.util.List;

/**
 * 适用于 DataBinding 的 RecyclerView 示例
 * Created by MrFeng on 2016/4/29.
 */
public class DataBindingRecFragment extends Fragment {
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
        RecyclerAdapter<String, RecDataBindingViewHolder> adapter = new RecyclerAdapter<String, RecDataBindingViewHolder>(
                list, new RecDataBindingViewHolderFactory(), R.layout.databinding_item) {
            @Override
            public void onBindObject2View(RecDataBindingViewHolder vh, String s, int position) {
                vh.getDataBinding().setVariable(BR.str, s);
            }
        };
        ((RecyclerView) view).setAdapter(adapter);
    }
}
