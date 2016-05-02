package com.xcw0754.north.Libraries.aboutRecycleView.DeliveryRecyclerView;

import android.content.Context;
import android.content.IntentSender;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.xcw0754.north.Libraries.MultiSkill.OneProduct;
import com.xcw0754.north.Libraries.SharedPreferences.SPUtils;
import com.xcw0754.north.Libraries.aboutRecycleView.SearchRecyclerView.RecyclerViewHolder2;
import com.xcw0754.north.MyApp;
import com.xcw0754.north.R;

import java.util.ArrayList;


/**
 * Created by xiao on 16-3-13.
 * 这是待发货界面用的。
 */
public class RecyclerViewAdapter5 extends RecyclerView.Adapter<RecyclerViewHolder5> {

    private LayoutInflater mInflater;
    private Context mContxt;

    private Handler UIHandler;                      //处理ui用的

    private ArrayList<OneProduct> products;

    private ArrayList<RecyclerViewHolder5> rvhList;






    /**
     * 传产品的链接url进来即可
     */
    public RecyclerViewAdapter5(Context context, final ArrayList<OneProduct> products)  {

        this.mContxt = context;
        this.products = products;
        mInflater = LayoutInflater.from(context);
        rvhList = new ArrayList<>();
        for(int i=0; i<products.size(); i++) {
            rvhList.add(null);
        }



        //更新ui
        UIHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                RecyclerViewHolder5 holder = rvhList.get(msg.what);

                Picasso.with(mContxt).load(products.get(msg.what).url).noFade().into(holder.cb);    //图
                holder.price.setText(products.get(msg.what).price);
                holder.title1.setText(products.get(msg.what).title1);
                holder.title2.setText(products.get(msg.what).title2);
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
    public void onBindViewHolder(RecyclerViewHolder5 holder, int pos) {
        //这些得远程请求
        rvhList.set(pos, holder);
        sendMessage(pos);
    }



    @Override
    public int getItemCount() {
        return products.size();
    }


    @Override
    public RecyclerViewHolder5 onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 创建ViewHolder
        View view = mInflater.inflate(R.layout.delivery_item, arg0, false);
        RecyclerViewHolder5 viewHolder = new RecyclerViewHolder5(view);
        return viewHolder;
    }


}














