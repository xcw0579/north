package com.xcw0754.north.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xcw0754.north.Libraries.aboutRecycleView.DeliveryRecyclerView.MyLayoutManager5;
import com.xcw0754.north.Libraries.aboutRecycleView.DeliveryRecyclerView.RecyclerViewAdapter5;
import com.xcw0754.north.Libraries.aboutRecycleView.ShopCarRecyclerView.MyLayoutManager3;
import com.xcw0754.north.Libraries.aboutRecycleView.ShopCarRecyclerView.RecyclerViewAdapter3;
import com.xcw0754.north.MyApp;
import com.xcw0754.north.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class DeliveryActivity extends SwipeBackActivity {

    private RecyclerViewAdapter5 rvaDelivery;
    @Bind(R.id.id_delivery_list_recyclerview)   RecyclerView rcvDelivery;
    @Bind(R.id.id_common_title)                 TextView tvTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        ButterKnife.bind(this); //使上面的bind注解生效
        tvTitle.setText("等待发货");
        init();
    }


    private void init() {
        Log.d("msg", "待发货一共有"+MyApp.getProducts().size()+"件商品");
        rvaDelivery = new RecyclerViewAdapter5(getApplicationContext(), MyApp.getProducts());

        rcvDelivery.setAdapter(rvaDelivery);
        rcvDelivery.setLayoutManager(new MyLayoutManager5(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

    }



    // 后退按钮的点击事件
    @OnClick(R.id.id_btn_common_back)
    public void moveback(ImageButton ibtn) {
        finish();
    }
}
