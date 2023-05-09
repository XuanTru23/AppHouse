package com.example.apphouse.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.apphouse.fragment.FragmentHome;
import com.example.apphouse.fragment.FragmentLike;
import com.example.apphouse.fragment.FragmentSetting;
import com.example.apphouse.fragment.FragmentUser;

public class ViewPager2HomeAdapter extends FragmentStateAdapter {

    public ViewPager2HomeAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FragmentHome();
            case 1:
                return new FragmentUser();
            case 2:
                return new FragmentLike();
            case 3:
                return new FragmentSetting();
            default:
                return new FragmentHome();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
