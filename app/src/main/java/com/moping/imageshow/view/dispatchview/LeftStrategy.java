package com.moping.imageshow.view.dispatchview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.moping.imageshow.entity.MessageEvent;
import com.moping.imageshow.util.ScreenUtil;
import com.moping.imageshow.view.ZoomImageView;

import org.greenrobot.eventbus.EventBus;

/**
 * 贴左策略
 */
public class LeftStrategy implements Strategy {

    @Override
    public void setDispatchAction(final ZoomImageView dispatchImageView, float totalAngle, float scale) {
        FrameLayout.LayoutParams currentLayoutParams = (FrameLayout.LayoutParams) dispatchImageView.getLayoutParams();
        int leftMargin = currentLayoutParams.leftMargin;
        int topMargin = currentLayoutParams.topMargin;
        final int height = currentLayoutParams.height;
        final int width = currentLayoutParams.width;

        int fullCycleCount = (int)totalAngle / 360;
        float mScale = scale;

        int realendLeftMargin = 0;
        if (height > width) { // 竖直方向矩形
            realendLeftMargin = Math.abs(height - width) / 2;
        } else if (height < width) { // 横方向矩形
            realendLeftMargin = - Math.abs(height - width) / 2;
        } else {  // 正方形
            realendLeftMargin = 0;
        }

//        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(dispatchImageView, "scaleX", mScale, 1f);
//        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(dispatchImageView, "scaleY", mScale, 1f);
//        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(dispatchImageView, "rotation", totalAngle - fullCycleCount * 360, 90);
//
//        dispatchImageView.setPivotX(width / 2);
//        dispatchImageView.setPivotY(height / 2);
//
//        int realendLeftMargin = 0;
//        if (height > width) { // 竖直方向矩形
//            realendLeftMargin = Math.abs(height - width) / 2;
//        } else if (height < width) { // 横方向矩形
//            realendLeftMargin = - Math.abs(height - width) / 2;
//        } else {  // 正方形
//            realendLeftMargin = 0;
//        }
//        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(dispatchImageView, "translationX", leftMargin, realendLeftMargin);
//        ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(dispatchImageView, "translationY", topMargin,
//                (ScreenUtil.getScreenHeight(dispatchImageView.getContext()) - height) / 2);

//        ValueAnimator leftAnimator = ValueAnimator.ofInt(leftMargin, realendLeftMargin);
//        leftAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                int currentValue = (Integer) valueAnimator.getAnimatedValue();
//                ((FrameLayout.LayoutParams) dispatchImageView.getLayoutParams()).leftMargin = currentValue;
//                dispatchImageView.setLayoutParams(dispatchImageView.getLayoutParams());
//            }
//        });
//        ValueAnimator topAnimator = ValueAnimator.ofInt(topMargin, (ScreenUtil.getScreenHeight(dispatchImageView.getContext()) - height) / 2);
//        topAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                int currentValue = (Integer) valueAnimator.getAnimatedValue();
//                ((FrameLayout.LayoutParams) dispatchImageView.getLayoutParams()).topMargin = currentValue;
//                dispatchImageView.setLayoutParams(dispatchImageView.getLayoutParams());
//            }
//        });

        float deltaY = topMargin - (ScreenUtil.getScreenHeight(dispatchImageView.getContext()) - height) / 2;

        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat("scaleX", mScale, 1f);
        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat("scaleY", mScale, 1f);
        PropertyValuesHolder rotationHolder = PropertyValuesHolder.ofFloat("rotation", totalAngle - fullCycleCount * 360, 90);
//        PropertyValuesHolder translationXHolder = PropertyValuesHolder.ofFloat("translationX", 0, - leftMargin + realendLeftMargin);
//        PropertyValuesHolder translationYHolder = PropertyValuesHolder.ofFloat("translationY", 0,
//                - deltaY);

        dispatchImageView.setPivotX(width / 2);
        dispatchImageView.setPivotY(height / 2);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(dispatchImageView, scaleXHolder, scaleYHolder,
                rotationHolder);

        ValueAnimator leftAnimator = ValueAnimator.ofInt(leftMargin, realendLeftMargin);
        leftAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int currentValue = (Integer) valueAnimator.getAnimatedValue();
                ((FrameLayout.LayoutParams) dispatchImageView.getLayoutParams()).leftMargin = currentValue;
                dispatchImageView.setLayoutParams(dispatchImageView.getLayoutParams());
            }
        });
        ValueAnimator topAnimator = ValueAnimator.ofInt(topMargin, (ScreenUtil.getScreenHeight(dispatchImageView.getContext()) - height) / 2);
        topAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int currentValue = (Integer) valueAnimator.getAnimatedValue();
                ((FrameLayout.LayoutParams) dispatchImageView.getLayoutParams()).topMargin = currentValue;
                dispatchImageView.setLayoutParams(dispatchImageView.getLayoutParams());
            }
        });

        dispatchImageView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        final long startTime = System.currentTimeMillis();
        Log.i("XXX", "start:" + startTime);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator, leftAnimator, topAnimator);
        animatorSet.setDuration(800);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                dispatchImageView.setLayerType(View.LAYER_TYPE_NONE, null);

                long endTime = System.currentTimeMillis();
                Log.i("XXX", "end:" + endTime + "  delta:" + (endTime - startTime));

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
