package com.xcw0754.north.Libraries.aboutRecycleView.ProductRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcw0754.north.R;

/**
 * Created by xiao on 16-3-13.
 */
public class RecyclerViewHolder1 extends RecyclerView.ViewHolder{


    public ImageView iv;
    public TextView tv_price;
    public TextView tv_title1;
    public TextView tv_title2;
    public int pos;

    public RecyclerViewHolder1(View arg) {
        super(arg);
        // 绑定各个item上面的view即可
        iv = (ImageView) arg.findViewById(R.id.id_iv_item1);
        tv_price= (TextView) arg.findViewById(R.id.id_product_price);
        tv_title1= (TextView) arg.findViewById(R.id.id_product_title_profile);
        tv_title2= (TextView) arg.findViewById(R.id.id_product_title_detail);
        pos = 0;
    }
}
