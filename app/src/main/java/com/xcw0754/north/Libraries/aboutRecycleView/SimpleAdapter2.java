package com.xcw0754.north.Libraries.aboutRecycleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xcw0754.north.R;

import java.util.List;

/**
 * Created by xiao on 16-3-13.
 * 用于recyclerview获取数据以及切换。（暂时没有用于项目中，用RecyclerViewAdapter和RecyclerViewHolder两个类代替了。）
 */

public class SimpleAdapter2 extends RecyclerView.Adapter<MyViewHolder2>{

    private LayoutInflater mInflater;
    private Context mContxt;
    private List<String> mDatas;


    public SimpleAdapter2(Context context, List<String> datas)  {
        this.mContxt = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {

        return mDatas.size();
    }


    @Override
    public void onBindViewHolder(MyViewHolder2 holder, int pos) {
//        绑定ViewHolder
        holder.tv.setText(mDatas.get(pos));
        Log.d("sort", "http://10.0.3.2:5000/source/sort/brand/b"+ (pos+1)+ ".jpg");
        Picasso.with(mContxt).load("http://10.0.3.2:5000/source/sort/brand/b"+ (pos+1)+ ".jpg").into(holder.iv);
    }


    @Override
    public MyViewHolder2 onCreateViewHolder(ViewGroup arg, int arg1) {
//        创建ViewHolder
        View view = mInflater.inflate(R.layout.tab_sort_recyclerview_item2, arg, false);
        MyViewHolder2 viewHolder = new MyViewHolder2(view);
        return viewHolder;
    }
}



class MyViewHolder2 extends RecyclerView.ViewHolder{

    TextView tv;
    ImageView iv;
    public MyViewHolder2(View arg) {
        super(arg);
        tv = (TextView) arg.findViewById(R.id.id_tv_item);
        iv = (ImageView) arg.findViewById(R.id.id_iv_item);
    }
}