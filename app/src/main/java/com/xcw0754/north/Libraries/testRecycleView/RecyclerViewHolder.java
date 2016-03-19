package com.xcw0754.north.Libraries.testRecycleView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.xcw0754.north.R;

/**
 * Created by xiao on 16-3-13.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder{

    public ImageView iv;

    public RecyclerViewHolder(View arg) {

        super(arg);
        iv = (ImageView) arg.findViewById(R.id.id_iv_item);
    }


}
