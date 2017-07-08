package me.everything.overscrolldemo.widget;

import android.view.View;

/**
 * 页面描述：
 *
 * Created by ditclear on 2017/7/5.
 */

public interface Loadable {

    public View getView();

    /**
     * 重置
     */
    public void onReset();


    /**
     * 下拉高度大于头部高度
     */
    public void onPrepare();


    /**
     * 放手后
     */
    public void onLoad();

    /**
     * 刷新完成
     */
    public void onComplete();

    /**
     * 下拉高度与头部高度比例
     */
    public void onPositionChange(float currentPercent);

    /**
     * 失败
     */
    public void onFailure();
}
