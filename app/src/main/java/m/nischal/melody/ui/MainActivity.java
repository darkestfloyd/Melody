package m.nischal.melody.ui;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import m.nischal.melody.Helper.RxBus;
import m.nischal.melody.R;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Cyplops on 08-Jul-15.
 */
public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private RxBus rxBus;
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
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.drawer_open, R.string.drawer_close);

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

    private void initBus() {
        rxBus = RxBus.getBus();
        Subscription sc = rxBus
                .toObserverable()
                .subscribe(busClass -> {
                    if (busClass instanceof RxBus.BusClass.TapEvent)
                        drawerLayout.openDrawer(Gravity.LEFT);
                });
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

}
