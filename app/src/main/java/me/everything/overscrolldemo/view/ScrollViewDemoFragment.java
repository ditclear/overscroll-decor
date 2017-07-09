package me.everything.overscrolldemo.view;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.everything.android.ui.overscroll.refreshlayout.RefreshLayout;
import me.everything.overscrolldemo.R;

/**
 * @author amitd
 */
public class ScrollViewDemoFragment extends Fragment implements
        RefreshLayout.OnRefreshListener, RefreshLayout.OnLoadMoreListener {

    private RefreshLayout refreshlayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.scrollview_overscroll_demo, null, false);
        refreshlayout = (RefreshLayout) fragmentView.findViewById(R.id.refresh_layout);
        refreshlayout.setOnLoadMoreListener(this);
        refreshlayout.setOnRefreshListener(this);
        initHorizontalScrollView(
                (HorizontalScrollView) fragmentView.findViewById(R.id.horizontal_scroll_view));
        initVerticalScrollView((ScrollView) fragmentView.findViewById(R.id.content_view));
        return fragmentView;
    }

    private void initHorizontalScrollView(HorizontalScrollView scrollView) {
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
    }

    private void initVerticalScrollView(ScrollView scrollView) {
//        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                refreshlayout.loadSuccess();
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshlayout.loadFailure();


            }
        }, 2000);
    }

}
