package com.xcw0754.north.Libraries.aboutRecycleView.SearchRecyclerView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.xcw0754.north.Libraries.SharedPreferences.SPUtils;
import com.xcw0754.north.Libraries.aboutRecycleView.SearchRecyclerView.RecyclerViewHolder2;
import com.xcw0754.north.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by xiao on 16-3-13.
 */
public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewHolder2> implements View.OnClickListener {

    private LayoutInflater mInflater;
    private Context mContxt;
    private int mDatas;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;    //这是监听接口，下面定义了的
    private Handler dataCacheHandler;               //缓存数据用的
    private Handler UIHandler;                      //处理ui用的

    private ArrayList<RecyclerViewHolder2> rvhList;
    private ArrayList<oneProduct> products;

    //选中的item
    private ArrayList<Integer>  checkList ;


    /**
     * 传产品的数量进来即可
     * @param context   上下文
     * @param datas     有多少个item，即几个产品
     */
    public RecyclerViewAdapter2(Context context, int datas)  {
        this.mContxt = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
        checkList = new ArrayList<>();

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
                            new oneProduct(msg.arg1,
                                    json.get("price").getAsString(),
                                    json.get("title1").getAsString(),
                                    json.get("title2").getAsString(),
                                    "http://10.0.3.2:5000/source/sort/itemlist/cfxd/0/"+ msg.arg1+".jpg"
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
                            JsonArray json = new JsonParser().parse(product).getAsJsonArray();
                            //从SPU里面取出产品编号，到服务端取数据，再存起来
                            for(int i=0; i<json.size(); i++) {
                                int pos = Integer.parseInt(json.get(i).getAsString());

                                Message msg = new Message();
                                String finalUrl = "http://10.0.3.2:5000/ask/product/detail/0/" + pos;
                                String response = HttpRequest.get(finalUrl).body();
                                msg.what = i;               //products的下标
                                msg.arg1 = pos;             //这是产品编号
                                msg.obj = response;         //这是该产品的数据
                                dataCacheHandler.sendMessage(msg);   // 丢一个what和obj给主线程处理
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
            public void handleMessage(Message msg) {
                RecyclerViewHolder2 holder = rvhList.get(msg.what);
                Picasso.with(mContxt).load(products.get(msg.what).url).into(holder.iv);
//                Log.d("msg", products.get(msg.what).pos + " ui价格的更新"+products.get(msg.what).price);
                holder.tv_price.setText(products.get(msg.what).price);
                holder.tv_title1.setText(products.get(msg.what).title1);
                holder.tv_title2.setText(products.get(msg.what).title2);
            }
        };
    }


    /**
     * 下面4个是为了批量选中收藏品而提供的接口。
     * 获取所有的选中的item，供清除/加入购物车用
     */
    public ArrayList<Integer> getCheckList() {
        return checkList;
    }

    public void replaceCheckList(ArrayList<Integer> array) {
        if( array==null )   return ;    //可能会清空，但不会null
        this.checkList = array;
    }

    public void putCheckList(int num) {
        if( num<0 ) return ;
        this.checkList.add(num);
    }

    public void delCheckList(int num) {
        if( num<0 ) return ;
        for(int i=0; i<checkList.size(); i++) {
            if(checkList.get(i)==num) {
                checkList.remove(i);
                return ;
            }
        }
    }


    public int posTonum(Integer pos) {
        if( pos<products.size() && pos>=0)
            return products.get(pos).pos;
        else
            return products.get(products.size()-1).pos;
    }







    /**
     * 方便外部更新ui用的，相当于刷新
     * @param which
     */
    public void sendMessageToHanler(int which, final int pos) {
        //函数名比较怪异，其实这里面还丢了很多东西，方便外部调用的，因为接口都差不多，你懂得。


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

        } else if( which==2 ) {        //删除item
            Log.d("msg", "发来的顺序是"+pos + "。而当前product的size是"+products.size());

            if( pos < products.size() ) {
                mDatas = mDatas - 1;    //这三个的size是保持一致的
                products.remove(pos);
                rvhList.remove(pos);
                Log.d("msg", "现在一共有" + mDatas + "个收藏品。  products的大小为"+products.size()+"rvhList的大小为"+rvhList.size());

                notifyItemRemoved(pos);
            }
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder2 holder, int pos) {
        //只需要将排序好的products跟holder对号入座即可。
//        Log.d("msg", "本次更新的是................... "+ pos + "而holder是" + holder.pos);

        if( pos>=rvhList.size() ) return;

        rvhList.set(pos, holder);
        holder.itemView.setTag(pos);
        holder.itemView.setOnClickListener(this);
        holder.cb_checkbox.setTag(pos);
        holder.cb_checkbox.setOnClickListener(this);

        sendMessageToHanler(1, pos);
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
    public RecyclerViewHolder2 onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 创建ViewHolder
        View view = mInflater.inflate(R.layout.product_store_recyclerview_item, arg0, false);
        RecyclerViewHolder2 viewHolder = new RecyclerViewHolder2(view);
        view.setOnClickListener(this);  //启动监听事件
        return viewHolder;
    }


    //item的点击事件，仅需在外部调用setOnItemClickListener设置listener即可。
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {

            switch(v.getId()) {
                case R.id.id_cb_store_product:
                    mOnItemClickListener.onCheckClick(v, v.getTag().toString());
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, v.getTag().toString());//传给下一个activity，这就是data
            }
        }
    }


    // 类似listview的监听接口，仅需调用此函数即可
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);

        // item中收藏部件的点击
        void onCheckClick(View view, String data);
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














