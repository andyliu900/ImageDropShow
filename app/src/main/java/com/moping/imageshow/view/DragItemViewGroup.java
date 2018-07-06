package com.moping.imageshow.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public class DragItemViewGroup extends FrameLayout {

    private static final String TAG = DragItemViewGroup.class.getName();

    // 记录手指上次触摸的坐标
    private float mLastPointX;
    private float mLastPointY;

    // 识别最小滑动距离
    private int mSlop;
    // 正在拖动的child
    private View mDragView;

    // 状态：空闲、拖动中
    enum State {
        IDLE,
        DRAGGING
    }

    State mCurrentState;

    public DragItemViewGroup(Context context) {
        this(context, null);
    }

    public DragItemViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragItemViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mSlop = ViewConfiguration.getWindowTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isPointOnViews(event)) {
                    mCurrentState = State.DRAGGING;
                    mLastPointX = event.getX();
                    mLastPointY = event.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int)(event.getX() - mLastPointX);
                int deltaY = (int)(event.getY() - mLastPointY);
                if (mCurrentState == State.DRAGGING && mDragView != null
                        && (Math.abs(deltaX) > mSlop || Math.abs(deltaY) > mSlop)) {
                    ViewCompat.offsetLeftAndRight(mDragView, deltaX);
                    ViewCompat.offsetTopAndBottom(mDragView, deltaY);
                    mLastPointX = event.getX();
                    mLastPointY = event.getY();
                }
                break;
                case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (mCurrentState == State.DRAGGING) {
                            mCurrentState = State.IDLE;
                            mDragView = null;
                        }
                        break;
                default:
        }

        return true;
    }

    private boolean isPointOnViews(MotionEvent ev) {
        boolean result = false;
        Rect rect = new Rect();
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View view = getChildAt(i);
            rect.set((int)view.getX(), (int)view.getY(), (int)view.getX() + (int)view.getWidth(),
                    (int)view.getY() + (int)view.getHeight());
            if (rect.contains((int)ev.getX(), (int)ev.getY())) {
                // 标记为被拖拽的child
                mDragView = view;
                result = true;
                break;
            }
        }

        return result && mCurrentState != State.DRAGGING;
    }
}
