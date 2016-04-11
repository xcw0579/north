package com.xcw0754.north.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xcw0754.north.Libraries.BigkooConvenientBanner.NetworkImageHolderView;
import com.xcw0754.north.Libraries.SharedPreferences.SPUtils;
import com.xcw0754.north.R;


import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;





/**
 * Created by xiao on 16-4-4.
 * modified 16-4-4.
 */

public class ProductDetailActivity extends SwipeBackActivity {

//    private ArrayList<Integer> localImages = new ArrayList<>();
    private ConvenientBanner convenientBanner;
    private ArrayList<String> networkImages = new ArrayList<>();
    private Handler dataCacheHandler;
    private int datas = 0;
    private Semaphore sema = new Semaphore(0);
    private String data;

    @Bind(R.id.id_common_title)             TextView tv_title;
    @Bind(R.id.id_product_detail_title)     TextView tv_title2;





    //TODO 产品的详情界面，尽量简单即可

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this); //使上面的bind注解生效

        convenientBanner = (ConvenientBanner) findViewById(R.id.id_product_detail_convenientBanner);
        countFile();
        init();

        //TODO 这里不应该是这样设的，为了方便才这样。
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        data = bundle.getString("msg"); //这个是为了加入购物车用的

        String whichone = "美的节能电...";    //直接设置为电风扇了，不需要的时候要改。
        tv_title.setText(whichone);
        tv_title2.setText("美的电风扇落地扇 台式家用遥控定时风扇静音转页落地风扇包邮 五叶柔风 智能遥控 强劲大风 特设静音档");
    }


    private void countFile() {
        //另开线程处理
        Runnable requestTask = new Runnable() {
            @Override
            public void run() {
                String Url = "http://10.0.3.2:5000/count/source/sort/itemlist/cfxd/1";
                String response = HttpRequest.get(Url).body();
                JsonObject json = new JsonParser().parse(response).getAsJsonObject();
                datas = json.get("count").getAsInt();
                sema.release();
            }
        };
        new Thread(requestTask).start();
    }

    /**
     * 初始化工作
     */
    private void init() {

//        //本地图片的加载方式
//        for (int position = 0; position < 7; position++) {
////            localImages.add( getResId("ic_test_" + position, R.drawable.class) );
////            localImages.add( getResId("ic123", R.drawable.class) );
//            localImages.add( R.drawable.ic123 );
//        }
//
//        convenientBanner.setPages(
//                new CBViewHolderCreator<LocalImageHolderView>() {
//                    //顺便构造
//                    @Override
//                    public LocalImageHolderView createHolder() {
//                        return new LocalImageHolderView();
//                    }
//                }, localImages)
//                .setPageIndicator(new int[]{R.drawable.ic_page_indicator,
//                        R.drawable.ic_page_indicator_focused}); //图片有点太大了
//


        try {
            sema.acquire();
        } catch( Exception e ) {
            Log.d("msg", "请求统计文件数量失败。");
        }

        for(int i=0; i<datas; i++) {
            networkImages.add("http://10.0.3.2:5000/source/sort/itemlist/cfxd/1/" + i + ".jpg");
        }

        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                            //顺便构造
                            @Override
                            public NetworkImageHolderView createHolder() {
                                return new NetworkImageHolderView();
                            }
                }, networkImages)   //这个数组会逐个传到adapter中的data，图片大小在xml设置。
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator,
                        R.drawable.ic_page_indicator_focused});

    }





    // 开始自动翻页
    @Override
    protected void onResume() {
        super.onResume();
        //开始自动翻页
        convenientBanner.startTurning(3000);
    }

    // 停止自动翻页
    @Override
    protected void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();
    }


    //添加到购物车
    @OnClick(R.id.id_btn_detail_addincar)
    public void addInCar(Button btn) {

        //目前实现只能添加一个产品，不能设置数量、地址等信息。
        if( SPUtils.check(getApplicationContext(), "car", data) ) {
            return ;
        }
        SPUtils.checkIn(getApplicationContext(), "car", data);  //这就是添加到购物车了。

        if( SPUtils.check(getApplicationContext(), "car", data) ) { //成功的话就显示出来。
            //添加成功则用显示框提示成功。
            Toast.makeText(getBaseContext(), "成功添加到购物车", Toast.LENGTH_LONG).show();
        }
    }


    // 后退按钮的点击事件
    @OnClick(R.id.id_btn_common_back)
    public void moveback(ImageButton ibtn) {
        finish();
    }

}




