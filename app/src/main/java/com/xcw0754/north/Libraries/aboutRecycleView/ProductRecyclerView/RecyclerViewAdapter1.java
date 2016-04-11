package com.xcw0754.north.Libraries.aboutRecycleView.ProductRecyclerView;

import android.animation.FloatArrayEvaluator;
import android.animation.IntArrayEvaluator;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;
import com.xcw0754.north.Libraries.SharedPreferences.SPUtils;
import com.xcw0754.north.Libraries.aboutRecycleView.TabSortRecyclerView.RecyclerViewAdapter;
import com.xcw0754.north.Libraries.aboutRecycleView.TabSortRecyclerView.RecyclerViewHolder;
import com.xcw0754.north.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by xiao on 16-3-13.
 * 从分类点击进来的页面，可以排序筛选。
 */
public class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerViewHolder1> implements View.OnClickListener {

    private LayoutInflater mInflater;
    private Context mContxt;
    private int mDatas;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;    //这是监听接口，下面定义了的
    private Handler dataCacheHandler;               //缓存数据用的
    private Handler UIHandler;                      //处理ui用的
    private ArrayList<RecyclerViewHolder1> rvhList;
    private ArrayList<oneProduct>  products;




    /**
     * 传产品的数量进来即可
     * @param context   上下文
     * @param datas     有多少个item，即几个产品
     */
    public RecyclerViewAdapter1(Context context, int datas)  {
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
                        //先请求并缓存所有数据
                        for (int pos=0; pos<mDatas; pos++) {
                            Message msg = new Message();
                            String finalUrl = "http://10.0.3.2:5000/ask/product/detail/0/" + pos;
                            String response = HttpRequest.get(finalUrl).body();
                            msg.what = pos;             //adapter的下标,也是产品编号(因为是首次load的)
                            msg.obj = response;
                            dataCacheHandler.sendMessage(msg);   // 丢一个what和obj给主线程处理
                        }
                    }
                };
                new Thread(requestTask).start();
            }
        });


        //更新ui
        UIHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                RecyclerViewHolder1 holder = rvhList.get(msg.what);
                Picasso.with(mContxt).load(products.get(msg.what).url).into(holder.iv);

                //收藏按钮: 必须实时check才行
                if( SPUtils.check(mContxt,"favorite", ""+products.get(msg.what).pos) ) {
                    //TODO 实心的照片
                    holder.favorite.setBackground(
                            mContxt.getResources().getDrawable(R.drawable.ic_heart_real));
                } else {
                    //TODO 这里的照片可能需要换成空心
                    holder.favorite.setBackground(
                            mContxt.getResources().getDrawable(R.drawable.ic_heart_blank));
                }
