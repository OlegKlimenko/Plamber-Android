package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.ua.plamber_android.R;
import com.ua.plamber_android.adapters.ViewPagerAdapter;
import com.ua.plamber_android.fragments.LibraryFragment;
import com.ua.plamber_android.fragments.RecommendedFragmnet;
import com.ua.plamber_android.fragments.UploadFragment;
import com.ua.plamber_android.fragments.UserBookFragment;
import com.ua.plamber_android.utils.TokenUtils;
import com.ua.plamber_android.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LibraryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    public static final String TAG = "LibraryActivity";
    private Utils utils;
    private static long timeExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ButterKnife.bind(this);
        utils = new Utils(this);
        setupPager();
        setSupportActionBar(mToolbar);
        setupNavigationDrawer();
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        setPage(0);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (timeExit + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            String mess = getString(R.string.press_once_to_exit);
            Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
        }
        timeExit = System.currentTimeMillis();
    }

    public void setupPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UserBookFragment(), "My books");
        adapter.addFragment(new LibraryFragment(), "Library");
        adapter.addFragment(new RecommendedFragmnet(), "Recommended");
        adapter.addFragment(new UploadFragment(), "Upload");
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        if (utils.getWidthDeviceDP() > 360){
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    public void setupNavigationDrawer() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_user_books:
                        setPage(0);
                        break;

                    case R.id.nav_library:
                        setPage(1);
                        break;

                    case R.id.nav_recommended:
                        setPage(2);
                        break;
                    case R.id.nav_upload_book:
                        setPage(3);
                        break;

                    case R.id.nav_setting:
                        Intent intent = SettingActivity.startSettingActivity(getApplicationContext());
                        startActivity(intent);
                        break;

                    case R.id.nav_logout:
                        logoutApplication();
                        break;
                }
                return true;
            }
        });
    }

    private void setPage(int postion) {
        mViewPager.setCurrentItem(postion);
        getSupportActionBar().setTitle(mViewPager.getAdapter().getPageTitle(postion));
        mDrawerLayout.closeDrawers();
        mNavigationView.getMenu().getItem(postion).setChecked(true);
    }

    private void logoutApplication() {
        TokenUtils tokenUtils = new TokenUtils(getApplicationContext());
        tokenUtils.removeToken();
        finish();
        Intent intent = LoginActivity.startLoginActivity(getApplicationContext());
        startActivity(intent);
    }

    public static Intent startLibraryActivity(Context context) {
        Intent intent = new Intent(context, LibraryActivity.class);
        return intent;
    }
}
