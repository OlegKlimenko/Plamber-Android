package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.ua.plamber_android.R;
import com.ua.plamber_android.fragments.SupportFragment;
import com.ua.plamber_android.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupportActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.support_parent_layout)
    LinearLayout mParentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        ButterKnife.bind(this);
        initToolbar();
        setSupportFragment();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setElevation(10);
            getSupportActionBar().setTitle(R.string.support_message);
        }
    }

    private void setSupportFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container_support_fragment);
        if (fragment == null) {
            fragment = new SupportFragment();
            fm.beginTransaction().replace(R.id.container_support_fragment, fragment).commit();
        }
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

    public void viewMessage(String message) {
        Utils.messageSnack(mParentLayout, message);
    }

    public static Intent startSupportActivity(Context context) {
        return new Intent(context, SupportActivity.class);
    }
}
