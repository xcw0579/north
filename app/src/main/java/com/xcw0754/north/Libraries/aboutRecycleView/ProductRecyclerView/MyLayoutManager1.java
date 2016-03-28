package com.xcw0754.north.Libraries.aboutRecycleView.ProductRecyclerView;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by xiao on 16-3-12.
 */
public class MyLayoutManager1 extends LinearLayoutManager {

    public MyLayoutManager1(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyLayoutManager1(Context context) {
        super(context);
    }

    public MyLayoutManager1(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }


    /*
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return null;
    }


    //绘制整块的面积用的，grid比较需要
    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                          int widthSpec, int heightSpec) {
        // 防止 0 item的情况
        int itemCount = getItemCount() ;
        if( itemCount == 0 ) {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
            Log.d("my", "没有item。");
            return ;
        }

        // 手动计算出layout的高度
        View view = recycler.getViewForPosition(0);
        if( view != null ) {
            measureChild(view, widthSpec, heightSpec);
            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
            int measuredHeight = itemCount;
            setMeasuredDimension(measuredWidth, measuredHeight );
        }
    }*/

}