//                Log.d("msg", products.get(msg.what).pos + " ui价格的更新"+products.get(msg.what).price);

                holder.tv_price.setText(products.get(msg.what).price);
                holder.tv_title1.setText(products.get(msg.what).title1);
                holder.tv_title2.setText(products.get(msg.what).title2);
            }
        };

    }


    /**
     * 方便外部更新ui用的，相当于刷新
     * @param which
     */
    public void sendMessageToHanler(int which, final int pos) {
        if( which==1 ) {    //UI handler
            UIHandler.post(new Runnable() {   //handler并不是另开线程的
                @Override
                public void run() {
                    Runnable requestTask = new Runnable() {
                        @Override
                        public void run() {
                            while( products.get(pos)==null ) {
                                try{
                                    Thread.sleep(500);
                                }catch( InterruptedException e){
                                    Log.d("msg", "等待资源的ui线程被杀。");
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

        } else {        //可以搞其他事情

        }
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder1 holder, int pos) {


        //只需要将排序好的products跟holder对号入座即可。
        rvhList.set(pos, holder);
        holder.itemView.setTag(pos);
        holder.favorite.setTag(pos);
        holder.buyItNow.setTag(pos);
        holder.itemView.setOnClickListener(this);
        holder.favorite.setOnClickListener(this);
        holder.buyItNow.setOnClickListener(this);
        sendMessageToHanler(1, pos);

//        UIHandler.post(new Runnable() {   //handler并不是另开线程的
//            @Override
//            public void run() {
//                Runnable requestTask = new Runnable() {
//                    @Override
//                    public void run() {
//                        while( products.get(pos)==null ) {
//                            try{
//                                Thread.sleep(500);
//                                Log.d("msg", "真睡。");
//                            }catch( InterruptedException e){
//                                Log.d("msg", "等待资源的ui线程被杀。");
//                            }
//                        }
//                        Message msg = new Message();
//                        msg.what = pos;
//                        UIHandler.sendMessage(msg);
//                        Log.d("msg", "有执行。");
//                    }
//                };
//                new Thread(requestTask).start();
//            }
//        });
    }


    @Override
    public RecyclerViewHolder1 onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 创建ViewHolder
        View view = mInflater.inflate(R.layout.product_list_recyclerview_item1, arg0, false);
        RecyclerViewHolder1 viewHolder = new RecyclerViewHolder1(view);
        view.setOnClickListener(this);  //启动监听事件
        return viewHolder;
    }


    // 提供给外部设置listener用的，创建adapter的时候就要用得上。
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    // item的点击事件，仅需在外部调用setOnItemClickListener设置listener即可。
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {

//            Log.d("msg", "接收点击的是:"+v.getId());

            //TODO 这里应该取出item的什么数据才是有用的？
            switch(v.getId()) {
                case R.id.id_product_favorite:
                    Log.d("msg", "小按钮的tag是:"+v.getTag());
                    mOnItemClickListener.onFavoriteClick(v, v.getTag().toString());
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, v.getTag().toString());//传给下一个activity，这就是data
            }
        }
    }

    // 这是接口
    public static interface OnRecyclerViewItemClickListener {

        //整个item的点击事件
        void onItemClick(View view, String data);

        // item中收藏部件的点击
        void onFavoriteClick(View view, String data);
    }

























    /**
     * 排序标签一旦被点击，先调用此函数对数据进行排序，后再通知其刷新
     */
    public void changeOrder(int reg) {
        switch (reg) {
            case 3:
            default:
            case 0:
                Collections.sort(products, new CompSyn());
                break;
            case 1:
                Collections.sort(products, new CompSale());
                break;
            case 2:
                Collections.sort(products, new CompPrice());
                break;
        }
        for(int i=0; i<mDatas; i++){
            Log.d("msg", ""+products.get(i).price);
        }
        //因为不知道当前的位置，所以全部重新设一遍
        for (int i=0; i<rvhList.size(); i++) {
            notifyItemChanged(i);
        }
    }



    @Override
    public int getItemCount() {
        return mDatas;
    }

    //比较器：价格
    public class CompPrice implements Comparator<oneProduct> {
        public int compare(oneProduct s1, oneProduct s2) {
            Double p1 = Double.parseDouble(s1.price);
            Double p2 = Double.parseDouble(s2.price);
            if ( p1<p2 ) {  //小的排前面
                return 1;
            }else if ( p1>p2 ) {
                return -1;
            }
            return 0;
        }
    }

    //比较器:销量
    public class CompSale implements Comparator<oneProduct> {
        public int compare(oneProduct s1, oneProduct s2) {
            int ret = s1.title1.compareTo(s2.title1);
            if( ret>0 ){  //字典序
                return 1;
            }else if( ret<0 ){
                return -1;
            }else{
                return 0;
            }
        }

    }

    //比较器:综合
    public class CompSyn implements Comparator<oneProduct> {
        public int compare(oneProduct s1, oneProduct s2) {
            if( s1.pos < s2.pos ){  //小的排前面
                return 1;
            }else if( s1.pos > s2.pos ){
                return -1;
            }else{
                return 0;
            }
        }
    }


    // 为了排序功能，创建此类当作结构体，记录每个产品的信息
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














