package m.nischal.melody.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import m.nischal.melody.Helper.BusEvents;
import m.nischal.melody.Helper.GeneralHelpers;
import m.nischal.melody.MediaPlayerPresenter;
import m.nischal.melody.ObjectModels.Song;
import m.nischal.melody.R;
import m.nischal.melody.RecyclerViewHelpers.RecyclerViewQuickRecall;
import m.nischal.melody.Util.ObservableContainer;
import m.nischal.melody.Util.RxBus;
import m.nischal.melody.ui.widgets.ScrimInsetsFrameLayout;
import rx.Observer;
import rx.subscriptions.CompositeSubscription;

import static m.nischal.melody.Helper.GeneralHelpers.DebugHelper.LumberJack;
import static m.nischal.melody.Helper.GeneralHelpers.PicassoHelper;
import static m.nischal.melody.MediaPlayerPresenter.Token;
import static m.nischal.melody.MediaPlayerPresenter.bindToService;
import static m.nischal.melody.MediaPlayerPresenter.unbindFromService;

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

    private final CompositeSubscription subscriptions = new CompositeSubscription();
    private final RxBus rxBus = RxBus.getBus();
    private Token token;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TabLayout tabLayout;

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

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                GeneralHelpers.DebugHelper.overdose(MainActivity.this, "all systems go!");
            }
        }, new IntentFilter("complete"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscriptions.hasSubscriptions())
            subscriptions.unsubscribe();
        unbindFromService(token);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
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
        rxBus.publish(new BusEvents.ViewPagerPageChanged());
    }

    private void showDetails() {
        Intent intent = new Intent(this, Details.class);
        startActivity(intent);
    }

    private void initLibraries() {
        subscriptions.add(rxBus.toObservable()
                .subscribe(busClass -> {
                    if (busClass instanceof BusEvents.RecyclerViewItemClick)
                        if (rxBus.getValue(RxBus.TAG_PAGER_POSITION, 0) != 0)
                            showDetails();
                        else playMusic();
                }));
        PicassoHelper.initPicasso(this);
        ObservableContainer.initAll(this);
        token = bindToService(this);
    }

    private void playMusic() {
        int position = rxBus.getValue(RxBus.TAG_RECYCLER_VIEW_ITEM_CLICK, 0);
        ObservableContainer.getSongArrayListObservable()
                .take(position + 1)
                .last()
                .subscribe(new Observer<Song>() {
                    @Override
                    public void onCompleted() {
                        LumberJack.v("onCompleted called/MainActivity#playMusic");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LumberJack.e("onError called/MainActivity#playMusic");
                        LumberJack.e(e);
                    }

                    @Override
                    public void onNext(Song song) {
                        LumberJack.v("onNext called/MainActivity#playMusic");
                        List<String> details = new ArrayList<String>();
                        details.add(song.getSong_path());
                        details.add(song.getSong_title());
                        details.add(song.getSong_album());
                        details.add(song.getSong_artist());
                        MediaPlayerPresenter.setup(details);
                        rxBus.publish(new BusEvents.NewSongAddedToQueue());
                    }
                });
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

    public RecyclerViewQuickRecall getRecyclerScrollListenerInstance() {
        return new RecyclerViewQuickRecall(getResources().getColor(R.color.primary),
                toolbar, tabLayout, drawerLayout);
    }

    public ViewPagerState getViewPagerStateListenerInstance() {
        return new ViewPagerState();
    }

    public class ViewPagerState extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            LumberJack.v("pager page changed!");
            rxBus.putValue(RxBus.TAG_PAGER_POSITION, position);
            rxBus.publish(new BusEvents.ViewPagerPageChanged());
        }
    }

}
