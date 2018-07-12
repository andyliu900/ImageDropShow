package com.moping.imageshow.view.dispatchview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.moping.imageshow.util.ScreenUtil;

/**
 * 贴上策略
 */
public class TopStrategy implements Strategy {

    @Override
    public void setDispatchAction(final DispatchImageView dispatchImageView) {
        FrameLayout.LayoutParams currentLayoutParams = (FrameLayout.LayoutParams) dispatchImageView.getLayoutParams();
        int leftMargin = currentLayoutParams.leftMargin;
        int topMargin = currentLayoutParams.topMargin;
        int width = currentLayoutParams.width;

        int parentWidth = ((ViewGroup)dispatchImageView.getParent()).getWidth();

        int fullCycleCount = (int)dispatchImageView.mTotalAngle / 360;

        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(dispatchImageView, "rotation", dispatchImageView.mTotalAngle - fullCycleCount * 360, 180);
        ValueAnimator leftAnimator = ValueAnimator.ofInt(leftMargin, (parentWidth - width) / 2);
        leftAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int currentValue = (Integer) valueAnimator.getAnimatedValue();
                ((FrameLayout.LayoutParams) dispatchImageView.getLayoutParams()).leftMargin = currentValue;
                dispatchImageView.setLayoutParams(dispatchImageView.getLayoutParams());
            }
        });
        ValueAnimator topAnimator = ValueAnimator.ofInt(topMargin, - ScreenUtil.dp2px(dispatchImageView.getContext(), 30));
        topAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int currentValue = (Integer) valueAnimator.getAnimatedValue();
                ((FrameLayout.LayoutParams) dispatchImageView.getLayoutParams()).topMargin = currentValue;
                dispatchImageView.setLayoutParams(dispatchImageView.getLayoutParams());
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotationAnimator, leftAnimator, topAnimator);
        animatorSet.setDuration(800);
        animatorSet.start();
    }

}
