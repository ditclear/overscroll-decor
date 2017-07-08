package me.everything.overscrolldemo.view;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.everything.android.ui.overscroll.refreshlayout.RefreshLayout;
import me.everything.overscrolldemo.R;
import me.everything.overscrolldemo.control.DemoContentHelper;
import me.everything.overscrolldemo.widget.SlideInItemAnimator;

/**
 * @author amitd
 */
public class CoordinatorLayoutDemoFragment extends Fragment implements
        RefreshLayout.OnRefreshListener, RefreshLayout.OnLoadMoreListener {

    private static final int GRID_SPAN_COUNT = 2;
    int i = 0;
    private RefreshLayout refreshlayout;
    private DemoRecyclerAdapterVertical adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.coordinator_demo, null,
                false);
        refreshlayout = (RefreshLayout) fragmentView.findViewById(R.id.refresh_layout);
        refreshlayout.setOnLoadMoreListener(this);
        refreshlayout.setOnRefreshListener(this);
        initVerticalRecyclerView((RecyclerView) fragmentView.findViewById(R.id.scroll_view));
        return fragmentView;
    }

    private void initVerticalRecyclerView(RecyclerView recyclerView) {
        LayoutInflater appInflater = LayoutInflater.from(getActivity().getApplicationContext());
        adapter = new DemoRecyclerAdapterVertical(
                new ArrayList<>(DemoContentHelper.getReverseSpectrumItems(getResources())),
                appInflater);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        recyclerView.setItemAnimator(new SlideInItemAnimator());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshlayout.setRefreshing(true);

    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.addItems(
                        new ArrayList<>(DemoContentHelper.getReverseSpectrumItems(getResources())));
                refreshlayout.loadSuccess();
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshlayout.loadSuccess();

                adapter.replace(
                        new ArrayList<>(DemoContentHelper.getReverseSpectrumItems(getResources())));


            }
        }, 2000);
    }
}