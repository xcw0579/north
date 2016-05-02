package com.xcw0754.north.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xcw0754.north.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class DefaultActivity extends SwipeBackActivity {

    @Bind(R.id.id_common_title)    TextView tvTitle;
    @Bind(R.id.id_defauly_content)    TextView tvContent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String data = bundle.getString("title");

        tvTitle.setText(data);

        data = bundle.getString("content");
        tvContent.setText(data);
    }


    // 后退按钮的点击事件
    @OnClick(R.id.id_btn_common_back)
    public void moveback(ImageButton ibtn) {
        finish();
    }



}

