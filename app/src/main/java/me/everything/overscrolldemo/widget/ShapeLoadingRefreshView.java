package me.everything.overscrolldemo.widget;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import me.everything.overscrolldemo.R;


/**
 * Created by canyinghao on 16/6/29.
 */
public class ShapeLoadingRefreshView extends FrameLayout implements Loadable {


    private final ObjectAnimator animator;
    private Animation rotate = null;

    private View progress;

    public ShapeLoadingRefreshView(Context context) {
        this(context, null);
    }

    public ShapeLoadingRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeLoadingRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        rotate = AnimationUtils.loadAnimation(context, R.anim.rotate);
        rotate.setRepeatMode(Animation.INFINITE);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_shape_loading, null);
        addView(v);
        animator = ObjectAnimator.ofFloat(progress, "rotation",
                360).setDuration(300);
        animator.setRepeatCount(ValueAnimator.INFINITE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        progress = findViewById(R.id.progressbar);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, 150);
        } else if (widthMeasureSpec == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 150);
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onReset() {
//        progress.clearAnimation();
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onLoad() {
//        progress.startAnimation(rotate);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onPositionChange(float currentPercent) {
//        ViewCompat.animate(progress).rotation(Math.abs(currentPercent) * 360);
    }

    @Override
    public void onFailure() {

    }

}
