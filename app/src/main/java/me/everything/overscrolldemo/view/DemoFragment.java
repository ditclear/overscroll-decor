package me.everything.overscrolldemo.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.everything.overscrolldemo.R;
import me.everything.overscrolldemo.control.DemoContentHelper;

/**
 * @author amitd
 */
public class DemoFragment extends Fragment {

    private static final int GRID_SPAN_COUNT = 2;

    private NestedScrollView childView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.coordinator_layout_demo, null, false);
        childView = (NestedScrollView) fragmentView.findViewById(R.id.scroll_view);
//        initVerticalRecyclerView((RecyclerView) fragmentView.findViewById(R.id.recycler_view));
        initAppBarLayout((CoordinatorLayout) fragmentView.findViewById(R.id.content_view));
        return fragmentView;
    }

    private void initAppBarLayout(CoordinatorLayout coordinatorLayout) {

        OverScrollDecoratorHelper.setUpOverScroll(coordinatorLayout, childView);


    }

    private void initVerticalRecyclerView(RecyclerView recyclerView) {
        LayoutInflater appInflater = LayoutInflater.from(getActivity().getApplicationContext());
        final DemoRecyclerAdapterBase adapter = new DemoRecyclerAdapterVertical(
                new ArrayList<>(DemoContentHelper.getReverseSpectrumItems(getResources())),
                appInflater);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(GRID_SPAN_COUNT,
                StaggeredGridLayoutManager.VERTICAL));
    }

}
