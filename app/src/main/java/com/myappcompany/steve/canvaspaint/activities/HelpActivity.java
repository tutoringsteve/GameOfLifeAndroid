package com.myappcompany.steve.canvaspaint.activities;

import android.os.Bundle;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.myappcompany.steve.canvaspaint.Adapters.HelpPagerAdapter;
import com.myappcompany.steve.canvaspaint.Adapters.SettingsPagerAdapter;
import com.myappcompany.steve.canvaspaint.R;

public class HelpActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager2 mViewPager2;
    private HelpPagerAdapter mAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        mViewPager2 = findViewById(R.id.pager);
        mAdapter = new HelpPagerAdapter(this);
        mViewPager2.setAdapter(mAdapter);
        mTabLayout = findViewById(R.id.tabs);
        new TabLayoutMediator(mTabLayout, mViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(getResources().getStringArray(R.array.tab_names_array)[position]);
            }
        }).attach();
    }
}