package me.everything.overscrolldemo.view;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.refreshlayout.RefreshLayout;
import me.everything.overscrolldemo.R;
import me.everything.overscrolldemo.control.DemoContentHelper;
import me.everything.overscrolldemo.control.DemoItem;

/**
 * @author amitd
 */
public class ListViewDemoFragment extends Fragment implements
        RefreshLayout.OnRefreshListener, RefreshLayout.OnLoadMoreListener {

    private RefreshLayout refreshlayout;
    private DemoListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.listview_overscroll_demo, null, false);
        initVerticalListView(
                new ArrayList<>(DemoContentHelper.getReverseSpectrumItems(getResources())),
                (ListView) fragmentView.findViewById(R.id.content_view));
        refreshlayout = (RefreshLayout) fragmentView.findViewById(R.id.refresh_layout);
        refreshlayout.setOnLoadMoreListener(this);
        refreshlayout.setOnRefreshListener(this);
        return fragmentView;
    }

    private void initVerticalListView(List<DemoItem> content, ListView listView) {
        LayoutInflater appInflater = LayoutInflater.from(getActivity().getApplicationContext());
        adapter = new DemoListAdapter(appInflater, content);
        listView.setAdapter(adapter);

//        OverScrollDecoratorHelper.setUpOverScroll(listView);
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
