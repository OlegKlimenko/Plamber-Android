package com.ua.plamber_android.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.ua.plamber_android.R;
import com.ua.plamber_android.fragments.CategoryFragment;
import com.ua.plamber_android.fragments.LibraryFragment;
import com.ua.plamber_android.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends BaseDrawerActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    PreferenceUtils preferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        preferenceUtils = new PreferenceUtils(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getIntent().getStringExtra(LibraryFragment.NAMECATEGORI));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(10);
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container_fragment_category);
        if (fragment == null) {
            fragment = new CategoryFragment();
            fm.beginTransaction().add(R.id.container_fragment_category, fragment).commit();
        }

        Bundle args = new Bundle();
        args.putLong(LibraryFragment.IDCATEGORI, getIntent().getLongExtra(LibraryFragment.IDCATEGORI, 1));
        fragment.setArguments(args);

        getNavigationView().getMenu().getItem(1).setChecked(true);
        backToLibrary();
        iniOfflineSwitch();
    }

    @Override
    public void onBackPressed() {
        if (!getDrawerLayout().isDrawerOpen(GravityCompat.START))
            super.onBackPressed();
         else
            getDrawerLayout().closeDrawers();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void iniOfflineSwitch() {
        getOfflineSwitcher().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferenceUtils.writeLogic(PreferenceUtils.OFFLINE_MODE, b);
                finish();
            }
        });
    }

}
