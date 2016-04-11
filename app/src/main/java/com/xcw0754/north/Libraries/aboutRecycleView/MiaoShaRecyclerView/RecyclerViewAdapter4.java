package com.xcw0754.north.Libraries.aboutRecycleView.MiaoShaRecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.xcw0754.north.R;
import java.util.ArrayList;



/**
 * Created by xiao on 16-3-13.
 * 这是首页用的block1
 */
public class RecyclerViewAdapter4 extends RecyclerView.Adapter<RecyclerViewHolder4> {

    private LayoutInflater mInflater;
    private Context mContxt;

    private ArrayList<String> mDatas;


    /**
     * 传产品的链接url进来即可
     */
    public RecyclerViewAdapter4(Context context, ArrayList<String> datas)  {
        this.mContxt = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder4 holder, int pos) {
        Picasso.with(mContxt).load(mDatas.get(pos)).into(holder.iv);
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    @Override
    public RecyclerViewHolder4 onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 创建ViewHolder
        View view = mInflater.inflate(R.layout.home_block_item, arg0, false);
        RecyclerViewHolder4 viewHolder = new RecyclerViewHolder4(view);
        return viewHolder;
    }



}














