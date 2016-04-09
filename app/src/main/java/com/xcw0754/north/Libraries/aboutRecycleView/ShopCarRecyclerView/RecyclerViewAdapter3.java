package com.xcw0754.north.Libraries.aboutRecycleView.ShopCarRecyclerView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
public class RecyclerViewAdapter3 extends RecyclerView.Adapter<RecyclerViewHolder3> implements View.OnClickListener {

    private LayoutInflater mInflater;
    private Context mContxt;
    private int mDatas;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;    //这是监听接口，下面定义了的
    private Handler dataHandler;               //缓存数据用的
    private Handler UIHandler2;                      //处理ui用的

    private ArrayList<RecyclerViewHolder3> rvhList;
    private ArrayList<oneProduct> products;

    //选中的item
    private ArrayList<oneProduct>  checkList;


    /**
     * 传产品的数量进来即可
     * @param context   上下文
     * @param datas     有多少个item，即几个产品
     */
    public RecyclerViewAdapter3(Context context, int datas)  {
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
        dataHandler = new Handler(){
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

        dataHandler.post(new Runnable() {   //handler并不是另开线程的
            @Override
            public void run() {
                //have to 另开一个线程
                Runnable requestTask = new Runnable() {
                    @Override
                    public void run() {
                        SPUtils.check(mContxt, "car", "");
                        String product = (String) SPUtils.get(mContxt, "car", "");
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
                            dataHandler.sendMessage(msg);   // 丢一个what和obj给主线程处理
                        }

                    }
                };
                new Thread(requestTask).start();
            }
        });


        //更新ui
        UIHandler2 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                RecyclerViewHolder3 holder = rvhList.get(msg.what);
                Picasso.with(mContxt).load(products.get(msg.what).url).into(holder.iv);
//                Log.d("msg", products.get(msg.what).pos + " ui价格的更新"+products.get(msg.what).price);
                holder.tv_price.setText(products.get(msg.what).price);
                holder.tv_title1.setText(products.get(msg.what).title1);
                holder.tv_title2.setText(products.get(msg.what).title2);

                holder.itemView.setTag(products.get(msg.what)); //直接丢一个对象进去
                holder.cb_checkbox.setTag(products.get(msg.what));
                Log.d("msg", "更新啦"+msg.what);
            }
        };
    }


    public void deleteAllChecked() {
        //将所有选中的物品从收藏夹删除
        //搜出选中的物品，添加到购物车中。
        Log.d("msg", "即将删除的顺序有"+checkList.toString());
        if( checkList.size()<=0 ) {
            Toast.makeText(mContxt, "未选中任何物品", Toast.LENGTH_LONG).show();
            return ;    //没有任何选中
        }

        Collections.sort(checkList, new Comp());
        for(int i=checkList.size()-1; i>=0; i--) {      //要按holder逆序
            SPUtils.checkOut(mContxt, "car", String.valueOf(checkList.get(i).pos));
            //先找出来，再通知其更新ui
            for(int j=0; j<products.size(); j++) {
                if( products.get(j).pos==checkList.get(i).pos ) {
                    sendMessageToHanler(2, j);    //这个得记录它的adapter的下标啊，不是产品编号啊
                    break;
                }
            }
        }
        checkList=new ArrayList<>();
        //解决bug：删除后部分的item竟然还是打勾的
        for(int i=0; i<rvhList.size(); i++) {
            rvhList.get(i).cb_checkbox.setSelected(false);
        }
    }

    public void putCheckList(Object obj) {
        oneProduct pro = (oneProduct) obj;
        for(int i=0; i<checkList.size(); i++){
            if(checkList.get(i).pos==pro.pos)   return;
        }
        checkList.add(pro);
        Log.d("msg", "新增了"+pro.pos);
    }

    public void delCheckList(Object obj) {
        oneProduct pro = (oneProduct) obj;
        for(int i=0; i<checkList.size(); i++) {
            if(checkList.get(i).pos==pro.pos) {
                Log.d("msg", "删除"+pro.pos);
                checkList.remove(i);
                return ;
            }
        }
    }





    /**
     * 方便外部更新ui用的，相当于刷新
     * @param which
     */
    public void sendMessageToHanler(int which, final int pos) {
        //函数名比较怪异，其实这里面还丢了很多东西，方便外部调用的，因为接口都差不多，你懂得。
        if( which==1 ) {    //UI handler
            UIHandler2.post(new Runnable() {   //handler并不是另开线程的
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
                            UIHandler2.sendMessage(msg);
                        }
                    };
                    new Thread(requestTask).start();
                }
            });

        } else if( which==2 ) {        //删除item

            if( pos < products.size() ) {
                mDatas = mDatas - 1;    //这三个的size是保持一致的
                products.remove(pos);
                rvhList.remove(pos);
                Log.d("msg", "删除后，现在一共有" + mDatas + "个收藏品。");
                notifyItemRemoved(pos);
            }
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder3 holder, int pos) {
        Log.d("msg", "绑定的是"+pos);
        //只需要将排序好的products跟holder对号入座即可。
        rvhList.set(pos, holder);
        holder.itemView.setOnClickListener(this);
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
    public RecyclerViewHolder3 onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 创建ViewHolder
        View view = mInflater.inflate(R.layout.product_store_recyclerview_item, arg0, false);
        RecyclerViewHolder3 viewHolder = new RecyclerViewHolder3(view);
        view.setOnClickListener(this);  //启动监听事件
        return viewHolder;
    }


    //item的点击事件，仅需在外部调用setOnItemClickListener设置listener即可。
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            switch(v.getId()) {
                //打勾按钮
                case R.id.id_cb_store_product:
                    mOnItemClickListener.onCheckClick(v, v.getTag());
                    break;
                //整个item的点击
                default:
                    mOnItemClickListener.onItemClick(v, v.getTag().toString());//传给下一个activity，这就是data
            }
        }
    }


    // 类似listview的监听接口，仅需调用此函数即可
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);

        // item中收藏部件的点击
        void onCheckClick(View view, Object data);
    }


    //比较器
    public class Comp implements Comparator<oneProduct> {
        public int compare(oneProduct s1, oneProduct s2) {
            int pos1 =0, pos2 =0;
            for(int i=0; i<products.size(); i++) {
                if(products.get(i).pos==s1.pos) pos1=i;
                if(products.get(i).pos==s2.pos) pos2=i;
            }
            if( pos1 < pos2 ){  //小的排前面
                return 1;
            }else if( pos1 > pos2 ){
                return -1;
            }else{
                return 0;
            }
        }
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














