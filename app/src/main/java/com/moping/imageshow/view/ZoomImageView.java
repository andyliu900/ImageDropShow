package com.moping.imageshow.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.moping.imageshow.R;
import com.moping.imageshow.adapter.ImageDraggingListener;

public class ZoomImageView extends RelativeLayout implements View.OnTouchListener {

    private ImageDraggingListener mImageDraggingListener;

    private ImageView image;
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
    
    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View container = LayoutInflater.from(getContext()).inflate(R.layout.zoom_container, null);
        image = (ImageView)container.findViewById(R.id.image);
        addView(container);
        setOnTouchListener(this);
    }

    public ImageView getImageView() {
        return image;
    }

    public void setImageView(Bitmap bitmap) {
        if (image != null) {
            image.setImageBitmap(bitmap);
        }
    }

    public void setImageDraggingListener(ImageDraggingListener listener) {
        mImageDraggingListener = listener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (view instanceof ZoomImageView) {
            ZoomImageView zoomImageView = (ZoomImageView)view;
            ImageView currentImageView = zoomImageView.getImageView();
            ((BitmapDrawable)currentImageView.getDrawable()).setAntiAlias(true);

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    parms = (FrameLayout.LayoutParams) zoomImageView.getLayoutParams();
                    startwidth = parms.width;
                    startheight = parms.height;
                    dx = event.getRawX() - parms.leftMargin;
                    dy = event.getRawY() - parms.topMargin;

                    mode = DRAG;

                    if (mImageDraggingListener != null) {
                        mImageDraggingListener.viewCaptured(zoomImageView);
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

                        zoomImageView.setLayoutParams(parms);

                        if (mImageDraggingListener != null) {
                            mImageDraggingListener.clamLeftTop(zoomImageView, (int) (x - dx), (int) (y - dy));
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
                                float scale = newDist / oldDist * zoomImageView.getScaleX();
                                if (scale > 0.6) {
                                    scalediff = scale;
                                    zoomImageView.setScaleX(scale);
                                    zoomImageView.setScaleY(scale);
                                }
                            }

                            zoomImageView.animate().rotationBy(angle).setDuration(0).setInterpolator(new LinearInterpolator()).start();

                            x = event.getRawX();
                            y = event.getRawY();

                            parms.leftMargin = (int) ((x - dx) + scalediff);
                            parms.topMargin = (int) ((y - dy) + scalediff);

                            parms.rightMargin = 0;
                            parms.bottomMargin = 0;
                            parms.rightMargin = parms.leftMargin + (5 * parms.width);
                            parms.bottomMargin = parms.topMargin + (10 * parms.height);

                            zoomImageView.setLayoutParams(parms);

                            if (mImageDraggingListener != null) {
                                mImageDraggingListener.clamLeftTop(zoomImageView, (int) (x - dx), (int) (y - dy));
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mImageDraggingListener != null) {
                        mImageDraggingListener.released(zoomImageView, totalAngle, scalediff, new ImageDraggingListener.ResetCallBack() {
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
