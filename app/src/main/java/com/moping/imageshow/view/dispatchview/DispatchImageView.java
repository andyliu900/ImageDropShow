package com.moping.imageshow.view.dispatchview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.moping.imageshow.R;
import com.moping.imageshow.adapter.ImageDraggingListener;

/**
 * 带分发功能，内嵌ImageView的视图
 *
 */
public class DispatchImageView extends RelativeLayout implements View.OnClickListener {

    private ImageView image;
    private View icon_left_layout;
    private View icon_right_layout;
    private View icon_up_layout;
    private View icon_down_layout;
    private ImageView iconLeft;
    private ImageView iconRight;
    private ImageView iconUp;
    private ImageView iconDown;
    public float mScale = 1f;
    public float mTotalAngle = 0.0f;

    // true：是原ImageView，可以进行分发操作    false：不是原ImageView，不能进行分发操作
    private boolean isOriginalImageView = false;

    private ImageDraggingListener.ResetCallBack mCallBack;

    public DispatchImageView(Context context) {
        this(context, null);
    }

    public DispatchImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DispatchImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View container = LayoutInflater.from(getContext()).inflate(R.layout.dispatch_container, null);
        image = (ImageView)container.findViewById(R.id.image);
        icon_left_layout = container.findViewById(R.id.icon_left_layout);
        icon_right_layout = container.findViewById(R.id.icon_right_layout);
        icon_up_layout = container.findViewById(R.id.icon_up_layout);
        icon_down_layout = container.findViewById(R.id.icon_down_layout);
        icon_left_layout.setOnClickListener(this);
        icon_right_layout.setOnClickListener(this);
        icon_up_layout.setOnClickListener(this);
        icon_down_layout.setOnClickListener(this);
        iconLeft = (ImageView)container.findViewById(R.id.iconLeft);
        iconRight = (ImageView)container.findViewById(R.id.iconRight);
        iconUp = (ImageView)container.findViewById(R.id.iconUp);
        iconDown = (ImageView)container.findViewById(R.id.iconDown);

        addView(container);
    }

    public void setIsOriginalImageView(boolean flag) {
        isOriginalImageView = flag;
    }

    public void setImageLayoutParams(RelativeLayout.LayoutParams layoutParams) {
        if (image != null) {
            image.setLayoutParams(layoutParams);
        }
    }

    public void setViewInFront(boolean flag) {
        if (flag) {
            iconLeft.setVisibility(View.VISIBLE);
            iconRight.setVisibility(View.VISIBLE);
            iconUp.setVisibility(View.VISIBLE);
            iconDown.setVisibility(View.VISIBLE);
        } else {
            iconLeft.setVisibility(View.GONE);
            iconRight.setVisibility(View.GONE);
            iconUp.setVisibility(View.GONE);
            iconDown.setVisibility(View.GONE);
        }
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(Bitmap bitmap) {
        if (image != null) {
            image.setImageBitmap(bitmap);
            showArrowAnimation();
        }
    }

    public void setRotate(float totalAngle, ImageDraggingListener.ResetCallBack callBack) {
        mCallBack = callBack;
        mTotalAngle = totalAngle;
    }

    public void setScale(float scale) {
        mScale = scale;
    }

    private void showArrowAnimation() {
        float leftX = iconLeft.getTranslationX();
        ObjectAnimator animatorLeft = ObjectAnimator.ofFloat(iconLeft, "translationX", leftX, 20, leftX);
        animatorLeft.setDuration(800);
        animatorLeft.setRepeatCount(ValueAnimator.INFINITE);
        animatorLeft.setRepeatMode(ValueAnimator.REVERSE);
        animatorLeft.start();

        float upY = iconUp.getTranslationY();
        ObjectAnimator animatorUp = ObjectAnimator.ofFloat(iconUp, "translationY", upY, 20, upY);
        animatorUp.setDuration(800);
        animatorUp.setRepeatCount(ValueAnimator.INFINITE);
        animatorUp.setRepeatMode(ValueAnimator.REVERSE);
        animatorUp.start();

        float rightX = iconLeft.getTranslationX();
        ObjectAnimator animatorRight = ObjectAnimator.ofFloat(iconRight, "translationX", rightX, -20, rightX);
        animatorRight.setDuration(800);
        animatorRight.setRepeatCount(ValueAnimator.INFINITE);
        animatorRight.setRepeatMode(ValueAnimator.REVERSE);
        animatorRight.start();

        float downY = iconLeft.getTranslationY();
        ObjectAnimator animatorDown = ObjectAnimator.ofFloat(iconDown, "translationY", downY, -20, downY);
        animatorDown.setDuration(800);
        animatorDown.setRepeatCount(ValueAnimator.INFINITE);
        animatorDown.setRepeatMode(ValueAnimator.REVERSE);
        animatorDown.start();
    }

    @Override
    public void onClick(View view) {
        StrategyContext strategyContext = new StrategyContext();
        switch (view.getId()) {
            case R.id.icon_left_layout:
                strategyContext.setStrategy(new LeftStrategy());
                if (mCallBack != null) {
                    mCallBack.resetTotalAngle(90);
                    mCallBack.resetScale();
                }
                break;
            case R.id.icon_right_layout:
                strategyContext.setStrategy(new RightStrategy());
                if (mCallBack != null) {
                    mCallBack.resetTotalAngle(-90);
                    mCallBack.resetScale();
                }
                break;
            case R.id.icon_up_layout:
                strategyContext.setStrategy(new TopStrategy());
                if (mCallBack != null) {
                    mCallBack.resetTotalAngle(180);
                    mCallBack.resetScale();
                }
                break;
            case R.id.icon_down_layout:
                strategyContext.setStrategy(new BottomStrategy());
                if (mCallBack != null) {
                    mCallBack.resetTotalAngle(0);
                    mCallBack.resetScale();
                }
                break;
                default:
        }
        strategyContext.setDispatchImageView(this);

        setViewInFront(false);
    }
}
