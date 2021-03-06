package com.xcw0754.north.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xcw0754.north.Libraries.aboutRecycleView.DividerItemDecoration;
import com.xcw0754.north.Libraries.aboutRecycleView.ProductRecyclerView.MyLayoutManager1;
import com.xcw0754.north.Libraries.aboutRecycleView.ProductRecyclerView.RecyclerViewAdapter1;
import com.xcw0754.north.Libraries.aboutRecycleView.TabSortRecyclerView.MyLayoutManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this); //使上面的bind注解生效

        //初始化
        oldcolor = tv_sale.getCurrentTextColor();


        //取出消息，是字符串形式的，以分类的简称+/+图片的数字，比如“tjpp/4”
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        String whichone = bundle.getString("msg");
//

        mmAdapter1 =  new RecyclerViewAdapter1(this, 15);
        rcv.setAdapter(mmAdapter1);
        rcv.setLayoutManager(new MyLayoutManager1(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rcv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }


    //TODO 最好在切换page的时候先loading一下更好












    private void itemChanged() {
//        for (int i=0; i<mmAdapter1.getItemCount(); i++) {
//            mmAdapter1.notifyItemChanged(i);
//        }
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
        this.itemChanged();
    }
//    销量
    @OnClick(R.id.id_product_list_sale)
    public void paintSale() {
        clearAllTextColor();
        tv_sale.setTextColor(getResources().getColor(R.color.primary_darked));
        //销量排序
        mmAdapter1.changeOrder(1);
        this.itemChanged();
    }
//    价格
    @OnClick(R.id.id_product_list_price)
    public void paintPric() {
        clearAllTextColor();
        tv_price.setTextColor(getResources().getColor(R.color.primary_darked));
        //价格排序
        mmAdapter1.changeOrder(2);
        this.itemChanged();
    }
//    过滤
    @OnClick(R.id.id_product_list_filter)
    public void paintFilt() {
        clearAllTextColor();
        tv_filter.setTextColor(getResources().getColor(R.color.primary_darked));
        //TODO 显示带筛选的排序，暂时不做
        mmAdapter1.changeOrder(3);
        this.itemChanged();
    }

    // 后退按钮的点击事件
    @OnClick(R.id.id_btn_common_back)
    public void moveback(ImageButton ibtn) {
        finish();
    }

}
