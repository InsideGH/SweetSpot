package com.sweetlab.sweetspot.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Aspect ratio configurable ImageView.
 */
public class AspectImageView extends ImageView {

    private float mBitmapRatio;

    public AspectImageView(Context context) {
        super(context);
    }

    public AspectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        if (w == 0 || h == 0) {
            if (w == 0) {
                w = (int) (h / mBitmapRatio);
            } else {
                h = (int) (w / mBitmapRatio);
            }
        }
        setMeasuredDimension(w, h);
    }

    /**
     * Set the aspect ration used for this view.
     *
     * @param aspectRatio The width divided by height.
     */
    public void setAspectRatio(float aspectRatio) {
        mBitmapRatio = aspectRatio;
    }
}
