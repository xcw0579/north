package com.xcw0754.north.Libraries.Introduction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.paolorotolo.appintro.AppIntro2;
import com.xcw0754.north.Activities.FragmentActivity;
import com.xcw0754.north.R;


/**
 * Created by xiao on 16-3-19.
 */

public class IntroActivity extends AppIntro2 {

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        addSlide(SampleSlide.newInstance(R.layout.intro_layout11));
        addSlide(SampleSlide.newInstance(R.layout.intro_layout22));
        addSlide(SampleSlide.newInstance(R.layout.intro_layout33));
        Log.d("intropage", "创建了。");
    }

    @Override
    public void onDonePressed() {
        Intent intent = new Intent(this, FragmentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
        System.gc();
        Log.d("intropage", "引导页被回收。");
    }



}
