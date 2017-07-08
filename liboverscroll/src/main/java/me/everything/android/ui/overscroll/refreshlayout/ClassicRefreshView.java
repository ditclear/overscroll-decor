package me.everything.android.ui.overscroll.refreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import me.everything.R;


public class ClassicRefreshView extends FrameLayout implements Loadable {


    private TextView tvRefresh;

    private AVLoadingIndicatorView progressBar;


    private CharSequence completeStr = "COMPLETE";
    private CharSequence failureStr = "REFRESH FAILURE";
    private CharSequence refreshingStr = "REFRESHING";
    private CharSequence pullStr = "PULL TO REFRESH";
    private CharSequence releaseStr = "RELEASE TO REFRESH";

    public ClassicRefreshView(Context context) {
        this(context, null);
    }

    public ClassicRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_classic_refresh, null);
        addView(v);
    }


    public CharSequence getReleaseStr() {
        return releaseStr;
    }

    public void setReleaseStr(CharSequence releaseStr) {
        this.releaseStr = releaseStr;
    }

    public CharSequence getPullStr() {
        return pullStr;
    }

    public void setPullStr(CharSequence pullStr) {
        this.pullStr = pullStr;
    }

    public CharSequence getRefreshingStr() {
        return refreshingStr;
    }

    public void setRefreshingStr(CharSequence refreshingStr) {
        this.refreshingStr = refreshingStr;
    }

    public CharSequence getCompleteStr() {
        return completeStr;
    }

    public void setCompleteStr(CharSequence completeStr) {
        this.completeStr = completeStr;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        tvRefresh = (TextView) findViewById(R.id.tvRefresh);

        progressBar = (AVLoadingIndicatorView) findViewById(R.id.progress_bar);

        progressBar.smoothToShow();
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
        tvRefresh.setText(pullStr);

    }

    @Override
    public void onPrepare() {
        tvRefresh.setText(releaseStr);
    }


    @Override
    public void onComplete() {
        tvRefresh.setText(completeStr);
    }

    @Override
    public void onFailure() {
        tvRefresh.setText(failureStr);
    }

    @Override
    public void onLoad() {
        tvRefresh.setText(refreshingStr);
    }

    @Override
    public void onPositionChange(float currentPercent) {


//        ViewCompat.animate(progressBar).rotation(Math.abs(currentPercent) * 360);

    }

}
