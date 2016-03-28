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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;
import com.xcw0754.north.Libraries.aboutRecycleView.TabSortRecyclerView.RecyclerViewAdapter;
import com.xcw0754.north.Libraries.aboutRecycleView.TabSortRecyclerView.RecyclerViewHolder;
import com.xcw0754.north.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by xiao on 16-3-13.
 */
public class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerViewHolder1> implements View.OnClickListener {

    private LayoutInflater mInflater;
    private Context mContxt;
    private int mDatas;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;    //这是监听接口，下面定义了的
    private Handler handler;                        //为了方便开线程后回传数据
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


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
//                RecyclerViewHolder1 holder = rvhList.get(msg.what);
                String response = (String) msg.obj;

                JsonObject json = new JsonParser().parse(response).getAsJsonObject();
                String errcode = json.get("errcode").getAsString();
                if ( errcode.equals("1002") ) {
//                    holder.tv_price.setText(json.get("price").getAsString());
//                    holder.tv_title1.setText(msg.what+json.get("title1").getAsString());
//                    holder.tv_title2.setText(json.get("title2").getAsString());
//                    holder.pos = msg.what;  //模拟产品的编号
                    products.set(msg.what,
                            new oneProduct(msg.what,
                                json.get("price").getAsString(),
                                json.get("title1").getAsString(),
                                json.get("title2").getAsString(),
                                "http://10.0.3.2:5000/ask/product/detail/0/"+msg.what+".jpg"
                    ));
                } else {
                    String errmsg = json.get("errmsg").getAsString();
                    Log.d("errmsg", errmsg);
                }
            }
        };


        // 装recyclerviewadapter的list先填空
        rvhList = new ArrayList<>();
        products = new ArrayList<>();
        for (int i=0; i<datas; i++) {
            rvhList.add(null);
            products.add(null);
        }

        //先缓存所有数据
        for (int pos=0; pos<mDatas; pos++) {
            final int finalPos = pos;
            handler.post(new Runnable() {   //handler并不是另开线程的
                @Override
                public void run() {
                    //have to 另开线程
                    Runnable requestTask = new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            String finalUrl = "http://10.0.3.2:5000/ask/product/detail/0/"+ finalPos;
                            String response = HttpRequest.get(finalUrl).body();
                            msg.what = finalPos;             //这个是array的adapter的下标
                            msg.obj = response;
                            handler.sendMessage(msg);   // 丢一个what和obj给主线程处理
                        }
                    };
                    new Thread(requestTask).start();
                }
            });
        }
    }



    /**
     * 排序标签一旦被点击，先调用此函数对数据进行排序，后再通知其刷新
     */
    public void changeOrder(int reg) {
        Log.d("msg", "确定调用了.");
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
    public void onBindViewHolder(final RecyclerViewHolder1 holder, final int pos) {

        //只需要将排序好的products跟holder对号入座即可。
        //TODO 这里可能需要使用到同步机制，或者说再弄一个helper？让新线程去睡眠等待资源？再提醒helper更新ui

        if (products.get(pos)!=null) {
            Picasso.with(mContxt)
                    .load("http://10.0.3.2:5000/source/sort/itemlist/cfxd/0/"+products.get(pos).pos+".jpg")
                    .into(holder.iv);
            holder.tv_price.setText(products.get(pos).price);
            holder.tv_title1.setText(products.get(pos).title1);
            holder.tv_title2.setText(products.get(pos).title2);
        }


//        // 绑定ViewHolder，先把图片load进去
//        String url = "http://10.0.3.2:5000/source/sort/itemlist/cfxd/0/"+ pos+".jpg";
//        Picasso.with(mContxt).load(url).into(holder.iv);
//        holder.itemView.setTag(pos);   //将产品编号绑在itemView的Tag中，以便点击时进行获取。
//
//        rvhList.set(pos, holder);
//        handler.post(new Runnable() {   //handler并不是另开线程的
//                @Override
//                public void run() {
//                    //have to 另开线程
//                    Runnable requestTask = new Runnable() {
//                        @Override
//                        public void run() {
//                            Message msg = new Message();
//                            String finalUrl = "http://10.0.3.2:5000/ask/product/detail/0/"+pos;
//                            String response = HttpRequest.get(finalUrl).body();
//                            msg.what = pos;             //这个是array的adapter的下标
//                            msg.obj = response;
//                            handler.sendMessage(msg);   // 丢一个what和obj给主线程处理
//                        }
//                    };
//                    new Thread(requestTask).start();
//                }
//            });
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














