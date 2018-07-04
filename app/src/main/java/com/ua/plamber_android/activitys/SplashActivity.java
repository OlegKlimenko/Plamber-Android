package com.ua.plamber_android.activitys;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.ua.plamber_android.R;
import com.ua.plamber_android.fragments.SplashFragment;

import butterknife.BindView;

public class SplashActivity extends SingleFragmentActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onStart() {
        super.onStart();
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
    }

    @Override
    protected Fragment createFragment() {
        return new SplashFragment();
    }

    @Override
    protected int setToolbarTitle() {
        return 0;
    }
}
