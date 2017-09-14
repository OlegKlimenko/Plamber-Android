package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ua.plamber_android.R;
import com.ua.plamber_android.adapters.ViewPagerAdapter;
import com.ua.plamber_android.fragments.LibraryFragment;
import com.ua.plamber_android.fragments.RecommendedFragmnet;
import com.ua.plamber_android.fragments.UserBookFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LibraryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.main_view_pager) ViewPager mViewPager;
    @BindView(R.id.tabLayout) TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        setupPager();
    }

    public void setupPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LibraryFragment(), "Library");
        adapter.addFragment(new UserBookFragment(), "My books");
        adapter.addFragment(new RecommendedFragmnet(), "Recommended");
        
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public static Intent startLibraryActivity(Context context) {
        Intent intent = new Intent(context, LibraryActivity.class);
        return intent;
    }
}
