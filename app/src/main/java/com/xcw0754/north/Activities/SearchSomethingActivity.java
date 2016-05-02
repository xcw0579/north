package com.xcw0754.north.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcw0754.north.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SearchSomethingActivity extends SwipeBackActivity {

    @Bind(R.id.id_et_search_something) EditText et;



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


    // 搜索按钮事件
    @OnClick(R.id.id_common_search_now)
    public void search(ImageView iv) {
        Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
        intent.putExtra("msg", et.getText().toString());       //传必要的数据,一般是产品编号
        startActivity(intent);
    }


}
