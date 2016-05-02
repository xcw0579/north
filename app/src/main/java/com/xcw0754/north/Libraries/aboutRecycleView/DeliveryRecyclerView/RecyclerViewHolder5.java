package com.xcw0754.north.Libraries.aboutRecycleView.DeliveryRecyclerView;

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
public class RecyclerViewHolder5 extends RecyclerView.ViewHolder{


    public CircleImageView cb;         // 产品圆形缩略图
    public TextView title1;         // 大标题
    public TextView title2;         // 小标题
    public TextView price;          // 价格




    public RecyclerViewHolder5(View arg) {
        super(arg);

        cb = (CircleImageView) arg.findViewById(R.id.id_delivery_cb_iv);
        title1 = (TextView) arg.findViewById(R.id.id_product_title_profile);
        title2 = (TextView) arg.findViewById(R.id.id_product_title_detail);
        price = (TextView) arg.findViewById(R.id.id_product_price);
    }
}
