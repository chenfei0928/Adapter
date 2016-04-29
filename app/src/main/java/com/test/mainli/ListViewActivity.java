package com.test.mainli;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.mainli.adapterlib.listView.AbstractBaseAdapter;
import com.mainli.adapterlib.listView.LvViewHolder;
import com.mainli.adapterlib.listView.LvViewHolderFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView 的演示
 * Created by Mainli on 2016/4/14.
 */
public class ListViewActivity extends AppCompatActivity {
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        mListView = (ListView) findViewById(R.id.listview);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            list.add("test:" + i);
        }
        mListView.setAdapter(new AbstractBaseAdapter<String, LvViewHolder>(list,
                new LvViewHolderFactory(), new int[]{R.layout.item}) {
            @Override
            public void getItemView(int position, LvViewHolder holder, String s) {
                TextView tv = holder.get(R.id.text);
                tv.setText(s);
                holder.get(R.id.text, TextView.class).setText(s);
            }
        });
    }
}
