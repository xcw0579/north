package com.xcw0754.north.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import com.xcw0754.north.R;

import butterknife.OnClick;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class MessageModifyActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_modify);
    }



    // 后退按钮的点击事件
    @OnClick(R.id.id_btn_common_back)
    public void moveback(ImageButton ibtn) {
        finish();
    }

}
