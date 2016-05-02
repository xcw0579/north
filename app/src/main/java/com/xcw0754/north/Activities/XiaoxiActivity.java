package com.xcw0754.north.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xcw0754.north.Libraries.aboutRecycleView.DeliveryRecyclerView.MyLayoutManager5;
import com.xcw0754.north.Libraries.aboutRecycleView.DeliveryRecyclerView.RecyclerViewAdapter5;
import com.xcw0754.north.Libraries.aboutRecycleView.XiaoxiRecyclerView.MyLayoutManager6;
import com.xcw0754.north.Libraries.aboutRecycleView.XiaoxiRecyclerView.RecyclerViewAdapter6;
import com.xcw0754.north.MyApp;
import com.xcw0754.north.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class XiaoxiActivity extends SwipeBackActivity {

    @Bind(R.id.id_common_title)    TextView tvTitle;
    @Bind(R.id.id_xiaoxi_recyclerview) RecyclerView rv;
    private RecyclerViewAdapter6 rva;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiaoxi);

        ButterKnife.bind(this);
        tvTitle.setText("我的消息");

        init();
    }

    private void init() {
        rva = new RecyclerViewAdapter6(getApplicationContext(), MyApp.GetXiaoxi().size());

        // 监听item事件
        rva.setOnItemClickListener( new RecyclerViewAdapter6.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Intent intent = new Intent(getApplicationContext(), WebviewActivity.class);
                //intent.putExtra("msg", data);
                startActivity(intent);
            }
        });

        rv.setAdapter(rva);
        rv.setLayoutManager(new MyLayoutManager6(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
    }







    // 后退按钮的点击事件
    @OnClick(R.id.id_btn_common_back)
    public void moveback(ImageButton ibtn) {
        finish();
    }


}
