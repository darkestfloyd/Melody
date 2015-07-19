package m.nischal.melody.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
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
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Cyplops on 08-Jul-15.
 */
public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener, ScrimInsetsFrameLayout.OnInsetsCallback {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private RxBus rxBus;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (true) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        initBus();
        PicassoHelper.initPicasso(this);
        ObservableContainer.initAll(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primary_dark));

        ScrimInsetsFrameLayout scrimInsetsFrameLayout = (ScrimInsetsFrameLayout) findViewById(R.id.scrim_header);
        scrimInsetsFrameLayout.setOnInsetsCallback(this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new MainFragment());
        fragmentTransaction.commit();
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
            DebugHelper.LumberJack.i("click in main");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
    }

    protected void setUpTabLayout(TabLayout tab) {
        this.tabLayout = tab;
    }

    private void replaceFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new DetailsFragment()).addToBackStack(null);
        fragmentTransaction.commit();
        int x = rxBus.getValue(RxBus.TAG_RECYCLER_VIEW_ITEM_CLICK);
        DebugHelper.LumberJack.i("child view click position: ", x);
        ObservableContainer.getAlbumArrayListObservable().take(x + 1).last().subscribe(album -> DebugHelper.LumberJack.i(album.getAlbum_name()));
    }

    private void initBus() {
        rxBus = RxBus.getBus();
        DebugHelper.LumberJack.i("bus id in activity: ", rxBus.hashCode());
        Subscription sc = rxBus
                .toObserverable()
                .subscribe(busClass -> {
                            if (busClass instanceof RxBus.BusClass.RecyclerViewItemClick)
                                replaceFragment();
                            if (busClass instanceof RxBus.BusClass.TapEvent)
                                drawerLayout.openDrawer(Gravity.LEFT);
                        }
                );
        subscriptions.add(sc);
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

    private class RecyclerViewQuickRecall extends RecyclerView.OnScrollListener {

        private int scrollValue;
        private boolean visible, waitForState;
        private int primary, black;
        private PropertyValuesHolder holder1, holder2;
        private ObjectAnimator toolBarAnimator, statusBarAnimator, tabLayoutAnimator;

        public RecyclerViewQuickRecall() {
            scrollValue = 0;
            visible = true;
            waitForState = false;
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

            if (dy > 0 && scrollValue > 300 && visible) {
                animate(-toolbar.getBottom(), 0, primary, black, -tabLayout.getTop(), false); //hide
            } else if (dy < -10 && !visible) {
                animate(0, 1, black, primary, 0, true); //show
            }
        }

        private void animate(int endy, int endalpha, int sc, int ec, int et, boolean v) {

            visible = v;

            toolbar.clearAnimation();
            holder1 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, endy);
            holder2 = PropertyValuesHolder.ofFloat(View.ALPHA, endalpha);
            toolBarAnimator = ObjectAnimator.ofPropertyValuesHolder(toolbar, holder1, holder2);

            drawerLayout.clearAnimation();
            statusBarAnimator = ObjectAnimator
                    .ofInt(drawerLayout, "statusBarBackgroundColor", sc, ec);
            statusBarAnimator.setEvaluator(new ArgbEvaluator());

            tabLayout.clearAnimation();
            tabLayoutAnimator = ObjectAnimator.ofFloat(tabLayout, View.TRANSLATION_Y, et);

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


}
