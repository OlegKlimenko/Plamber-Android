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
import com.ua.plamber_android.utils.SingleFragmentActivity;
import com.ua.plamber_android.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

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
