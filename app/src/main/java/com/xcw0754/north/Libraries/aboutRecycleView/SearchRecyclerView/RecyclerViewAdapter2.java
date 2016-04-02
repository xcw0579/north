package com.xcw0754.north.Libraries.aboutRecycleView.SearchRecyclerView;

import android.content.Context;
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
import com.xcw0754.north.Libraries.SharedPreferences.SPUtils;
import com.xcw0754.north.Libraries.aboutRecycleView.ProductRecyclerView.RecyclerViewHolder1;
import com.xcw0754.north.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by xiao on 16-3-13.
 */
public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewHolder1> implements View.OnClickListener {

    private LayoutInflater mInflater;
    private Context mContxt;
    private int mDatas;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;    //这是监听接口，下面定义了的
    private Handler dataCacheHandler;               //缓存数据用的
    private Handler UIHandler;                      //处理ui用的

    private ArrayList<RecyclerViewHolder1> rvhList;
    private ArrayList<oneProduct> products;


    /**
     * 传产品的数量进来即可
     * @param context   上下文
     * @param datas     有多少个item，即几个产品
     */
    public RecyclerViewAdapter2(Context context, int datas)  {
        this.mContxt = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);


        // 装recyclerviewadapter的list先填空
        rvhList = new ArrayList<>();
        products = new ArrayList<>();
        for (int i=0; i<datas; i++) {
            rvhList.add(null);
            products.add(null);
        }


        //缓存数据
        dataCacheHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
//                RecyclerViewHolder1 holder = rvhList.get(msg.what);
                String response = (String) msg.obj;

                JsonObject json = new JsonParser().parse(response).getAsJsonObject();
                String errcode = json.get("errcode").getAsString();
                if ( errcode.equals("1002") ) {
                    products.set(msg.what,
                            new oneProduct(msg.what,
                                    json.get("price").getAsString(),
                                    json.get("title1").getAsString(),
                                    json.get("title2").getAsString(),
                                    "http://10.0.3.2:5000/source/sort/itemlist/cfxd/0/"+ msg.what+".jpg"
                            ));
                } else {
                    String errmsg = json.get("errmsg").getAsString();
                    Log.d("errmsg", errmsg);
                }
            }
        };

        dataCacheHandler.post(new Runnable() {   //handler并不是另开线程的
            @Override
            public void run() {
                //have to 另开一个线程
                Runnable requestTask = new Runnable() {
                    @Override
                    public void run() {

                        if( SPUtils.contains(mContxt, "favorite") ) {
                            String product = (String) SPUtils.get(mContxt, "favorite", "");
                            JsonObject json = new JsonParser().parse(product).getAsJsonObject();

                            int i=0;
                            while( true ) {
                                if( json.get(""+i)==null ) {
                                    break;
                                }
                                int pos = Integer.parseInt(json.get(""+i).getAsString());

                                Message msg = new Message();
                                String finalUrl = "http://10.0.3.2:5000/ask/product/detail/0/" + pos;
                                String response = HttpRequest.get(finalUrl).body();
                                msg.what = pos;             //adapter的下标
                                msg.obj = response;
                                dataCacheHandler.sendMessage(msg);   // 丢一个what和obj给主线程处理

                                i++;
                            }
                        }
                    }
                };
                new Thread(requestTask).start();
            }
        });


        //更新ui
        UIHandler = new Handler(){
            @Override
            public void handleMessage(final Message msg) {
                final RecyclerViewHolder1 holder = rvhList.get(msg.what);
                Picasso.with(mContxt).load(products.get(msg.what).url).into(holder.iv);
                holder.tv_price.setText(products.get(msg.what).price);
                holder.tv_title1.setText(products.get(msg.what).title1);
                holder.tv_title2.setText(products.get(msg.what).title2);

                // 收藏
                holder.favoriteFalse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //反转图标
                        holder.favoriteFalse.setVisibility(View.GONE);
                        holder.favoriteTrue.setVisibility(View.VISIBLE);

                        //TODO 将产品添加到收藏列表

                        //如果还没有任何收藏
                        if( !SPUtils.contains(mContxt, "favorite") ) {
                            JsonObject json = new JsonObject();
                            SPUtils.put(mContxt, "favorite", json.toString());
                        }

                        // 取出来解析，增加item，再装进去
                        String content = (String) SPUtils.get(mContxt, "favorite", "");
                        JsonArray json = new JsonParser().parse(content).getAsJsonArray();
                        json.add(""+msg.what);
                        SPUtils.put(mContxt, "favorite", json.toString());
                        //TODO  item要从recyclerview增加

                    }
                });

                // 取消收藏
                holder.favoriteTrue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //反转图标
                        holder.favoriteFalse.setVisibility(View.VISIBLE);
                        holder.favoriteTrue.setVisibility(View.GONE);

                        // 取出来解析，删除掉what，再装回去
                        String content = (String) SPUtils.get(mContxt, "favorite", "");
                        JsonArray json = new JsonParser().parse(content).getAsJsonArray();
                        for(int i=0; i<json.size(); i++) {
                            if (json.get(i).getAsString() == "" + msg.what) {
                                json.remove(i);     //找到即删除
                                products.remove(i);
                                //TODO  item要从recyclerview删除
                                break;
                            }
                        }
                        SPUtils.put(mContxt, "favorite", json.toString());  //放回
                    }
                });
            }
        };
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder1 holder, final int pos) {
        //只需要将排序好的products跟holder对号入座即可。
        rvhList.set(pos, holder);
        UIHandler.post(new Runnable() {   //handler并不是另开线程的
            @Override
            public void run() {
                Runnable requestTask = new Runnable() {
                    @Override
                    public void run() {
                        while ( products.get(pos)==null ) {
                            try{
                                Thread.sleep(500);
                            }catch( InterruptedException e){
                            }
                        }
                        Message msg = new Message();
                        msg.what = pos;
                        UIHandler.sendMessage(msg);
                    }
                };
                new Thread(requestTask).start();
            }
        });
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
    public RecyclerViewHolder1 onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 创建ViewHolder
        View view = mInflater.inflate(R.layout.product_list_recyclerview_item1, arg0, false);
        RecyclerViewHolder1 viewHolder = new RecyclerViewHolder1(view);
        view.setOnClickListener(this);  //启动监听事件
        return viewHolder;
    }

    //item的点击事件，仅需在外部调用setOnItemClickListener设置listener即可。
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //TODO 这里应该取出item的什么数据才是有用的？
            mOnItemClickListener.onItemClick(v, (String)v.getTag());//传给下一个activity
        }
    }

    // 类似listview的监听接口，仅需调用此函数即可
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }


    // 为了缓存数据
    public class oneProduct {
        public int pos;     //在后台的名称
        public String price;
        public String title1;
        public String title2;
        public String url;

        public oneProduct(int pos, String price, String title1, String title2, String url) {
            this.pos = pos;
            this.price = price;
            this.title1 = title1;
            this.title2 = title2;
            this.url = url;
        }
    }
}














