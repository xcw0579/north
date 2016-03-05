package com.xcw0754.north.Libraries.testRecycleView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xcw0754.north.R;

import java.util.ArrayList;
import java.util.List;

public class RecycleActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private SimpleAdapter mAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        initDatas();
        initViews();

        mAdapter = new SimpleAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);

//        设置recycleview布局管理
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); //两列
        mRecyclerView.setLayoutManager(gridLayoutManager);

//        设置分割线
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
    }

    private void initDatas() {
        //需要事先保存的数据，待展示出来的
        mDatas = new ArrayList<String>();

        for (int i = 0; i < 200; i++) {
            mDatas.add("热水器");
        }
    }

}
