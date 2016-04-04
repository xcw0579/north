package com.xcw0754.north.Libraries.aboutRecycleView.SearchRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xcw0754.north.R;

/**
 * Created by xiao on 16-3-13.
 */
public class RecyclerViewHolder2 extends RecyclerView.ViewHolder{


    public ImageView iv;        //缩略图
    public TextView tv_price;   //价格
    public TextView tv_title1;  //大标题
    public TextView tv_title2;  //小标题
    public int pos;             //产品编号（后台的编号）
    public CheckBox cb_checkbox; //勾选按钮


    public RecyclerViewHolder2(View arg) {
        super(arg);
        // 绑定各个item上面的view即可
        iv = (ImageView) arg.findViewById(R.id.id_iv_item1);
        tv_price = (TextView) arg.findViewById(R.id.id_product_price);
        tv_title1 = (TextView) arg.findViewById(R.id.id_product_title_profile);
        tv_title2 = (TextView) arg.findViewById(R.id.id_product_title_detail);
        pos = 0;
        cb_checkbox = (CheckBox) arg.findViewById(R.id.id_cb_store_product);
    }
}
