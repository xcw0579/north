package com.xcw0754.north.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xcw0754.north.R;

import org.json.JSONObject;

import java.util.concurrent.Semaphore;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_name)      EditText    _nameText;
    @Bind(R.id.input_email)     EditText    _emailText;
    @Bind(R.id.input_password)  EditText    _passwordText;
    @Bind(R.id.btn_signup)      Button      _signupButton;
//    @Bind(R.id.link_login) TextView _loginLink;


    final private Semaphore sema = new Semaphore(1);
    private boolean issignup = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

//        _loginLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Finish the registration screen and return to the Login activity
//                     登录：将注册信息存到服务器上。
//                finish();
//            }
//        });
    }

    /**
     * 注册过程
     */
    public void signup() {
        Log.d(TAG, "Signup");

        // 验证注册信息
        if ( !validate() ) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(this);
//                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("注册中...");
        progressDialog.show();      // 一直显示注册进度条，直到下面销毁这个activity。

        //  取出注册信息
        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String passwd = _passwordText.getText().toString();

        // 提交注册信息到数据库
        sendSignupMessage(name, email, passwd);

        /**
         * 延迟调用
         */
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        try {
                            sema.acquire();
                            if( issignup==true )
                                onSignupSuccess();// 注册成功
                            else
                                onSignupFailed();   //注册失败
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
    public void sendSignupMessage(final String name,final String email,final String passwd) {

        Runnable requestTask = new Runnable() {
            @Override
            public void run() {
                String response = HttpRequest.get("http://10.0.3.2:5000/signup", true,
                        "user", name, "passwd", passwd, "email", email).body();
                JsonObject json = new JsonParser().parse(response).getAsJsonObject();

                String errcode = json.get("errcode").getAsString();
                String errmsg = json.get("errmsg").getAsString() ;
                if ( errcode.equals("1001") ) {
                    try {
                        sema.acquire();
                        issignup = true;
                        sema.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d( "network", "注册帐号失败原因："+ errmsg );
                }
            }
        };
        new Thread(requestTask).start();
    }

    /**
     * 注册成功：返回注册的帐号给登录界面，由登录界面来存储登录帐号。
     */
    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "注册成功", Toast.LENGTH_LONG).show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        finish();
                    }
                }, 1000);

//        setResult(RESULT_OK, null);
//        finish();

    }

    /**
     * 注册失败：进行提示
     */
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    /**
     * 检测注册信息是否符合规范
     */
    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }



}










