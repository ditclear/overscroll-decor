package me.everything.android.ui.overscroll;

import static me.everything.android.ui.overscroll.IOverScrollState.STATE_BOUNCE_BACK;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_DRAG_END_SIDE;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_DRAG_START_SIDE;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_IDLE;
import static me.everything.android.ui.overscroll.IOverScrollState.STATE_LOADING;

import android.support.annotation.IntDef;

/**
 * @author amit
 */
@IntDef({STATE_IDLE, STATE_DRAG_START_SIDE, STATE_DRAG_END_SIDE, STATE_LOADING, STATE_BOUNCE_BACK})
public @interface IOverScrollState {

    /** No over-scroll is in-effect. */
    int STATE_IDLE = 0;

    /**
     * User is actively touch-dragging, thus enabling over-scroll at the view's <i>start</i>
     * side.
     */
    int STATE_DRAG_START_SIDE = 1;

    /** User is actively touch-dragging, thus enabling over-scroll at the view's <i>end</i> side. */
    int STATE_DRAG_END_SIDE = 2;

    int STATE_LOADING = 3;
    /**
     * User has released their touch, thus throwing the view back into place via bounce-back
     * animation.
     */
    int STATE_BOUNCE_BACK = 4;
}
