package com.xcw0754.north.Libraries.aboutRecycleView.XiaoxiRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcw0754.north.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xiao on 16-3-13.
 * 这是待发货界面用的。
 */
public class RecyclerViewHolder6 extends RecyclerView.ViewHolder{


    public ImageView iv;         // 产品圆形缩略图
    public TextView shor;        // 消息简介




    public RecyclerViewHolder6(View arg) {
        super(arg);

        iv = (ImageView) arg.findViewById(R.id.id_iv_item1);
        shor = (TextView) arg.findViewById(R.id.id_xiaoxi_short);

    }


}
