package com.myappcompany.steve.canvaspaint;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.myappcompany.steve.canvaspaint.fragments.SettingsControlFragment;

public class SettingsPagerAdapter extends FragmentStateAdapter {

    private final int NUMBER_OF_TABS = 3;

    public SettingsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        //insert some data into the bundle
        //bundle.put_____();
        return new SettingsControlFragment();
    }

    @Override
    public int getItemCount() {
        return NUMBER_OF_TABS;
    }
}
