package com.sweetlab.sweetspot.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;

/**
 * RecyclerView LayoutManager used for the carousel. This extensions makes scrolling slower.
 */
public class CarouselLayoutManager extends LinearLayoutManager {
    private static final int TIME_MULTIPLIER = 5;

    public CarouselLayoutManager(Context context) {
        super(context, HORIZONTAL, false);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            protected int calculateTimeForScrolling(int dx) {
                return super.calculateTimeForScrolling(dx) * TIME_MULTIPLIER;
            }

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                if (this.getChildCount() == 0) {
                    return null;
                } else {
                    int firstChildPos = getPosition(getChildAt(0));
                    int direction = targetPosition < firstChildPos != getReverseLayout() ? -1 : 1;
                    return getOrientation() == 0 ? new PointF((float) direction, 0.0F) : new PointF(0.0F, (float) direction);
                }
            }
        };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}
