package com.xcw0754.north.Libraries.aboutRecycleView.XiaoxiRecyclerView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.xcw0754.north.MyApp;
import com.xcw0754.north.R;

import java.util.ArrayList;


/**
 * Created by xiao on 16-3-13.
 * 这是待发货界面用的。
 */
public class RecyclerViewAdapter6 extends RecyclerView.Adapter<RecyclerViewHolder6> implements View.OnClickListener  {

    private LayoutInflater mInflater;
    private Context mContxt;

    private Handler UIHandler;                      //处理ui用的

    private int datas = 0;

    private ArrayList<RecyclerViewHolder6> rvhList;


    private OnRecyclerViewItemClickListener mOnItemClickListener = null;    //这是监听接口，下面定义了的



    /**
     * 传产品的链接url进来即可
     */
    public RecyclerViewAdapter6(Context context, int datas)  {

        this.mContxt = context;
        this.datas = datas;
        mInflater = LayoutInflater.from(context);
        rvhList = new ArrayList<>();
        for(int i=0; i<datas; i++) {
            rvhList.add(null);
        }


        //更新ui
        UIHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                RecyclerViewHolder6 holder = rvhList.get(msg.what);
                String url = "http://10.0.3.2:5000/source/xiaoxi/0.jpg";
                Picasso.with(mContxt).load(url).into(holder.iv);    //图

                String xx = MyApp.GetXiaoxi().get(0);   //可能只有一条
                if( xx.length()>20)
                    xx = xx.substring(0,20);

                holder.shor.setText(xx);
            }
        };

    }


    private void sendMessage(final int pos) {
        UIHandler.post(new Runnable() {   //handler并不是另开线程的
            @Override
            public void run() {
                Runnable requestTask = new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = pos;
                        UIHandler.sendMessage(msg);
                    }
                };
                new Thread(requestTask).start();
            }
        });
    }



    @Override
    public void onBindViewHolder(RecyclerViewHolder6 holder, int pos) {
        //这些得远程请求
        rvhList.set(pos, holder);
        sendMessage(pos);
    }



    @Override
    public int getItemCount() {
        return datas;
    }


    @Override
    public RecyclerViewHolder6 onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 创建ViewHolder
        View view = mInflater.inflate(R.layout.xiaoxi_item, arg0, false);
        RecyclerViewHolder6 viewHolder = new RecyclerViewHolder6(view);
        view.setOnClickListener(this);  //启动监听事件
        return viewHolder;
    }

















    // 提供给外部设置listener用的，创建adapter的时候就要用得上。
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }



    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, "1");
            Log.d("msg", "按下一次。");
        } else {
            Log.d("msg", "居然没有初始化到监听。");
        }
    }



    // 这是接口
    public static interface OnRecyclerViewItemClickListener {
        //整个item的点击事件
        void onItemClick(View view, String data);
    }


}














