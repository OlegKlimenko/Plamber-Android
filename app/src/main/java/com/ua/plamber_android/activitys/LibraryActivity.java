package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.ua.plamber_android.R;
import com.ua.plamber_android.adapters.ViewPagerAdapter;
import com.ua.plamber_android.fragments.LibraryFragment;
import com.ua.plamber_android.fragments.RecommendedFragmnet;
import com.ua.plamber_android.fragments.UserBookFragment;
import com.ua.plamber_android.utils.TokenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LibraryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.main_view_pager) ViewPager mViewPager;
    @BindView(R.id.tabLayout) TabLayout mTabLayout;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;
    @BindView(R.id.drawerLayout) DrawerLayout mDrawerLayout;

    public static final String TAG = "LibraryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        setupPager();
        setupNavigationDrawer();

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    public void setupPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LibraryFragment(), "Library");
        adapter.addFragment(new UserBookFragment(), "My books");
        adapter.addFragment(new RecommendedFragmnet(), "Recommended");

        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(1); //set mybooks satrt page
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void setupNavigationDrawer() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_logout:
                        TokenUtils tokenUtils = new TokenUtils(getApplicationContext());
                        tokenUtils.removeToken();
                        finish();
                        Intent intent = LoginActivity.startLoginActivity(getApplicationContext());
                        startActivity(intent);
                }
                return true;
            }
        });
    }

    public static Intent startLibraryActivity(Context context) {
        Intent intent = new Intent(context, LibraryActivity.class);
        return intent;
    }
}
