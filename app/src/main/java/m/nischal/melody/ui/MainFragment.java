package m.nischal.melody.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import m.nischal.melody.Helper.LoaderHelper;
import m.nischal.melody.ObjectModels.BaseModel;
import m.nischal.melody.R;
import m.nischal.melody.demo;

/**
 * Created by Cyplops on 08-Jul-15.
 */
public class MainFragment extends Fragment {

    private ArrayList<String> titles = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titles = TitleHelper.getTitles();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getActivity().getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter(getFragmentManager()));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private static class TitleHelper {

        public static ArrayList<String> getTitles() {
            ArrayList<String> titles = new ArrayList<String>();
            titles.add(" SONGS ");
            titles.add(" ALBUMS ");
            titles.add(" ARTISTS ");
            titles.add(" PLAYLISTS ");
            titles.add(" GENRES ");
            return titles;
        }

    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            //TODO implement for different types
            if (position == 0) {
                BaseFragment base = new BaseFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(BaseModel.VIEW_PAGER_POSITION_STRING, position);
                base.setArguments(bundle);
                return base;
            }
            return new demo();
        }

        @Override
        public int getCount() {
            return titles.size();
        }
    }

}
