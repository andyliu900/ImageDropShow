package com.moping.imageshow.view.dispatchview;

/**
 * 策略Context类
 *
 * Created by randysu on 2018/4/13.
 */

public class StrategyContext {

    Strategy strategy;

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void setDispatchImageView(DispatchImageView dispatchImageView) {
        if (strategy != null) {
            strategy.setDispatchAction(dispatchImageView);
        }
    }

}
