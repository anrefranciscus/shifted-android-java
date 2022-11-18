package com.example.projectbootcamp.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.projectbootcamp.activities.ui.menu_1.HomeFragment;
import com.example.projectbootcamp.activities.ui.menu_2.GalleryFragment;
import com.example.projectbootcamp.activities.ui.menu_3.Menu3Fragment;

public class TabLayoutAdapter extends FragmentStateAdapter {

    String[] fragment_names = new String[]{"Kuliner", "Gallery", "Review"};


    public TabLayoutAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new GalleryFragment();
            case 2:
                return new Menu3Fragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return fragment_names.length;
    }
}
