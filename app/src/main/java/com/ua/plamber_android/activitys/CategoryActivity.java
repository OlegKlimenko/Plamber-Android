package com.ua.plamber_android.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ua.plamber_android.R;
import com.ua.plamber_android.fragments.CategoryFragment;
import com.ua.plamber_android.fragments.LibraryFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends BaseDrawerActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

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

        mNavigationView.getMenu().getItem(1).setChecked(true);
        backToLibrary();
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


}
