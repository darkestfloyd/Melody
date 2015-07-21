package m.nischal.melody.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import m.nischal.melody.Helper.GeneralHelpers;
import m.nischal.melody.ObjectModels._BaseModel;
import m.nischal.melody.R;

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

public class MainFragment extends Fragment {

    private ArrayList<String> titles = new ArrayList<String>();
    private ViewPager viewPager;
    private MainActivity.ViewPagerState stateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titles = GeneralHelpers.TitleHelper.getTitles();

        MainActivity parent = (MainActivity) getActivity();
        parent.setToolbar((Toolbar) view.findViewById(R.id.toolbar));

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        stateListener = parent.getViewPagerStateListenerInstance();
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));
        viewPager.addOnPageChangeListener(stateListener);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        parent.setUpTabLayout(tabLayout);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewPager.removeOnPageChangeListener(stateListener);
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
