package com.xcw0754.north.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xcw0754.north.R;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ProductDetailActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
    }
    //TODO 产品的详情界面，尽量简单即可
}
