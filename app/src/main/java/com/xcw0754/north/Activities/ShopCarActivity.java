package com.xcw0754.north.Activities;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.ObjectConstructor;
import com.xcw0754.north.Libraries.SharedPreferences.SPUtils;
import com.xcw0754.north.Libraries.aboutRecycleView.DividerItemDecoration;
import com.xcw0754.north.Libraries.aboutRecycleView.SearchRecyclerView.MyLayoutManager2;
import com.xcw0754.north.Libraries.aboutRecycleView.SearchRecyclerView.RecyclerViewAdapter2;
import com.xcw0754.north.Libraries.aboutRecycleView.ShopCarRecyclerView.MyLayoutManager3;
import com.xcw0754.north.Libraries.aboutRecycleView.ShopCarRecyclerView.RecyclerViewAdapter3;
import com.xcw0754.north.MyApp;
import com.xcw0754.north.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Semaphore;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ShopCarActivity extends SwipeBackActivity {


    private RecyclerViewAdapter3 rvaCar;
    @Bind(R.id.id_shop_car_rv) RecyclerView rcvCar;
    @Bind(R.id.id_layout_no_chose) LinearLayout LlayoutNO;
    @Bind(R.id.id_layout_buy_page) RelativeLayout LlayoutYES;
    @Bind(R.id.id_btn_car_delete) Button deleteButton;
    @Bind(R.id.id_btn_car_buy) Button buyButton;
    @Bind(R.id.id_common_title) TextView tvTitle;
    final private static Semaphore sema = new Semaphore(0); //同步用





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_car);
        ButterKnife.bind(this);
        tvTitle.setText("购物车");

        init();
    }

    private void init() {

        //TODO 将所有的收藏读出来，显示出来。（只作资源请求，而不提交本地数据到后台）
        //TODO 隐藏提示信息



        //有产品则显示出来
        if( SPUtils.contains(getApplicationContext(), "car")  ) {

            String product = (String) SPUtils.get(getApplicationContext(), "car", "");
            JsonArray json = new JsonParser().parse(product).getAsJsonArray();

            if( json.size()==0 ) {  //并没有收藏

                LlayoutNO.setVisibility(View.VISIBLE);  //有了收藏的产品就要隐藏起来
                LlayoutYES.setVisibility(View.GONE);
            } else {

                rvaCar = new RecyclerViewAdapter3(this, json.size());
                rvaCar.setOnItemClickListener(new RecyclerViewAdapter3.OnRecyclerViewItemClickListener() {
                    //整个item的点击
                    @Override
                    public void onItemClick(View view, String data) {
                        //TODO 暂时不让点击进去
                    }

                    //item中的选中按钮的点击，data是产品编号
                    @Override
                    public void onCheckClick(View view, Object data) {
                        //在array中，如果存在，则删除，如果不存在，则添加进去
                        if( view.isSelected() ) {
                            rvaCar.delCheckList(data);
                            view.setSelected(false);
                            Log.d("msg", "checklist删除。。");
                        } else {
                            rvaCar.putCheckList(data);
                            view.setSelected(true);
                            Log.d("msg", "checklist变黑了。");
                        }
                    }
                });

                // 一旦有新的物品加入的话，勾选的框都会被取消调了。
                rcvCar.setAdapter(rvaCar);
                rcvCar.setLayoutManager(new MyLayoutManager3(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                rcvCar.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
                rcvCar.setItemAnimator(new DefaultItemAnimator());   //默认的增删动画


                //删除按钮
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("msg", "删除按钮被点击了。");
                        rvaCar.deleteAllChecked();
                    }
                });
                //TODO 购物车界面的购买按钮
                buyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 提交数据到服务端，更新一下待发货产品
                        if( rvaCar.checkList.size()>0) {
                            if( !MyApp.ContainEmail()) {
                                Toast.makeText(getBaseContext(), "请先登录", Toast.LENGTH_LONG).show();
                                return;
                            }


                            try {
                                BuyItNow();
                                sema.acquire();
                            } catch (InterruptedException e) {
                                Log.d("msg", "购买时出现异常了。");
                                e.printStackTrace();
                            }
                            rvaCar.deleteAllChecked();  //必须删光
                        } else {
                            Toast.makeText(getBaseContext(), "未选中任何物品", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                //显示出来
                LlayoutNO.setVisibility(View.GONE);
                LlayoutYES.setVisibility(View.VISIBLE);
            }
        } else {
            LlayoutNO.setVisibility(View.VISIBLE);  //有了收藏的产品就要隐藏起来
            LlayoutYES.setVisibility(View.GONE);
        }
    }


    // 按下购买按钮之后
    private void BuyItNow() {
        Runnable requestTask = new Runnable() {
            @Override
            public void run() {
                JsonObject json;
                for(int i=0; i<rvaCar.checkList.size(); i++) {
                    String Url = "http://10.0.3.2:5000/put/delivery";
                    String response = HttpRequest.get(Url, true,
                            "email", MyApp.getEmail(), "id", rvaCar.checkList.get(i).pos).body();

                    Log.d("msg", response);

                    json = new JsonParser().parse(response).getAsJsonObject();
                    String errcode = json.get("errcode").getAsString();
                    if ( !errcode.equals("1003") ) {
                        Log.d("msg", "购物车的购买按钮在提交数据到服务端时出错了。");
                        //Toast.makeText(getBaseContext(), "连接服务器出错", Toast.LENGTH_LONG).show();
                        return ;
                    }
                }
                sema.release();
                MyApp.updataDelivery(getApplicationContext());     //待发货的产品更新一下
            }
        };
        new Thread(requestTask).start();
        Toast.makeText(getApplicationContext(), "购买成功", Toast.LENGTH_LONG).show();
        Log.d("msg", "立即购买按钮已经被按下，数据应该也请求了。");
    }



    // 后退按钮的点击事件
    @OnClick(R.id.id_btn_common_back)
    public void moveback(ImageButton ibtn) {
        finish();
    }

}
