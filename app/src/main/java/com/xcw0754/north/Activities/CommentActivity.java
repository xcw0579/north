package com.xcw0754.north.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xcw0754.north.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class CommentActivity extends SwipeBackActivity {


    @Bind(R.id.id_comment_webview)      WebView wv ;
    @Bind(R.id.id_common_title)         TextView tvTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        tvTitle.setText("主题社区");
        wv.loadUrl("http://mygd.qq.com/f-130-1.htm");
        //wv.loadUrl("https://www.taobao.com/go/market/gmall/g-sale.php?spm=5148.1292865.temai.1.8svpGb");
    }



    // 后退按钮的点击事件
    @OnClick(R.id.id_btn_common_back)
    public void moveback(ImageButton ibtn) {
        finish();
    }





}
