package com.moping.imageshow.view.dispatchview;

import com.moping.imageshow.view.ZoomImageView;

/**
 * 策略接口
 *
 */
public interface Strategy {

    public abstract void setDispatchAction(ZoomImageView dispatchImageView, float totalAngle, float scale);

}
