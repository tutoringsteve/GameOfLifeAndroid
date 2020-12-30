package com.myappcompany.steve.gameoflifeandroid.Adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.myappcompany.steve.gameoflifeandroid.fragments.SettingsColorFragment;
import com.myappcompany.steve.gameoflifeandroid.fragments.SettingsControlFragment;
import com.myappcompany.steve.gameoflifeandroid.fragments.SettingsGridFragment;

public class SettingsPagerAdapter extends FragmentStateAdapter {

    private static final int NUMBER_OF_TABS = 3;

    public SettingsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
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
                SettingsControlFragment controlFragment = new SettingsControlFragment();
                //Attach a bundle to the fragment object
                //controlFragment.setArguments(bundle);
                return controlFragment;
            case 1:
                SettingsGridFragment gridFragment = new SettingsGridFragment();
                //Attach a bundle to the fragment object
                //controlFragment.setArguments(bundle);
                return gridFragment;
            case 2:
                SettingsColorFragment colorFragment = new SettingsColorFragment();
                //Attach a bundle to the fragment object
                //controlFragment.setArguments(bundle);
                return colorFragment;
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUMBER_OF_TABS;
    }
}
