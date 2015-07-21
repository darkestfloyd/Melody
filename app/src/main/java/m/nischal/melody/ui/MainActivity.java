package m.nischal.melody.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import m.nischal.melody.Helper.DebugHelper;
import m.nischal.melody.Helper.ObservableContainer;
import m.nischal.melody.Helper.PicassoHelper;
import m.nischal.melody.Helper.RxBus;
import m.nischal.melody.Helper.ScrimInsetsFrameLayout;
import m.nischal.melody.R;
import rx.subscriptions.CompositeSubscription;

/*The MIT License (MIT)
 *
 *    Copyright (c) 2015 Nischal M
 *
 *    Permission is hereby granted, free of charge, to any person obtaining a copy
 *    of this software and associated documentation files (the "Software"), to deal
 *    in the Software without restriction, including without limitation the rights
 *    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *    copies of the Software, and to permit persons to whom the Software is
 *    furnished to do so, subject to the following conditions:
 *
 *    The above copyright notice and this permission notice shall be included in
 *    all copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *    THE SOFTWARE.
 */

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener, ScrimInsetsFrameLayout.OnInsetsCallback {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private RxBus rxBus;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    private boolean toolbarVisible;

    private CompositeSubscription subscriptions = new CompositeSubscription();
    private boolean waitForState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyFlashScreen()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeath()
                .build());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        toolbarVisible = true;
        waitForState = false;

        initLibraries();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark));

        ((ScrimInsetsFrameLayout) findViewById(R.id.scrim_header))
                .setOnInsetsCallback(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new MainFragment())
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscriptions.hasSubscriptions())
            subscriptions.unsubscribe();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
        toolbar.setTitle("Melody.");
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar);
    }

    protected void setUpTabLayout(TabLayout tab) {
        this.tabLayout = tab;
        rxBus.putValue(RxBus.TAG_PAGER_POSITION, 0);
        rxBus.publish(new RxBus.BusClass.ViewPagerPageChanged());
    }

    private void replaceFragment() {

        Intent intent = new Intent(this, Details.class);
        startActivity(intent);

        /*getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new demo())
                .addToBackStack(null)
                .commit();*/

        //int x = rxBus.getValue(RxBus.TAG_RECYCLER_VIEW_ITEM_CLICK);
        //ObservableContainer.getAlbumArrayListObservable().take(x + 1).last().subscribe(album -> DebugHelper.LumberJack.i(album.getAlbum_name()));
    }

    private void initLibraries() {
        rxBus = RxBus.getBus();
        subscriptions.add(rxBus.toObservable()
                .subscribe(busClass -> {
                    if (busClass instanceof RxBus.BusClass.RecyclerViewItemClick)
                        if (rxBus.getValue(RxBus.TAG_PAGER_POSITION) != 0)
                            replaceFragment();
                    else DebugHelper.overdose(this, "song click");
                }));
        PicassoHelper.initPicasso(this);
        ObservableContainer.initAll(this);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        actionBarDrawerToggle.onDrawerSlide(drawerView, slideOffset);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        actionBarDrawerToggle.onDrawerClosed(drawerView);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        actionBarDrawerToggle.onDrawerClosed(drawerView);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        actionBarDrawerToggle.onDrawerStateChanged(newState);
    }

    @Override
    public void onInsetsChanged(Rect insets) {
        Toolbar toolbar = this.toolbar;
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)
                toolbar.getLayoutParams();
        lp.topMargin = insets.top;
        int top = insets.top;
        insets.top += toolbar.getHeight();
        toolbar.setLayoutParams(lp);
        insets.top = top;
    }

    public RecyclerViewQuickRecall getScrollListenerInstnce() {
        return new RecyclerViewQuickRecall();
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

    public ViewPagerState getViewPagerStateListenerInstance() {
        return new ViewPagerState();
    }

    public class ViewPagerState extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            if (!toolbarVisible)
                animate(0, 1f, Color.BLACK, getResources().getColor(R.color.primary_dark), 0, true);
            rxBus.putValue(RxBus.TAG_PAGER_POSITION, position);
            rxBus.publish(new RxBus.BusClass.ViewPagerPageChanged());
        }
    }

    private class RecyclerViewQuickRecall extends RecyclerView.OnScrollListener {

        private int scrollValue;
        private int primary, black;

        public RecyclerViewQuickRecall() {
            scrollValue = 0;
            primary = getResources().getColor(R.color.primary_dark);
            black = Color.BLACK;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            //0 - idle
            //1 - dragging
            //3 - setting
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            scrollValue += dy;

            if (dy > 10 && scrollValue > 500 && toolbarVisible) {
                animate(-toolbar.getBottom(), 1f, primary, black, -tabLayout.getTop(), false); //hide
            } else if (dy < -10 && !toolbarVisible) {
                animate(0, 1f, black, primary, 0, true); //show
            }
        }
    }

    private class AnimatorFeedBack implements Animator.AnimatorListener {

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
