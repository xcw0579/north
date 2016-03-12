package com.xcw0754.north.Libraries.testRecycleView;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


/**
 * Created by xiao on 16-3-12.
 */
public class MyLayoutManager extends GridLayoutManager {

    public MyLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

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


        View view = recycler.getViewForPosition(0);
        if( view != null ) {
            measureChild(view, widthSpec, heightSpec);
            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
            int line = ( itemCount + getSpanCount() - 1 ) / getSpanCount();
            int measuredHeight = line * ( view.getMeasuredHeight() + 6 ) ;
            setMeasuredDimension(measuredWidth, measuredHeight );
        }
    }

}