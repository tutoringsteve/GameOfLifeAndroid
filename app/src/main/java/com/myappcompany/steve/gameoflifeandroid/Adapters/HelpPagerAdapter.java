package com.myappcompany.steve.gameoflifeandroid.Adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.myappcompany.steve.gameoflifeandroid.fragments.HelpControlFragment;
import com.myappcompany.steve.gameoflifeandroid.fragments.HelpFAQFragment;

public class HelpPagerAdapter extends FragmentStateAdapter {

    private static final int NUMBER_OF_TABS = 2;

    public HelpPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        //insert some data into the bundle
        //bundle.put_____();

        switch (position) {
            case 0:
                HelpControlFragment helpControlFragment = new HelpControlFragment();
                //Attach a bundle to the fragment object
                //controlFragment.setArguments(bundle);
                return helpControlFragment;
            case 1:
                HelpFAQFragment helpFAQFragment = new HelpFAQFragment();
                //Attach a bundle to the fragment object
                //controlFragment.setArguments(bundle);
                return helpFAQFragment;
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUMBER_OF_TABS;
    }
}
