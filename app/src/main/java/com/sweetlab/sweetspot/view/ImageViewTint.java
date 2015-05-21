package com.sweetlab.sweetspot.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.widget.ImageView;

import com.sweetlab.sweetspot.R;

/**
 * Static methods to tint and unTint an ImageView. Using ImageView's
 * tags to handle cancellation and animation switching between
 * tinting and clearing.
 */
public class ImageViewTint {

    /**
     * Clear tint from ImageView.
     *
     * @param imageView ImageView to animate to no tint.
     * @param duration  Duration of animation.
     */
    public static void animateToNoTint(final ImageView imageView, final int duration) {
        Object objHasColorFilter = imageView.getTag(R.id.TAG_VIEW_TINT_HAS_COLOR_FILTER);
        if (objHasColorFilter == null) {
            return;
        }

        Object objColor = imageView.getTag(R.id.TAG_VIEW_TINT_COLOR);
        if (objColor == null) {
            return;
        }

        Object oldValueAnimator = imageView.getTag(R.id.TAG_VIEW_TINT_VALUE_ANIMATOR);
        if (oldValueAnimator != null) {
            ((ValueAnimator) oldValueAnimator).cancel();
        }

        final ValueAnimator valueAnimator = ValueAnimator.ofArgb((int) objColor, 0);

        valueAnimator.setDuration(duration).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int argb = (int) animation.getAnimatedValue();
                imageView.setTag(R.id.TAG_VIEW_TINT_COLOR, argb);
                imageView.setTag(R.id.TAG_VIEW_TINT_HAS_COLOR_FILTER, true);
                imageView.setColorFilter(argb);
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                imageView.setTag(R.id.TAG_VIEW_TINT_VALUE_ANIMATOR, valueAnimator);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageView.setTag(R.id.TAG_VIEW_TINT_VALUE_ANIMATOR, null);
                imageView.setTag(R.id.TAG_VIEW_TINT_COLOR, 0);
                imageView.setTag(R.id.TAG_VIEW_TINT_HAS_COLOR_FILTER, false);
                imageView.clearColorFilter();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // onAnimationEnd will be called.
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        valueAnimator.start();
    }

    /**
     * Apply tint to ImageView.
     *
     * @param imageView ImageView to tint.
     * @param toColor   Color to use as tint.
     * @param duration  Duration of animation.
     */
    public static void animateToTint(final ImageView imageView, final int toColor, final int duration) {
        int fromColor = 0;

        Object objHasColorFilter = imageView.getTag(R.id.TAG_VIEW_TINT_HAS_COLOR_FILTER);
        Object objColor = imageView.getTag(R.id.TAG_VIEW_TINT_COLOR);
        if (objHasColorFilter != null && objColor != null) {
            fromColor = (int) objColor;
        }

        Object oldValueAnimator = imageView.getTag(R.id.TAG_VIEW_TINT_VALUE_ANIMATOR);
        if (oldValueAnimator != null) {
            ((ValueAnimator) oldValueAnimator).cancel();
        }

        final ValueAnimator valueAnimator = ValueAnimator.ofArgb(fromColor, toColor);
        valueAnimator.setDuration(duration).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int argb = (int) animation.getAnimatedValue();
                imageView.setTag(R.id.TAG_VIEW_TINT_COLOR, argb);
                imageView.setTag(R.id.TAG_VIEW_TINT_HAS_COLOR_FILTER, true);
                imageView.setColorFilter(argb);
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                imageView.setTag(R.id.TAG_VIEW_TINT_VALUE_ANIMATOR, valueAnimator);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageView.setTag(R.id.TAG_VIEW_TINT_VALUE_ANIMATOR, null);
                imageView.setTag(R.id.TAG_VIEW_TINT_COLOR, toColor);
                imageView.setTag(R.id.TAG_VIEW_TINT_HAS_COLOR_FILTER, true);
                imageView.setColorFilter(toColor);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // onAnimationEnd will be called.
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        valueAnimator.start();
    }
}
