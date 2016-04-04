package com.xcw0754.north.Libraries.BigkooConvenientBanner;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.bigkoo.convenientbanner.holder.Holder;
import com.squareup.picasso.Picasso;
import com.xcw0754.north.R;

/**
 * Created by xiao on 16-4-4.
 */

public class NetworkImageHolderView implements Holder<String> {
    private ImageView imageView;


    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    /**
     * 传个url进来，自动加载到imageview中
     * @param context
     * @param position  不清楚
     * @param data      图片的URL
     */
    @Override
    public void UpdateUI(Context context,int position, String data) {
        imageView.setImageResource(R.drawable.ic_default_image);
        Picasso.with(context).load(data).into(imageView);
    }
}