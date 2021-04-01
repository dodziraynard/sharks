package com.innova.sharks.ui.start;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.innova.sharks.adapter.TabAdapter;
import com.innova.sharks.databinding.ActivityStartBinding;
import com.innova.sharks.utils.Constants;

import static com.innova.sharks.utils.Constants.FRAGMENT_POSITION;
import static com.innova.sharks.utils.Constants.SHARED_PREFS_FILE;

public class StartActivity extends AppCompatActivity {
    private static final String TAG = "StartActivity";
    int mCurrentViewPagerItem = 0;
    private ActivityStartBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(Constants.IS_NEW_USER, false).apply();

        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new IntroFragment(), "Start");
        adapter.addFragment(new FeatureFragment(), "Feature");
        adapter.addFragment(new LoginFragment(), "Login");
        adapter.addFragment(new RegisterFragment(), "Register");
        mBinding.viewPager.setAdapter(adapter);
        mBinding.previous.setVisibility(View.GONE);

        mBinding.next.setOnClickListener(view -> {
            mCurrentViewPagerItem = mBinding.viewPager.getCurrentItem() + 1;
            mBinding.viewPager.setCurrentItem(mCurrentViewPagerItem, true);
        });

        mBinding.previous.setOnClickListener(view -> {
            mCurrentViewPagerItem = mBinding.viewPager.getCurrentItem() - 1;
            mBinding.viewPager.setCurrentItem(mCurrentViewPagerItem, true);
        });

        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position <= 0) {
                    mBinding.previous.setVisibility(View.GONE);
                } else {
                    mBinding.previous.setVisibility(View.VISIBLE);
                }
                if (position >= 2) {
                    mBinding.next.setVisibility(View.GONE);
                } else {
                    mBinding.next.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        int position = getIntent().getIntExtra(FRAGMENT_POSITION, -1);
        if (position > -1) {
            mBinding.viewPager.setCurrentItem(position, true);
        }
    }
}