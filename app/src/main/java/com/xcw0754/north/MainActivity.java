package com.xcw0754.north;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xcw0754.north.Activities.FragmentActivity;
import com.xcw0754.north.Libraries.Introduction.IntroActivity;


/**
 * 记录一些知识的信息：
 * 1) 绑定事件的三种方式：http://www.nowamagic.net/academy/detail/50120258
 *  这里讲得更加详细：http://blog.csdn.net/primer_programer/article/details/23056147
 */





/**
 * Created by Administrator on 2016/2/10.
 * 主界面：用于显示新产品的文化，类似于虾米第一次启动时显示的几个小gif一样。
 * 通常用于测试其他的模块，直接start那个Activity。
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); 跟AppCompatActivity不兼容？
        setContentView(R.layout.activity_main);



        //  切换activity
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onPause() {
        Log.d("mainpage", "MainActivity被回收。");
        super.onPause();
        finish();
    }






















/*    // 还没有重写
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // 按下menu键会触发的动作
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // 还没有重写
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 选中menu菜单中的任意一项后会调用此方法
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement  这个xml里没有东西
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}
