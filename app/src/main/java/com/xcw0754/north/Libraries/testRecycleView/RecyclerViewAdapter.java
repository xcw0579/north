package com.xcw0754.north.Libraries.testRecycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.xcw0754.north.R;



/**
 * Created by xiao on 16-3-13.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

    private LayoutInflater mInflater;
    private Context mContxt;
    private int mDatas;
    private String name;    //文件夹名称


    public RecyclerViewAdapter(Context context, int datas, String name)  {
        this.mContxt = context;
        this.mDatas = datas;
        this.name = name + "/";
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return mDatas;
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int pos) {
        // 绑定ViewHolder
        String url = "http://10.0.3.2:5000/source/sort/product/"+ name+ pos+".jpg";
        Log.d("sort", url);
        Picasso.with(mContxt).load(url).into(holder.iv);
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup arg, int arg1) {
//        创建ViewHolder
        View view = mInflater.inflate(R.layout.tab_sort_recyclerview_item1, arg, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
    }
}



