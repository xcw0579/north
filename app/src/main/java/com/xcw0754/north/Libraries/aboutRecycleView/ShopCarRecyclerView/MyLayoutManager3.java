package com.xcw0754.north.Libraries.aboutRecycleView.ShopCarRecyclerView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


/**
 * Created by xiao on 16-3-12.
 */
public class MyLayoutManager3 extends LinearLayoutManager {

    public MyLayoutManager3(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                          int widthSpec, int heightSpec) {
        // 防止 0 item的情况
        int itemCount = getItemCount() ;
        if( itemCount == 0 ) {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
            Log.d("msg", "没有item。");
            return ;
        }
        //TODO 这里在item为0时取这个就会出错。
        // 手动计算出layout的高度
        View view = null;
        for(int i=0; i<itemCount; i++) {
            if( view==null ) {
                try{
                    view = recycler.getViewForPosition(i); //这里可能会导致多次绘制首个item
                }catch (Exception e) {
                    Log.d("msg", "改成获取第" + i + "个item失败了");
                }
            }
        }
        if( view==null )    return ;

        int extra = 0;
        if( itemCount>=4 )  extra = 120;
        measureChild(view, widthSpec, heightSpec);
        int measuredWidth = View.MeasureSpec.getSize(widthSpec);
        int measuredHeight = (view.getMeasuredHeight() + 8) * itemCount + extra;
        setMeasuredDimension(measuredWidth, measuredHeight);
    }
}