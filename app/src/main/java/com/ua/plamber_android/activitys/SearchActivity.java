package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.ua.plamber_android.R;
import com.ua.plamber_android.fragments.SearchFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseDrawerActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.searchViewBook)
    SearchView mSearch;

    public static final String SEARCH_KEY = "SEARCH_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setElevation(10);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int menuItem = getIntent().getIntExtra(START_WITH_MENU, 0);
        getNavigationView().getMenu().getItem(menuItem).setChecked(true);
        backToLibrary();

        mSearch.setVisibility(View.VISIBLE);
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearch.clearFocus();
                Bundle args = new Bundle();
                args.putString(SEARCH_KEY, query);
                SearchFragment fragment = new SearchFragment();
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.search_container, fragment).commit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return true;
    }

    public static Intent startSearchActivity(Context context) {
        return new Intent(context, SearchActivity.class);
    }
}
