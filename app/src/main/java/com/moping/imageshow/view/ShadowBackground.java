package com.moping.imageshow.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;

/**
 * 阴影背景
 */
public class ShadowBackground extends Drawable {

    private Paint mPatint;
    private int mShadowRadius;
//    private int mViewWidth;
//    private int mViewHeight;
    private RectF mRect;

    private ShadowBackground(int shadowColor, int shadowRadius) {
//        this.mViewWidth = viewWidth;
//        this.mViewHeight = viewHeight;
        this.mShadowRadius = shadowRadius;
        mPatint = new Paint();
        mPatint.setColor(Color.TRANSPARENT);
        mPatint.setAntiAlias(true);
        mPatint.setShadowLayer(shadowRadius, 0, 0, shadowColor);
        mPatint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
//        Log.i("XXX", "mViewWidth:" + mViewWidth + " mViewHeight:" + mViewHeight);
        Log.i("XXX", "left:" + left + " top:" + top + " right:" + right + " bottom:" + bottom);
        mRect = new RectF(left + mShadowRadius, top + mShadowRadius,
                right - mShadowRadius, bottom - mShadowRadius);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRoundRect(mRect, mShadowRadius, mShadowRadius, mPatint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPatint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPatint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public static void setShadowBackground(View view, Drawable drawable) {
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

    public static void setShadowBackground(View view) {
        ShadowBackground drawable = new ShadowBackground.Builder()
                .builder();
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

    public static void setShadowBackground(View view, int shadowColor, int shadowRadius) {
        ShadowBackground drawable = new ShadowBackground.Builder()
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .builder();
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

    public static class Builder {
//        private int mViewWidth;
//        private int mViewHeight;
        private int mShadowColor;
        private int mShadowRadius;

        public Builder() {
            mShadowColor = Color.parseColor("#4d000000");
            mShadowRadius = 18;
        }

//        public ShadowBackground.Builder setWidth(int width) {
//            this.mViewWidth = width;
//            return this;
//        }
//
//        public ShadowBackground.Builder setHeight(int height) {
//            this.mViewHeight = height;
//            return this;
//        }

        public ShadowBackground.Builder setShadowColor(int shadowColor) {
            this.mShadowColor = shadowColor;
            return this;
        }

        public ShadowBackground.Builder setShadowRadius(int shadowRadius) {
            this.mShadowRadius = shadowRadius;
            return this;
        }

        public ShadowBackground builder() {
            return new ShadowBackground(mShadowColor, mShadowRadius);
        }
    }
}
