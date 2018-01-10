package com.ua.plamber_android.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ua.plamber_android.R;
import com.ua.plamber_android.adapters.ViewPagerAdapter;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.fragments.ConnectionErrorDialog;
import com.ua.plamber_android.fragments.LibraryFragment;
import com.ua.plamber_android.fragments.RecommendedFragmnet;
import com.ua.plamber_android.fragments.UploadFragment;
import com.ua.plamber_android.fragments.UserBookFragment;
import com.ua.plamber_android.utils.PreferenceUtils;
import com.ua.plamber_android.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class LibraryActivity extends BaseDrawerActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.fab_upload)
    FloatingActionButton mFabUpload;
    @BindView(R.id.parentLibraryLayout)
    CoordinatorLayout mParentLayout;

    public static final String TAG = "LibraryActivity";
    public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
    private static final int REQUEST_WRITE_STORAGE = 101;
    private Utils utils;
    private PreferenceUtils preferenceUtils;
    private static long timeExit;
    public static int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ButterKnife.bind(this);
        utils = new Utils(this);
        preferenceUtils = new PreferenceUtils(this);
        setSupportActionBar(mToolbar);
        setupPager();
        setPagerSwipe();
        setToggle(mToolbar);
        setPage(0);
        setupNavigationDrawer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFabUpload.setElevation(5);
        }
        //view fab on start in offline mode
        initFabButton(0);

        if (!checkPermission())
            runQuestionPermissions();
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
    protected void onResume() {
        super.onResume();
        initOfflineModeSwitch();
    }

    @OnClick(R.id.fab_upload)
    public void openUploadActivity() {
        if (!preferenceUtils.readStatusOffline())
            startActivity(UploadActivity.startUploadActivity(getApplicationContext()));
        else
            Utils.messageSnack(mParentLayout, getString(R.string.upload_not_available_in_offline_mode));
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
                if (preferenceUtils.readStatusOffline()) {
                    Utils.messageSnack(mParentLayout, getString(R.string.search_not_access_in_offline));
                } else {
                    Intent intent = SearchActivity.startSearchActivity(this);
                    intent.putExtra(START_WITH_MENU, mViewPager.getCurrentItem());
                    startActivityForResult(intent, LibraryFragment.MENU_REQUEST);
                }
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
        adapter.addFragment(new UserBookFragment(), getString(R.string.my_books));
        adapter.addFragment(new LibraryFragment(), getString(R.string.library));
        adapter.addFragment(new RecommendedFragmnet(), getString(R.string.recommended));
        adapter.addFragment(new UploadFragment(), getString(R.string.upload));
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        if (utils.getWidthDeviceDP() > 400) {
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
        currentPosition = position;
        if (position == 3 || position == 0) {
            mFabUpload.show();
        } else {
            mFabUpload.hide();
        }
    }

    public static Intent startLibraryActivity(Context context) {
        Intent intent = new Intent(context, LibraryActivity.class);
        return intent;
    }

    private void initOfflineModeSwitch() {
        getOfflineSwitcher().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferenceUtils.writeOfflineMode(b);
                updateView();
            }
        });
    }

    public void updateView() {
        setupPager();
        setPage(currentPosition);
    }

    public void switchToOffline() {
        getOfflineSwitcher().setChecked(true);
    }

    public void runErrorDialog(String message) {
        if (!preferenceUtils.readStatusOffline()) {
            ConnectionErrorDialog dialog = new ConnectionErrorDialog();
            dialog.setCancelable(false);
            Bundle args = new Bundle();
            args.putString(ERROR_MESSAGE, message);
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), ConnectionErrorDialog.TAG);
        }
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


    private void runQuestionPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.not_have_write_permission, Toast.LENGTH_SHORT).show();
                }
        }
    }

}
