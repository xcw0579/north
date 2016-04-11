package com.xcw0754.north.Libraries.aboutRecycleView.MiaoShaRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcw0754.north.R;

/**
 * Created by xiao on 16-3-13.
 * 这是首页用的。
 */
public class RecyclerViewHolder4 extends RecyclerView.ViewHolder{


    public ImageView iv;        //缩略图

    public RecyclerViewHolder4(View arg) {
        super(arg);
        iv = (ImageView) arg.findViewById(R.id.id_home_block_title);
    }
}
