package com.xcw0754.north.Libraries.testRecycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xcw0754.north.R;

import java.util.List;

/**
 * Created by Administrator on 2016/3/4.
 */

public class SimpleAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private LayoutInflater mInflater;
    private Context mContxt;
    private List<String> mDatas;


    public SimpleAdapter(Context context, List<String> datas)  {
        this.mContxt = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int pos) {
//        绑定ViewHolder
        holder.tv.setText(mDatas.get(pos));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup arg, int arg1) {
//        创建ViewHolder
        View view = mInflater.inflate(R.layout.item_single_textview, arg, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }
}



class MyViewHolder extends RecyclerView.ViewHolder{

    TextView tv;
    public MyViewHolder(View arg) {
        super(arg);

        tv = (TextView) arg.findViewById(R.id.id_tv);
    }
}