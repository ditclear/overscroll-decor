package me.everything.android.ui.overscroll.adapters;

import android.support.v4.widget.NestedScrollView;
import android.view.View;

/**
 * 页面描述：
 *
 * Created by ditclear on 2017/7/8.
 */

public class NestedScrollViewOverScrollDecorAdapter implements IOverScrollDecoratorAdapter {
    protected final NestedScrollView mView;

    public NestedScrollViewOverScrollDecorAdapter(NestedScrollView view) {
        this.mView = view;
    }

    public View getView() {
        return this.mView;
    }

    public boolean isInAbsoluteStart() {
        return !this.mView.canScrollVertically(-1);
    }

    public boolean isInAbsoluteEnd() {

        return !this.mView.canScrollVertically(1);
    }

}