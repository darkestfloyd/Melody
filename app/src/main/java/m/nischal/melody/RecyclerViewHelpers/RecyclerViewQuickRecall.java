package m.nischal.melody.RecyclerViewHelpers;

/*
 * Copyright 2015 Nischal M
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import m.nischal.melody.Helper.BusEvents;
import m.nischal.melody.Util.RxBus;
import rx.Subscription;

import static m.nischal.melody.Helper.GeneralHelpers.PicassoHelper.TAG;
import static m.nischal.melody.Helper.GeneralHelpers.PicassoHelper.picassoWrapper;

public class RecyclerViewQuickRecall extends RecyclerView.OnScrollListener {

    private int scrollValue;
    private int primary, black;
    private boolean toolbarVisible, waitForState;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private DrawerLayout drawerLayout;

    private RxBus rxBus = RxBus.getBus();
    private Subscription sc;

    public RecyclerViewQuickRecall(@ColorInt int primary, Toolbar toolbar,
                                   TabLayout tabLayout, DrawerLayout drawerLayout) {
        this.scrollValue = 0;
        this.primary = primary;
        this.black = Color.BLACK;
        this.waitForState = false;
        this.toolbarVisible = true;
        this.drawerLayout = drawerLayout;
        this.toolbar = toolbar;
        this.tabLayout = tabLayout;

        sc = rxBus.toObservable()
                .subscribe(busEvents -> {
                    if (busEvents instanceof BusEvents.ViewPagerPageChanged)
                        if (!toolbarVisible)
                            animate(0, 1f, black, primary, 0, true);
                });
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        sc.unsubscribe();
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        //0 - idle
        //1 - dragging
        //3 - setting

        if (newState == RecyclerView.SCROLL_STATE_IDLE)
            picassoWrapper.resumeTag(TAG);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        scrollValue += dy;

        if (Math.abs(dy) > 50)
            picassoWrapper.pauseTag(TAG);
        else picassoWrapper.resumeTag(TAG);

        if (dy > 10 && scrollValue > 500 && toolbarVisible) {
            animate(-toolbar.getBottom(), 1f, primary, black, -tabLayout.getTop(), false); //hide
        } else if (dy < -10 && !toolbarVisible) {
            animate(0, 1f, black, primary, 0, true); //show
        }
    }

    public void animate(int endy, float endalpha, int sc, int ec, int et, boolean v) {

        toolbarVisible = v;

        toolbar.clearAnimation();
        PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, endy);
        PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat(View.ALPHA, endalpha);
        ObjectAnimator toolBarAnimator = ObjectAnimator.ofPropertyValuesHolder(toolbar, holder1, holder2);

        drawerLayout.clearAnimation();
        ObjectAnimator statusBarAnimator = ObjectAnimator
                .ofInt(drawerLayout, "statusBarBackgroundColor", sc, ec);
        statusBarAnimator.setEvaluator(new ArgbEvaluator());

        tabLayout.clearAnimation();
        ObjectAnimator tabLayoutAnimator = ObjectAnimator.ofFloat(tabLayout, View.TRANSLATION_Y, et);

        AnimatorSet animations = new AnimatorSet();
        animations
                .play(toolBarAnimator)
                .with(statusBarAnimator)
                .with(tabLayoutAnimator);
        animations.addListener(new AnimatorFeedBack());
        animations.setDuration(300);
        animations.setInterpolator(new DecelerateInterpolator());
        animations.start();
    }

    class AnimatorFeedBack implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animator) {
            if (waitForState)
                animator.cancel();
            waitForState = true;
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            waitForState = false;
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            //waitForState = false;
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }
    }

}

