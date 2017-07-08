package me.everything.android.ui.overscroll.adapters;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollStateListener;
import me.everything.android.ui.overscroll.IOverScrollUpdateListener;
import me.everything.android.ui.overscroll.ListenerStubs;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * 页面描述：
 *
 * Created by ditclear on 2017/7/8.
 */

public class CoordinatorLayoutOverScrollDecorAdapter implements IOverScrollDecoratorAdapter,
        IOverScrollStateListener {

    protected final CoordinatorLayout mView;
    protected final AppBarLayout mAppBarLayout;

    protected RecyclerView mRecyclerView;
    protected NestedScrollView mScrollView;
    protected IOverScrollStateListener mStateListener =
            new ListenerStubs.OverScrollStateListenerStub();
    protected IOverScrollUpdateListener mUpdateListener =
            new ListenerStubs.OverScrollUpdateListenerStub();
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
                }
            });
        } else {
            mAppBarLayout = null;
        }


    }

    public CoordinatorLayoutOverScrollDecorAdapter setOverScrollStateListener(
            IOverScrollStateListener listener) {
        mStateListener =
                (listener != null ? listener : new ListenerStubs.OverScrollStateListenerStub());
        return this;
    }

    public CoordinatorLayoutOverScrollDecorAdapter setOverScrollUpdateListener(
            IOverScrollUpdateListener listener) {
        mUpdateListener =
                (listener != null ? listener : new ListenerStubs.OverScrollUpdateListenerStub());
        return this;
    }

    public CoordinatorLayoutOverScrollDecorAdapter initRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        recyclerView.setLayoutManager(
                new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL,
                        false));

        mDecor = OverScrollDecoratorHelper.setUpStaticOverScroll(mRecyclerView.getRootView(),
                OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        mDecor.setOverScrollUpdateListener(new IOverScrollUpdateListener() {
            @Override
            public void onOverScrollUpdate(IOverScrollDecor decor, int state, float offset) {
                ViewCompat.setTranslationY(mAppBarLayout, offset);
                mUpdateListener.onOverScrollUpdate(decor, state, offset);
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
            public void onOverScrollUpdate(IOverScrollDecor decor, int state, float offset) {
                ViewCompat.setTranslationY(mAppBarLayout, offset);
                mUpdateListener.onOverScrollUpdate(decor, state, offset);
            }
        });
        mDecor.setOverScrollStateListener(this);

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
        return this.mRecyclerView;
    }

    @Override
    public boolean isInAbsoluteStart() {
        return offset == 0;
    }

    @Override
    public boolean isInAbsoluteEnd() {
        return false;
    }

    public CoordinatorLayoutOverScrollDecorAdapter setCallBack(
            CallBack callBack) {
        mCallBack = callBack;
        return this;
    }

    @Override
    public void onOverScrollStateChange(IOverScrollDecor decor, int oldState, int newState) {
//        mStateListener.onOverScrollStateChange(decor, oldState, newState);
    }


    public interface CallBack {
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset);

        public void onOverScrollUpdate(IOverScrollDecor decor, int state, float offset);

        public void onOverScrollStateChange(IOverScrollDecor decor, int oldState, int newState);
    }
}
