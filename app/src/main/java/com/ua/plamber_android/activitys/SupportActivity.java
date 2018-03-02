package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.ua.plamber_android.R;
import com.ua.plamber_android.fragments.SupportFragment;

import butterknife.BindView;

public class SupportActivity extends SingleFragmentActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected Fragment createFragment() {
        return new SupportFragment();
    }

    @Override
    protected int setToolbarTitle() {
        return R.string.support_message;
    }

    public static Intent startSupportActivity(Context context) {
        return new Intent(context, SupportActivity.class);
    }
}
