package com.myappcompany.steve.canvaspaint.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.myappcompany.steve.canvaspaint.R;
import com.myappcompany.steve.canvaspaint.Adapters.SettingsPagerAdapter;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager2 mViewPager2;
    private SettingsPagerAdapter mAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mViewPager2 = findViewById(R.id.pager);
        mAdapter = new SettingsPagerAdapter(this);
        mViewPager2.setAdapter(mAdapter);
        mTabLayout = findViewById(R.id.tabs);
        new TabLayoutMediator(mTabLayout, mViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(getResources().getStringArray(R.array.settings_tab_names_array)[position]);
            }
        }).attach();
    }
}