package com.ua.plamber_android.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ua.plamber_android.R;
import com.ua.plamber_android.adapters.ViewPagerAdapter;
import com.ua.plamber_android.fragments.BaseViewBookFragment;
import com.ua.plamber_android.fragments.LibraryFragment;
import com.ua.plamber_android.fragments.RecommendedFragmnet;
import com.ua.plamber_android.fragments.UploadFragment;
import com.ua.plamber_android.fragments.UserBookFragment;
import com.ua.plamber_android.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LibraryActivity extends BaseDrawerActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.fab_upload)
    FloatingActionButton mFabUpload;

    public static final String TAG = "LibraryActivity";
    private Utils utils;
    private static long timeExit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ButterKnife.bind(this);
        utils = new Utils(this);
        setSupportActionBar(mToolbar);
        setupPager();
        setPagerSwipe();
        setToggle(mToolbar);
        setPage(0);
        setupNavigationDrawer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFabUpload.setElevation(5);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LibraryFragment.MENU_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                setPage(data.getIntExtra(BACK_WITH_MENU, 0));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_library, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_search_library:
                Intent intent = SearchActivity.startSearchActivity(this);
                intent.putExtra(START_WITH_MENU, mViewPager.getCurrentItem());
                startActivityForResult(intent, LibraryFragment.MENU_REQUEST);
                break;
        }
        return true;
    }

    public void setupNavigationDrawer() {
        getNavigationView().setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                        startSetting();
                        break;

                    case R.id.nav_logout:
                        logoutApplication();
                        break;
                }
                return true;
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
        adapter.addFragment(new UploadFragment(), "Uploaded");
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        if (utils.getWidthDeviceDP() > 360) {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    public void setPage(int postion) {
        mViewPager.setCurrentItem(postion);
        getSupportActionBar().setTitle(mViewPager.getAdapter().getPageTitle(postion));
        getDrawerLayout().closeDrawers();
        getNavigationView().getMenu().getItem(postion).setChecked(true);
    }

    private void setPagerSwipe() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPage(position);
                initFabButton(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initFabButton(int position) {
        UploadFragment.currentPosition = position;
        if (position == 3) {
            mFabUpload.show();
        } else {
            mFabUpload.hide();
        }

        mFabUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(UploadActivity.startUploadActivity(getApplicationContext()));
            }
        });
    }


    public static Intent startLibraryActivity(Context context) {
        Intent intent = new Intent(context, LibraryActivity.class);
        return intent;
    }
}
