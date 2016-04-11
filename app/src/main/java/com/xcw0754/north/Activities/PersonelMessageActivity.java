package com.xcw0754.north.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xcw0754.north.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class PersonelMessageActivity extends SwipeBackActivity {





    @Bind(R.id.id_personel_head_content)    TextView    tv_head;
    @Bind(R.id.id_personel_user_content)    TextView    tv_user;
    @Bind(R.id.id_personel_nick_content)    TextView    tv_nick;
    @Bind(R.id.id_personel_sex_content)     TextView    tv_sex;
    @Bind(R.id.id_personel_born_content)    TextView    tv_born;

    @Bind(R.id.id_personel_vip_content)     TextView    tv_vip;
    @Bind(R.id.id_personel_club_content)    TextView    tv_club;
    @Bind(R.id.id_personel_addr_content)    TextView    tv_addr;
    @Bind(R.id.id_personel_safe_content)    TextView    tv_safe;
    @Bind(R.id.id_common_title)             TextView    tv_title;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personel_message);
        ButterKnife.bind(this);
        initText();
    }

    private void initText() {
        tv_title.setText("个人中心");


        tv_head.setText("");
        tv_user.setText("xcw0579@175game.com");
        tv_nick.setText("丛林第一");
        tv_sex.setText("男");
        tv_born.setText("1999年7月15日");

        tv_vip.setText("我的vip");
        tv_club.setText("我的俱乐部");
        tv_addr.setText("广州市...");
        tv_safe.setText("可修改密码");

    }


    // 头像
    @OnClick(R.id.id_personel_head)
    public void onClickhead(LinearLayout layout) {
        Intent intent = new Intent(getApplicationContext(), MessageModifyActivity.class);
        startActivity(intent);
    }

    // 用户名
    @OnClick(R.id.id_personel_user)
    public void onClickuser(LinearLayout layout) {
        Intent intent = new Intent(getApplicationContext(), MessageModifyActivity.class);
        startActivity(intent);
    }

    // 昵称
    @OnClick(R.id.id_personel_nick)
    public void onClicknick(LinearLayout layout) {
        Intent intent = new Intent(getApplicationContext(), MessageModifyActivity.class);
        startActivity(intent);
    }

    // 性别
    @OnClick(R.id.id_personel_sex)
    public void onClicksex(LinearLayout layout) {
        Intent intent = new Intent(getApplicationContext(), MessageModifyActivity.class);
        startActivity(intent);
    }

    // 出生日期
    @OnClick(R.id.id_personel_born)
    public void onClickborn(LinearLayout layout) {
        Intent intent = new Intent(getApplicationContext(), MessageModifyActivity.class);
        startActivity(intent);
    }

    // 会员
    @OnClick(R.id.id_personel_vip)
    public void onClickvip(LinearLayout layout) {
        Intent intent = new Intent(getApplicationContext(), MessageModifyActivity.class);
        startActivity(intent);
    }

    // 俱乐部
    @OnClick(R.id.id_personel_club)
    public void onClickclub(LinearLayout layout) {
        Intent intent = new Intent(getApplicationContext(), MessageModifyActivity.class);
        startActivity(intent);
    }

    // 地址管理
    @OnClick(R.id.id_personel_addr)
    public void onClickaddr(LinearLayout layout) {
        Intent intent = new Intent(getApplicationContext(), MessageModifyActivity.class);
        startActivity(intent);
    }

    // 账户安全
    @OnClick(R.id.id_personel_safe)
    public void onClicksafe(LinearLayout layout) {
        Intent intent = new Intent(getApplicationContext(), MessageModifyActivity.class);
        startActivity(intent);
    }



    // 后退按钮
    @OnClick(R.id.id_btn_common_back)
    public void moveback(ImageButton ibtn) {
        finish();
    }

}
