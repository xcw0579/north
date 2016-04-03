package com.xcw0754.north.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xcw0754.north.Libraries.SharedPreferences.SPUtils;
import com.xcw0754.north.Libraries.aboutRecycleView.DividerItemDecoration;
import com.xcw0754.north.Libraries.aboutRecycleView.ProductRecyclerView.MyLayoutManager1;
import com.xcw0754.north.Libraries.aboutRecycleView.ProductRecyclerView.RecyclerViewAdapter1;
import com.xcw0754.north.Libraries.aboutRecycleView.TabSortRecyclerView.MyLayoutManager;
import com.xcw0754.north.Libraries.aboutRecycleView.TabSortRecyclerView.RecyclerViewAdapter;
import com.xcw0754.north.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ProductListActivity extends SwipeBackActivity {

    @Bind(R.id.id_product_list_synthesize)  TextView tv_synthesize;
    @Bind(R.id.id_product_list_sale)        TextView tv_sale;
    @Bind(R.id.id_product_list_price)       TextView tv_price;
    @Bind(R.id.id_product_list_filter)      TextView tv_filter;
    @Bind(R.id.id_product_list_recyclerview)RecyclerView rcv;

    private int oldcolor;
    private RecyclerViewAdapter1 mmAdapter1;
    //TODO 最好在切换page的时候先loading一下更好






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this); //使上面的bind注解生效

        //初始化
        oldcolor = tv_sale.getCurrentTextColor();


        //取出产品类型，是字符串形式的，以分类的简称+/+图片的数字，比如“tjpp/4”
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        String whichone = bundle.getString("msg");
//


        mmAdapter1 =  new RecyclerViewAdapter1(this, 15);
        mmAdapter1.setOnItemClickListener(new RecyclerViewAdapter1.OnRecyclerViewItemClickListener() {
            //整个item的点击
            @Override
            public void onItemClick(View view, String data) {
                Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                intent.putExtra("msg", data);       //传必要的数据,一般是产品编号
                startActivity(intent);
            }
            //item中的部件的点击，data是产品编号
            @Override
            public void onFavoriteClick(View view, String data) {

                //根据data判断是否已经被收藏了，再设置对应的图片给收藏按钮
                if( SPUtils.check(getApplicationContext(), "favorite", data) ) {
                    //已经有收藏了，则取消收藏
                    SPUtils.checkOut(getApplicationContext(), "favorite", data);
                } else {
                    //取消收藏，再显示空心（刷新一下UI即可）
                    SPUtils.checkIn(getApplicationContext(), "favorite", data);
                }
                mmAdapter1.sendMessageToHanler(1, Integer.parseInt(data) ); //更新ui

            }

        });

        rcv.setAdapter(mmAdapter1);
        rcv.setLayoutManager(new MyLayoutManager1(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rcv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

    }


//    将4个textview都变成黑色
    private void clearAllTextColor() {
        tv_synthesize.setTextColor(oldcolor);
        tv_sale.setTextColor(oldcolor);
        tv_price.setTextColor(oldcolor);
        tv_filter.setTextColor(oldcolor);
    }

//    综合
    @OnClick(R.id.id_product_list_synthesize)
    public void paintSynt() {
        clearAllTextColor();
        tv_synthesize.setTextColor(getResources().getColor(R.color.primary_darked));
        //综合排序
        mmAdapter1.changeOrder(0);
    }
//    销量
    @OnClick(R.id.id_product_list_sale)
    public void paintSale() {
        clearAllTextColor();
        tv_sale.setTextColor(getResources().getColor(R.color.primary_darked));
        //销量排序
        mmAdapter1.changeOrder(1);
    }
//    价格
    @OnClick(R.id.id_product_list_price)
    public void paintPric() {
        clearAllTextColor();
        tv_price.setTextColor(getResources().getColor(R.color.primary_darked));
        //价格排序
        mmAdapter1.changeOrder(2);
    }
//    过滤
    @OnClick(R.id.id_product_list_filter)
    public void paintFilt() {
        clearAllTextColor();
        tv_filter.setTextColor(getResources().getColor(R.color.primary_darked));
        //TODO 显示带筛选的排序，暂时不做
        mmAdapter1.changeOrder(3);
    }

    // 后退按钮的点击事件
    @OnClick(R.id.id_btn_common_back)
    public void moveback(ImageButton ibtn) {
        finish();
    }

}
