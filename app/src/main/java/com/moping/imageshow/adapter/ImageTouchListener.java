package com.moping.imageshow.adapter;

import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.moping.imageshow.view.dispatchview.DispatchImageView;

/**
 * 顺时针旋转为正方向
 *
 * 图片的Touch事件，只处理缩放、旋转
 */
public class ImageTouchListener implements View.OnTouchListener {

    private ImageDraggingListener mImageDraggingListener;
    float scalediff = 1f;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;

    // 初始化状态变量
    FrameLayout.LayoutParams parms;
    int startwidth;
    int startheight;
    float dx = 0, dy = 0, x = 0, y = 0;
    float angle = 0;
    float totalAngle = 0.0f;

    public ImageTouchListener() {

    }

    public ImageTouchListener(ImageDraggingListener listener) {
        mImageDraggingListener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v instanceof DispatchImageView) {
            DispatchImageView currentDispatchImageView = (DispatchImageView)v;
            ImageView currentImageView = currentDispatchImageView.getImage();
            ((BitmapDrawable)currentImageView.getDrawable()).setAntiAlias(true);

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    parms = (FrameLayout.LayoutParams) currentDispatchImageView.getLayoutParams();
                    startwidth = parms.width;
                    startheight = parms.height;
                    dx = event.getRawX() - parms.leftMargin;
                    dy = event.getRawY() - parms.topMargin;

                    mode = DRAG;

                    if (mImageDraggingListener != null) {
                        mImageDraggingListener.viewCaptured(currentDispatchImageView);
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        mode = ZOOM;
                    }
                    d = rotation(event);
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;

                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {

                        x = event.getRawX();
                        y = event.getRawY();

                        parms.leftMargin = (int) (x - dx);
                        parms.topMargin = (int) (y - dy);

                        parms.rightMargin = 0;
                        parms.bottomMargin = 0;
                        parms.rightMargin = parms.leftMargin + (5 * parms.width);
                        parms.bottomMargin = parms.topMargin + (10 * parms.height);

                        currentDispatchImageView.setLayoutParams(parms);

                        if (mImageDraggingListener != null) {
                            mImageDraggingListener.clamLeftTop(currentDispatchImageView, (int) (x - dx), (int) (y - dy));
                        }

                    } else if (mode == ZOOM) {
                        if (event.getPointerCount() == 2) {

                            newRot = rotation(event);
                            float r = newRot - d;
                            angle = r;
                            totalAngle += angle;

                            x = event.getRawX();
                            y = event.getRawY();

                            float newDist = spacing(event);
                            if (newDist > 10f) {
                                float scale = newDist / oldDist * currentDispatchImageView.getScaleX();
                                if (scale > 0.6) {
                                    scalediff = scale;
                                    currentDispatchImageView.setScaleX(scale);
                                    currentDispatchImageView.setScaleY(scale);
                                }
                            }

                            currentDispatchImageView.animate().rotationBy(angle).setDuration(0).setInterpolator(new LinearInterpolator()).start();

                            x = event.getRawX();
                            y = event.getRawY();

                            parms.leftMargin = (int) ((x - dx) + scalediff);
                            parms.topMargin = (int) ((y - dy) + scalediff);

                            parms.rightMargin = 0;
                            parms.bottomMargin = 0;
                            parms.rightMargin = parms.leftMargin + (5 * parms.width);
                            parms.bottomMargin = parms.topMargin + (10 * parms.height);

                            currentDispatchImageView.setLayoutParams(parms);

                            if (mImageDraggingListener != null) {
                                mImageDraggingListener.clamLeftTop(currentDispatchImageView, (int) (x - dx), (int) (y - dy));
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mImageDraggingListener != null) {
                        mImageDraggingListener.released(currentDispatchImageView, totalAngle, scalediff, new ImageDraggingListener.ResetCallBack() {
                            @Override
                            public void resetTotalAngle(float resetAngle) {
                                totalAngle = resetAngle;
                            }

                            @Override
                            public void resetScale() {
                                scalediff = 1f;
                            }
                        });
                    }
                    break;
                    default:
            }
            return true;
        } else {
            return false;
        }
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

}
