package com.example.testquest;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.sergivonavi.materialbanner.Banner;
import com.sergivonavi.materialbanner.BannerInterface;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private Banner mBanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SystemUtils.setStatusBarColor(this, R.color.colorBgLight);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mBanner = findViewById(R.id.banner);

        Activity mActivity = this;

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SystemUtils.closeKeyboard(mActivity);
                View focusView = getCurrentFocus();
                if (focusView != null) focusView.clearFocus();

                if (position == 2 && !SystemUtils.isOnline(mActivity)) {
                    SystemUtils.showNoInternetAlert(mActivity, getSupportFragmentManager());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBanner.setRightButtonListener(new BannerInterface.OnClickListener() {
            @Override
            public void onClick(BannerInterface banner) {
                mBanner.dismiss();
            }
        });
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SignUpFragment();
                case 1:
                    return new SignInFragment();
                case 2:
                    return new InfoFragment();
                default:
                    return new InfoFragment();
            }

        }



        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return getString(R.string.sign_up);
                case 1: return getString(R.string.sign_in);
                case 2: return getString(R.string.info);
                default: return getString(R.string.info);
            }
        }
    }
}
