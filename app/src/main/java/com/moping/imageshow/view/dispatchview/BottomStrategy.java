package com.moping.imageshow.view.dispatchview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.moping.imageshow.entity.MessageEvent;
import com.moping.imageshow.util.ScreenUtil;
import com.moping.imageshow.view.ZoomImageView;

import org.greenrobot.eventbus.EventBus;

/**
 * 贴上策略
 */
public class BottomStrategy implements Strategy {

    @Override
    public void setDispatchAction(final ZoomImageView dispatchImageView, float totalAngle, float scale) {
        FrameLayout.LayoutParams currentLayoutParams = (FrameLayout.LayoutParams) dispatchImageView.getLayoutParams();
        int leftMargin = currentLayoutParams.leftMargin;
        int topMargin = currentLayoutParams.topMargin;
        int height = currentLayoutParams.height;
        int width = currentLayoutParams.width;

        int parentWidth = ((ViewGroup)dispatchImageView.getParent()).getWidth();

        int fullCycleCount = (int)totalAngle / 360;
        float mScale = scale;

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(dispatchImageView, "scaleX", mScale, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(dispatchImageView, "scaleY", mScale, 1f);
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(dispatchImageView, "rotation", totalAngle - fullCycleCount * 360, 0f);
        ValueAnimator leftAnimator = ValueAnimator.ofInt(leftMargin, (parentWidth - width) / 2);
        leftAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int currentValue = (Integer) valueAnimator.getAnimatedValue();
                ((FrameLayout.LayoutParams) dispatchImageView.getLayoutParams()).leftMargin = currentValue;
                dispatchImageView.setLayoutParams(dispatchImageView.getLayoutParams());
            }
        });
        ValueAnimator topAnimator = ValueAnimator.ofInt(topMargin, ScreenUtil.getScreenHeight(dispatchImageView.getContext()) - height);
        topAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int currentValue = (Integer) valueAnimator.getAnimatedValue();
                ((FrameLayout.LayoutParams) dispatchImageView.getLayoutParams()).topMargin = currentValue;
                dispatchImageView.setLayoutParams(dispatchImageView.getLayoutParams());
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator, rotationAnimator, leftAnimator, topAnimator);
        animatorSet.setDuration(800);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                EventBus.getDefault().post(new MessageEvent(dispatchImageView));
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
    }

}
