package com.myappcompany.steve.canvaspaint.Adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.myappcompany.steve.canvaspaint.fragments.HelpControlFragment;
import com.myappcompany.steve.canvaspaint.fragments.SettingsColorFragment;
import com.myappcompany.steve.canvaspaint.fragments.SettingsControlFragment;
import com.myappcompany.steve.canvaspaint.fragments.SettingsGridFragment;

public class HelpPagerAdapter extends FragmentStateAdapter {

    private static final int NUMBER_OF_TABS = 3;

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
                HelpControlFragment helpControlFragment2 = new HelpControlFragment();
                //Attach a bundle to the fragment object
                //controlFragment.setArguments(bundle);
                return helpControlFragment2;
            case 2:
                HelpControlFragment helpControlFragment3 = new HelpControlFragment();
                //Attach a bundle to the fragment object
                //controlFragment.setArguments(bundle);
                return helpControlFragment3;
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUMBER_OF_TABS;
    }
}
