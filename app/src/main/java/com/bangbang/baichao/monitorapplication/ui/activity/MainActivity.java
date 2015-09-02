/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bangbang.baichao.monitorapplication.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.bangbang.baichao.monitorapplication.R;
import com.bangbang.baichao.monitorapplication.entity.User;
import com.bangbang.baichao.monitorapplication.ui.fragment.ManagerControlFragment;
import com.bangbang.baichao.monitorapplication.ui.pulltorefreshlistfragment.TrackOfMonitorFragment;
import com.bangbang.baichao.monitorapplication.ui.pulltorefreshlistfragment.ManagerList2Fragment;
import com.bangbang.baichao.monitorapplication.ui.fragment.MonitorControlFragment;
import com.bangbang.baichao.monitorapplication.ui.pulltorefreshlistfragment.MonitorPulltorefreshListFragment;
import com.bangbang.baichao.monitorapplication.utils.HttpUtils;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {


    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the four primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;
    protected final User user = User.getInstance();

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        actionBar.addTab(
                actionBar.newTab()
                        .setText("监控列表")
                        .setTabListener(this));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("添加监控")
                        .setTabListener(this));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("接口人列表")
                        .setTabListener(this));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("添加接口人")
                        .setTabListener(this));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("监控记录")
                        .setTabListener(this));
    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new MonitorPulltorefreshListFragment();
                case 1:
                    return new MonitorControlFragment();
                case 2:
                    return new ManagerList2Fragment();
                case 3:
                    return  new ManagerControlFragment();
                case 4:
                    return new TrackOfMonitorFragment();
                default:
                    return new MonitorPulltorefreshListFragment();
            }
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Section " + (position + 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            try {
                HttpUtils.UserExit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            user.init();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
