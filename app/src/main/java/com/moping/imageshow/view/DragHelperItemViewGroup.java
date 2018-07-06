package com.moping.imageshow.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class DragHelperItemViewGroup extends FrameLayout {

    private static final String TAG = DragHelperItemViewGroup.class.getName();

    private ViewDragHelper mDragHelper;

    private OnDraggingListener mListener;

    public DragHelperItemViewGroup(Context context) {
        this(context, null);
    }

    public DragHelperItemViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragHelperItemViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                if (mListener != null) {
                    mListener.viewCaptured(capturedChild);
                }
            }

            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                if (mListener != null) {
                    mListener.clamLeft(left, dx);
                }
                return left;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                if (mListener != null) {
                    mListener.clamTop(top, dy);
                }
                return top;
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (mListener != null) {
                    mListener.released(releasedChild);
                }
                mDragHelper.flingCapturedView(getPaddingLeft(), getPaddingTop(),
                        getWidth() - getPaddingRight() - releasedChild.getWidth(),
                        getHeight() - getPaddingBottom() - releasedChild.getHeight());

                invalidate();

                setViewPosition(releasedChild, releasedChild.getLeft(), releasedChild.getTop());
            }
        });


    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);

        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper != null && mDragHelper.continueSettling(true)) {
            invalidate();

            setViewPosition(mDragHelper.getCapturedView(), mDragHelper.getCapturedView().getLeft(),
                    mDragHelper.getCapturedView().getTop());
        }
    }

    private void setViewPosition(View view, int marginLeft, int marginTop) {
        ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(
                view.getMeasuredWidth(),
                view.getMeasuredHeight());
        marginParams.setMargins(marginLeft, marginTop, 0, 0);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(marginParams);

        view.setLayoutParams(layoutParams);
    }

    public View getCapturedView() {
        if (mDragHelper != null) {
            View view = mDragHelper.getCapturedView();
            return view;
        } else {
            return null;
        }
    }

    public void setOnDraggingListener(OnDraggingListener listener) {
        mListener = listener;
    }

    public interface OnDraggingListener {
        void viewCaptured(View captureView);
        void clamLeft(int left, int dx);
        void clamTop(int top, int dy);
        void released(View releasedChild);
    }
}
