package com.xcw0754.north.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xcw0754.north.Libraries.SharedPreferences.SPUtils;
import com.xcw0754.north.R;

import java.util.concurrent.Semaphore;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

//public class LoginActivity extends AppCompatActivity {
public class LoginActivity extends SwipeBackActivity {
    private static final String TAG = "LoginActivity";  //debug专用
    public static final int RESULT_CODE = 1;


    @Bind(R.id.input_email)     EditText _emailText;
    @Bind(R.id.input_password)  EditText _passwordText;
    @Bind(R.id.btn_login)       Button _loginButton;
    @Bind(R.id.link_signup)     Button _signupLink;
    @Bind(R.id.id_common_title) TextView _title;


    final private Semaphore sema = new Semaphore(0);
    private boolean islogin = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this); //使上面的bind注解生效

        _title.setText("登录");


        // 监听登录按钮。
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // 监听注册按钮。
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动Signup activity页面
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }



    public void login() {
//        登录过程的逻辑

        Log.d(TAG, "已经按下Login按钮....");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

//        验证悬浮框
//        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
//                R.style.AppTheme_Dark_Dialog);
        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setIndeterminate(true);  // true则可以设置当前进度值
        progressDialog.setCanceledOnTouchOutside(false);    // 设置在点击Dialog外是否取消Dialog进度条
        progressDialog.setMessage("验证中...");
        progressDialog.show();


        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // 的验证登录。
        sendLoginMessage(email, password);

        // 延迟3秒钟，再跳转到登录成功的相关页面
        new android.os.Handler().postDelayed(
                new Runnable() {
                        public void run() {
                            try {
                                sema.acquire();
                                if( islogin==true )
                                    onLoginSuccess(email, password); // 注册成功
                                else
                                    onLoginFailed();   //注册失败
                                sema.release();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();//  隐藏进度条
                        }
                }, 3000);
    }

    /**
     * 发送帐号密码邮件到服务器
     */
    public void sendLoginMessage(final String email,final String passwd) {

        Runnable requestTask = new Runnable() {
            @Override
            public void run() {
                String response = HttpRequest.get("http://10.0.3.2:5000/login", true,
                        "passwd", passwd, "email", email).body();

                JsonObject json = new JsonParser().parse(response).getAsJsonObject();
                String errcode = json.get("errcode").getAsString();
                String errmsg = json.get("errmsg").getAsString() ;
                if ( errcode.equals("1000") ) {
                    islogin = true;
                } else {
                    Log.d("network", "登录失败原因："+ errmsg );
                }
                sema.release();
            }
        };
        new Thread(requestTask).start();
    }

    /**
     * 登录成功，将个人信息等数据存进本地数据库中
     */
    public void onLoginSuccess(String email,String passwd) {

        SPUtils.put(getApplicationContext(), "email", email);
        SPUtils.put(getApplicationContext(), "passwd", passwd);

        _loginButton.setEnabled(true);
        //TODO 通过网络将个人信息数据拉下来
        Intent intent=new Intent();
        intent.putExtra("islogin", true);
        setResult(RESULT_CODE, intent);
        finish();
    }


    /**
     * 登录失败
     */
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }


    /**
     * 验证输入是否合法
     */
    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 16) {
            _passwordText.setError("between 4 and 16 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    // 后退按钮的点击事件
    @OnClick(R.id.id_btn_common_back)
    public void moveback(ImageButton ibtn) {
        finish();
    }


//    @Override
//    public void onBackPressed() {
//        // 按下后退键之后：直接退出整个界面
//        moveTaskToBack(true);
//    }


}
