package me.everything.overscrolldemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import me.everything.overscrolldemo.R;


public class ClassicRefreshView extends FrameLayout implements Loadable {

    private ImageView ivArrow;


    private TextView tvRefresh;

    private ProgressBar progressBar;


    private Animation rotateUp;

    private Animation rotateDown;
    private Animation rotate;

    private boolean rotated = false;

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

        rotateUp = AnimationUtils.loadAnimation(context, R.anim.rotate_up);
        rotateDown = AnimationUtils.loadAnimation(context, R.anim.rotate_down);
        rotate = AnimationUtils.loadAnimation(context, R.anim.rotate);
        rotate.setRepeatMode(Animation.INFINITE);

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
        ivArrow = (ImageView) findViewById(R.id.ivArrow);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
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
        rotated = false;
        ivArrow.clearAnimation();
        ivArrow.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        tvRefresh.setText(pullStr);

    }

    @Override
    public void onPrepare() {
        tvRefresh.setText(releaseStr);
        if (!rotated) {
            ivArrow.clearAnimation();
            ivArrow.startAnimation(rotateUp);
            progressBar.setVisibility(GONE);
            rotated = true;
        }
    }


    @Override
    public void onComplete() {
        rotated = false;

        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        tvRefresh.setText(completeStr);
    }

    @Override
    public void onFailure() {
        rotated = false;

        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        tvRefresh.setText(failureStr);
    }

    @Override
    public void onLoad() {
        rotated = false;
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
//        progressBar.startAnimation(rotate);
        tvRefresh.setText(refreshingStr);
    }

    @Override
    public void onPositionChange(float currentPercent) {


        ivArrow.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);

        if (currentPercent < 1) {
            if (rotated) {
                ivArrow.clearAnimation();
                ivArrow.startAnimation(rotateDown);
                rotated = false;
            }

            tvRefresh.setText(pullStr);
        } else {
            tvRefresh.setText(releaseStr);
            if (!rotated) {
                ivArrow.clearAnimation();
                ivArrow.startAnimation(rotateUp);
                rotated = true;
            }

        }
    }

}
