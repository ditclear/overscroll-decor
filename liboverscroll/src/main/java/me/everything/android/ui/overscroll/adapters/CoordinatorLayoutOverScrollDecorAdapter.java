package me.everything.android.ui.overscroll.adapters;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollUpdateListener;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by ditclear on 2017/7/8.
 */

public class CoordinatorLayoutOverScrollDecorAdapter implements IOverScrollDecoratorAdapter {

    protected final CoordinatorLayout mView;
    protected final AppBarLayout mAppBarLayout;

    protected RecyclerView mRecyclerView;
    protected NestedScrollView mScrollView;
    int offset = 0;

    private IOverScrollDecor mDecor;
    private CallBack mCallBack;

    public CoordinatorLayoutOverScrollDecorAdapter(CoordinatorLayout view) {
        this.mView = view;
        if (view.getChildAt(0) instanceof AppBarLayout) {
            mAppBarLayout = (AppBarLayout) view.getChildAt(0);
            mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    offset = verticalOffset;
                    if (offset == 0) {
                        mDecor.attach();
                    } else {
                        mDecor.detach();
                    }
                    if (mCallBack != null) {
                        mCallBack.onOffsetChanged(appBarLayout, verticalOffset);
                    }
                    Log.d("coor", " verticalOffset = [" + verticalOffset + "]");
                }
            });
        } else {
            mAppBarLayout = null;
        }


    }

    public CoordinatorLayoutOverScrollDecorAdapter initRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;

        mDecor = OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView,
                OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        mDecor.setOverScrollUpdateListener(new IOverScrollUpdateListener() {
            @Override
            public void onOverScrollUpdate(IOverScrollDecor decor, int state,
                    float offset) {
                ViewCompat.setTranslationY(mAppBarLayout, offset);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (offset == 0 || !mRecyclerView.canScrollVertically(1)) {
                    mDecor.attach();
                } else {
                    mDecor.detach();
                }
            }
        });
        return this;
    }

    public CoordinatorLayoutOverScrollDecorAdapter initNestedScrollView(
            NestedScrollView scrollView) {
        this.mScrollView = scrollView;

        mDecor = OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        mDecor.setOverScrollUpdateListener(new IOverScrollUpdateListener() {
            @Override
            public void onOverScrollUpdate(IOverScrollDecor decor, int state,
                    float offset) {
                if (mAppBarLayout != null) {
                    ViewCompat.setTranslationY(mAppBarLayout, offset);
                }
            }
        });

        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
                    int oldScrollY) {
                if (offset == 0 || !ViewCompat.canScrollVertically(mScrollView, 1)) {
                    mDecor.attach();
                } else {
                    mDecor.detach();
                }
            }
        });
        return this;
    }

    @Override
    public View getView() {
        return this.mView;
    }

    @Override
    public boolean isInAbsoluteStart() {
        return offset == 0;
    }

    @Override
    public boolean isInAbsoluteEnd() {
        return false;
    }

    public void setCallBack(
            CallBack callBack) {
        mCallBack = callBack;
    }

    public interface CallBack {
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset);
    }
}
