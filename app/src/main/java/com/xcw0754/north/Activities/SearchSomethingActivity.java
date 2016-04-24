package com.xcw0754.north.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xcw0754.north.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SearchSomethingActivity extends SwipeBackActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_something);
        ButterKnife.bind(this); //使上面的bind注解生效



    }









    // 后退按钮的点击事件
    @OnClick(R.id.id_tv_quxiao)
    public void moveback(TextView ibtn) {
        finish();
    }



}
