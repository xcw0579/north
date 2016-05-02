package com.xcw0754.north.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xcw0754.north.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class BookActivity extends SwipeBackActivity {



    @Bind(R.id.input_book)    EditText _inputText;
    @Bind(R.id.id_common_title)    TextView _title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this); //使上面的bind注解生效
        _title.setText("我的预约");
    }




    // 清除按钮
    @OnClick(R.id.btn_book)
    public void Clear(Button ibtn) {
        Toast.makeText(getBaseContext(), "登记成功，请耐心等待", Toast.LENGTH_LONG).show();
        _inputText.setText("");
    }



    // 后退按钮的点击事件
    @OnClick(R.id.id_btn_common_back)
    public void moveback(ImageButton ibtn) {
        finish();
    }


}
