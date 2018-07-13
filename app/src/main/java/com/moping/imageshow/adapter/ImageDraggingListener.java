package com.moping.imageshow.adapter;

import android.view.View;

public interface ImageDraggingListener {

    void viewCaptured(View captureView);
    void clamLeftTop(View captureView, int left, int top);
    void released(View releasedChild, float totalAngle, float scale, ResetCallBack callBack);

    public interface ResetCallBack {
        void resetTotalAngle(float resetAngle);
        void resetScale();
    }

}
