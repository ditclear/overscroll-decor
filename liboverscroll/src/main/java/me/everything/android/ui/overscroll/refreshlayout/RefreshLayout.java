package me.everything.android.ui.overscroll.refreshlayout;

import static android.R.attr.orientation;

import static me.everything.android.ui.overscroll.OverScrollDecoratorHelper.setUpStaticOverScroll;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import me.everything.R;
import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollState;
import me.everything.android.ui.overscroll.IOverScrollStateListener;
import me.everything.android.ui.overscroll.IOverScrollUpdateListener;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter;

/**
 * 页面描述：RefreshLayout
 *
 * Created by ditclear on 2017/7/5.
 */

public class RefreshLayout extends FrameLayout implements IOverScrollUpdateListener,
        IOverScrollStateListener {


    //   下拉监听
    protected OnRefreshListener mOnRefreshListener = new ListenerStubs.OnRefreshListenerStub();
    //    上拉监听
    protected OnLoadMoreListener mOnLoadMoreListener = new ListenerStubs.OnLoadMoreListenerStub();

    protected OnScrollUpdateListener mOnScrollUpdateListener = new ListenerStubs
            .OnScrollUpdateListenerStub();
    TextView mTextView;
    private IOverScrollDecor decor = null;
    private View mHeaderView;
    private View mContentView;
    private View mFooterView;
    private int mHeaderHeight, mFooterHeight;
    private AppBarLayout mAppBarLayout;
    private boolean isRefreshing;
    private int offset = 0;

    public RefreshLayout(Context context) {
        super(context);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Loadable getIHeader() {
        return (Loadable) mHeaderView;
    }

    private Loadable getIFooter() {
        return (Loadable) mFooterView;
    }

    @Override
    public void onOverScrollUpdate(IOverScrollDecor scrollDecor, int state,
            float offset) {
        ViewCompat.setTranslationY(mHeaderView, offset);
        ViewCompat.setTranslationY(mFooterView, offset);

        this.isRefreshing = offset >= 0;

        if (scrollDecor.getView() instanceof AppBarLayout) {
            //代表是AppBarLayout
            ViewCompat.setTranslationY(decor.getView(), offset);
        } else if (mAppBarLayout != null && isRefreshing) {
            ViewCompat.setTranslationY(mAppBarLayout, offset);
        }
        if (state != IOverScrollState.STATE_LOADING) {
            getIHeader().onPositionChange(offset / mHeaderHeight);
            getIFooter().onPositionChange(offset / mHeaderHeight);
        }
        mOnScrollUpdateListener.onScrollUpdate(state, offset);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final int childCount = getChildCount();

        if (childCount > 0) {
            mHeaderView = findViewById(R.id.refresh_header);
            mContentView = findViewById(R.id.content_view);
            mFooterView = findViewById(R.id.refresh_footer);
        }

        if (mContentView == null) {
            throw new IllegalStateException("mContentView is null");
        }
        if (mHeaderView == null) {
            mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.normal_header,
                    null);
            addView(mHeaderView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }
        if (mFooterView == null) {
            mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.normal_header,
                    null);
            addView(mFooterView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }

        if (mHeaderView != null && !(mHeaderView instanceof Loadable)) {

            throw new IllegalStateException("mHeaderView  error");
        }
        if (mFooterView != null && !(mFooterView instanceof Loadable)) {

            throw new IllegalStateException("mFooterView error");
        }


        initContentView();

    }

    private void initContentView() {

        if (mContentView instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) mContentView;
            initRecyclerOverScroll(recyclerView);
        } else if (mContentView instanceof ListView) {
            decor = OverScrollDecoratorHelper.setUpOverScroll((ListView) mContentView);
        } else if (mContentView instanceof GridView) {
            decor = OverScrollDecoratorHelper.setUpOverScroll((GridView) mContentView);
        } else if (mContentView instanceof ScrollView) {
            decor = OverScrollDecoratorHelper.setUpOverScroll((ScrollView) mContentView);
        } else if (mContentView instanceof CoordinatorLayout) {
            if (((CoordinatorLayout) mContentView).getChildAt(0) instanceof AppBarLayout) {
                mAppBarLayout = (AppBarLayout) ((CoordinatorLayout) mContentView).getChildAt(0);
                IOverScrollDecor scrollDecor = OverScrollDecoratorHelper.setUpStaticOverScroll(
                        mAppBarLayout,
                        OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
                scrollDecor.setOverScrollUpdateListener(this);
                scrollDecor.setOverScrollStateListener(this);

                mAppBarLayout.addOnOffsetChangedListener(
                        new AppBarLayout.OnOffsetChangedListener() {
                            @Override
                            public void onOffsetChanged(AppBarLayout appBarLayout,
                                    int verticalOffset) {
                                offset = verticalOffset;
                                if (offset == 0) {
                                    decor.attach();
                                } else {
                                    decor.detach();
                                }
                            }
                        });
            }
            if (mContentView.findViewById(R.id.scroll_view) instanceof NestedScrollView) {
                decor = OverScrollDecoratorHelper.setUpOverScroll(
                        (NestedScrollView) mContentView.findViewById(R.id.scroll_view));
            } else if (mContentView.findViewById(R.id.scroll_view) instanceof RecyclerView) {
                initRecyclerOverScroll((RecyclerView) mContentView.findViewById(R.id.scroll_view));
            } else {
                decor = OverScrollDecoratorHelper.setUpOverScroll((CoordinatorLayout) mContentView,
                        this, this);
            }

        } else {
            decor = setUpStaticOverScroll(mContentView,
                    OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        }
        decor.setOverScrollUpdateListener(this);
        decor.setOverScrollStateListener(this);
    }

    private IOverScrollDecor initRecyclerOverScroll(RecyclerView recyclerView) {
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager == null) {
            decor = new VerticalOverScrollBounceEffectDecorator(
                    new RecyclerViewOverScrollDecorAdapter(
                            recyclerView, true));
        } else if (layoutManager instanceof LinearLayoutManager ||
                layoutManager instanceof StaggeredGridLayoutManager) {
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                throw new UnsupportedOperationException("unSupport orientation HORIZONTAL");
            } else {
                decor = OverScrollDecoratorHelper.setUpOverScroll(recyclerView,
                        OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
            }
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (offset == 0 || !recyclerView.canScrollVertically(1)) {
                    decor.attach();
                } else {
                    decor.detach();
                }
            }
        });
        return decor;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChildWithMargins(mHeaderView, widthMeasureSpec, 0, heightMeasureSpec,
                    0);
            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();

            mHeaderHeight =
                    mHeaderView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

        }

        if (mFooterView != null) {
            measureChildWithMargins(mFooterView, widthMeasureSpec, 0, heightMeasureSpec,
                    0);
            MarginLayoutParams lp = (MarginLayoutParams) mFooterView.getLayoutParams();

            mFooterHeight =
                    mFooterView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

        }
        if (mContentView != null) {


            measureChildWithMargins(mContentView, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }

        int count = getChildCount();

        for (int i = 0; i < count; i++) {

            View v = getChildAt(i);


            if (v != mHeaderView && v != mFooterView && v != mContentView) {

                measureChildWithMargins(v, widthMeasureSpec, 0, heightMeasureSpec, 0);


            }


        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layoutChild();
        if (decor != null && decor.getHeight() != mHeaderHeight) {
            decor.setHeaderHeight(mHeaderHeight);
        }
    }

    private void layoutChild() {

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        if (mHeaderView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int top = paddingTop + lp.topMargin - mHeaderHeight;
            final int right = left + mHeaderView.getMeasuredWidth();
            final int bottom = top + mHeaderView.getMeasuredHeight();


            mHeaderView.layout(left, top, right, bottom);

        }

        if (mFooterView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mFooterView.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int top = getMeasuredHeight() + paddingTop + lp.topMargin;
            final int right = left + mFooterView.getMeasuredWidth();
            final int bottom = top + mFooterView.getMeasuredHeight();
            mFooterView.layout(left, top, right, bottom);

        }
        if (mContentView != null) {

            MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int top = paddingTop + lp.topMargin;
            final int right = left + mContentView.getMeasuredWidth();
            final int bottom = top + mContentView.getMeasuredHeight();

            mContentView.layout(left, top, right, bottom);
        }


        int count = getChildCount();

        for (int i = 0; i < count; i++) {

            View v = getChildAt(i);

            if (v != mHeaderView && v != mFooterView && v != mContentView) {

                MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();
                int left = paddingLeft + lp.leftMargin;
                int top = paddingTop + lp.topMargin;

                int right = left + v.getMeasuredWidth();
                int bottom = top + v.getMeasuredHeight();

                v.layout(left, top, right, bottom);

            }


        }
    }

    @Override
    public void onOverScrollStateChange(IOverScrollDecor decor, int oldState, int newState) {

        @IOverScrollState
        int state = newState;

        switch (state) {

            case IOverScrollState.STATE_BOUNCE_BACK:

                break;
            case IOverScrollState.STATE_DRAG_END_SIDE:
                getIHeader().onPrepare();
                break;
            case IOverScrollState.STATE_DRAG_START_SIDE:
                getIHeader().onReset();
                break;
            case IOverScrollState.STATE_IDLE:
                getIHeader().onReset();
                break;
            case IOverScrollState.STATE_LOADING:
                if (isRefreshing) {
                    getIHeader().onLoad();
                    mOnRefreshListener.onRefresh();
                } else {
                    getIFooter().onLoad();
                    mOnLoadMoreListener.onLoadMore();
                }

                break;
        }
    }

    /**
     * 设置刷新监听
     *
     * @param listener OnRefreshListener
     */
    public void setOnRefreshListener(@NonNull OnRefreshListener listener) {
        this.mOnRefreshListener =
                (listener != null ? listener : new ListenerStubs.OnRefreshListenerStub());
        ;
    }

    /**
     * 设置加载更多监听
     *
     * @param listener OnLoadMoreListener
     */
    public void setOnLoadMoreListener(@NonNull OnLoadMoreListener listener) {
        this.mOnLoadMoreListener = (listener != null ? listener : new ListenerStubs
                .OnLoadMoreListenerStub());
    }

    /**
     * 设置加载更多监听
     *
     * @param listener OnLoadMoreListener
     */
    public void setOnScrollUpdateListener(
            OnScrollUpdateListener listener) {
        this.mOnScrollUpdateListener = (listener != null ? listener : new ListenerStubs
                .OnScrollUpdateListenerStub());
    }

    public void loadSuccess() {
        if (isRefreshing) {
            getIHeader().onComplete();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    decor.setCurrentState(IOverScrollState.STATE_BOUNCE_BACK);
                }
            }, 1000);
        } else {
            decor.setCurrentState(IOverScrollState.STATE_BOUNCE_BACK);
        }

    }

    public void loadFailure() {
        if (isRefreshing) {
            getIHeader().onFailure();
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                decor.setCurrentState(IOverScrollState.STATE_BOUNCE_BACK);
            }
        }, 1000);
    }

    public boolean isRefreshing() {
        return this.decor.getCurrentState() == IOverScrollState.STATE_LOADING;
    }

    public void setRefreshing(boolean refreshing) {

        if (mHeaderHeight == 0) {
            int widthSpec = MeasureSpec.makeMeasureSpec((1 << 30) - 1, MeasureSpec.AT_MOST);
            int heightSpec = MeasureSpec.makeMeasureSpec((1 << 30) - 1, MeasureSpec.AT_MOST);
            mHeaderView.measure(widthSpec, heightSpec);
            mHeaderHeight = mHeaderView.getMeasuredHeight();
            decor.setHeaderHeight(mHeaderHeight);
        }
        if (refreshing) {
            if (mHeaderView != null && !isRefreshing()) {
                ViewCompat.setTranslationY(decor.getView(),
                        mHeaderHeight * 1.5f);
                decor.setCurrentState(IOverScrollState.STATE_LOADING);
                onOverScrollUpdate(decor, IOverScrollState.STATE_LOADING, mHeaderHeight * 1.5f);
                onOverScrollStateChange(decor, IOverScrollState.STATE_DRAG_END_SIDE,
                        IOverScrollState.STATE_LOADING);
            }

        } else {
            decor.setCurrentState(IOverScrollState.STATE_BOUNCE_BACK);

        }
    }

    /**
     * 上拉加载监听
     */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    /**
     * 下拉刷新监听
     */
    public interface OnRefreshListener {
        void onRefresh();
    }

    public interface ListenerStubs {

        class OnRefreshListenerStub implements OnRefreshListener {

            @Override
            public void onRefresh() {
            }
        }

        class OnLoadMoreListenerStub implements OnLoadMoreListener {

            @Override
            public void onLoadMore() {
            }
        }

        class OnScrollUpdateListenerStub implements OnScrollUpdateListener {

            @Override
            public void onScrollUpdate(int state, float offset) {

            }
        }

    }

    public interface OnScrollUpdateListener {

        public void onScrollUpdate(int state, float offset);
    }
}
