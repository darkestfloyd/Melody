package m.nischal.melody.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;

import m.nischal.melody.Helper.DebugHelper;
import m.nischal.melody.Helper.GeneralHelpers;
import m.nischal.melody.Helper.RxBus;
import m.nischal.melody.ObjectModels._BaseModel;
import m.nischal.melody.R;

/**
 * Created by Cyplops on 08-Jul-15.
 */
public class MainFragment extends Fragment {

    private ArrayList<String> titles = new ArrayList<String>();
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_new, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titles = GeneralHelpers.TitleHelper.getTitles();

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        toolbar.setTitle("M3l0dY.");
        toolbar.setNavigationOnClickListener(view1 -> DebugHelper.LumberJack.i("clicked!"));
        ((MainActivity) getActivity()).setToolbar(toolbar);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        ((MainActivity) getActivity()).setUpTabLayout(tabLayout);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RxBus bus = RxBus.getBus();
        DebugHelper.LumberJack.d("click at menu");
        int id = item.getItemId();
        if (id == android.R.id.home) {
            DebugHelper.LumberJack.d("click at icon");
            bus.publish(new RxBus.BusClass.TapEvent());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment base = new BaseFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(_BaseModel.VIEW_PAGER_POSITION_STRING, position);
            base.setArguments(bundle);
            return base;
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //Not deleting items for now!
            //TODO Get some way to store the fragments.
            //super.destroyItem(container, position, object);
        }
    }

}
