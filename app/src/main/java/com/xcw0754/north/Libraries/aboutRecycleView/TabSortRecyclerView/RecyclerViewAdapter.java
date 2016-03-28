package com.xcw0754.north.Libraries.aboutRecycleView.TabSortRecyclerView;

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
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> implements View.OnClickListener {

    private LayoutInflater mInflater;
    private Context mContxt;
    private int mDatas;
    private String name;    //服务端的文件夹名称，路径自己补

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;    //这是接口，下面定义了


    public RecyclerViewAdapter(Context context, int datas, String name)  {
        this.mContxt = context;
        this.mDatas = datas;
        this.name = name + "/";
        mInflater = LayoutInflater.from(context);
    }

    //item的点击事件，仅需在外部调用setOnItemClickListener设置listener即可。
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //TODO 这里应该取出item的什么数据才是有用的？
            mOnItemClickListener.onItemClick(v, (String)v.getTag());
        }
    }

    // 提供给外部用的，创建adapter的时候就要用得上。
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }



    @Override
    public int getItemCount() {
        return mDatas;
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int pos) {
        // 绑定ViewHolder
        String url = "http://10.0.3.2:5000/source/sort/product/"+ name+ pos+".jpg";
        Picasso.with(mContxt).load(url).into(holder.iv);
        Log.d("sort", url);
        holder.itemView.setTag(name+pos);   //将数据保存在itemView的Tag中，以便点击时进行获取
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 创建ViewHolder
        View view = mInflater.inflate(R.layout.tab_sort_recyclerview_item1, arg0, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        view.setOnClickListener(this);  //监听事件
        return viewHolder;
    }




    // 类似listview的监听接口，仅需调用此函数即可
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , String data);
    }
}


