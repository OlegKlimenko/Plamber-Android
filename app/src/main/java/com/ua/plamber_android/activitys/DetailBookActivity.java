package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ua.plamber_android.R;
import com.ua.plamber_android.fragments.DetailBookFragment;
import com.ua.plamber_android.fragments.DetailBookFragmentOffline;
import com.ua.plamber_android.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailBookActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static final String BOOK_ID = "BOOK_ID";
    public static final String BOOK_NAME = "BOOK_NAME";
    public static final String PDF_PATH = "PDF_PATH";
    PreferenceUtils preferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        ButterKnife.bind(this);
        preferenceUtils = new PreferenceUtils(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            String title = getIntent().getStringExtra(BOOK_NAME);
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setElevation(10);
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.detail_fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fragment.setArguments(setBundleId());
            fm.beginTransaction().replace(R.id.detail_fragment_container, fragment).commit();
        }
    }

    private Fragment createFragment() {
        if (preferenceUtils.readStatusOffline()) {
            return new DetailBookFragmentOffline();
        } else {
            return new DetailBookFragment();
        }
    }

    private Bundle setBundleId() {
        Bundle args = new Bundle();
        long id = getIntent().getLongExtra(BOOK_ID, 0);
        args.putLong(BOOK_ID, id);
        return args;
    }

    public static Intent startDetailActivity(Context context) {
        Intent intent = new Intent(context, DetailBookActivity.class);
        return intent;
    }
}
