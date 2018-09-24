package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.ua.plamber_android.R;
import com.ua.plamber_android.fragments.CategoryUploadFragment;
import com.ua.plamber_android.fragments.SelectLanguageFragamnt;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectCategoryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.searchViewBook)
    SearchView mSearchView;

    public static final String SEARCH_KEY = "SEARCH_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_upload);
        ButterKnife.bind(this);

        mSearchView.setVisibility(View.VISIBLE);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setFragment(newText);
                return true;
            }
        });
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.select_category_upload);
            getSupportActionBar().setElevation(10);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setFragment(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void setFragment(String searchKey) {
        CategoryUploadFragment fragment = new CategoryUploadFragment();
        Bundle args = new Bundle();
        args.putString(SelectLanguageFragamnt.SEARCH_KEY, searchKey);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.category_upload_container, fragment).commit();
    }

    public static Intent startCategoryUploadActivity(Context context) {
        return new Intent(context, SelectCategoryActivity.class);
    }
}
