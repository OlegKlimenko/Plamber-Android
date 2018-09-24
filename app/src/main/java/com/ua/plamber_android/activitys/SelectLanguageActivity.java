package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.ua.plamber_android.R;
import com.ua.plamber_android.fragments.CategoryUploadFragment;
import com.ua.plamber_android.fragments.SelectLanguageFragamnt;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectLanguageActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.searchViewBook)
    SearchView mSearchView;

    private static final String TAG = "SelectLanguageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
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
        SelectLanguageFragamnt fragment = new SelectLanguageFragamnt();
        Bundle args = new Bundle();
        args.putString(SelectLanguageFragamnt.SEARCH_KEY, searchKey);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.select_language_container, fragment).commit();
    }

    public static Intent startSelectLanguageActivity(Context context) {
        return new Intent(context, SelectLanguageActivity.class);
    }
}
