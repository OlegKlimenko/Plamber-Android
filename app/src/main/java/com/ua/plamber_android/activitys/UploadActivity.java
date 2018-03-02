package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ua.plamber_android.R;
import com.ua.plamber_android.fragments.DetailBookFragmentOffline;
import com.ua.plamber_android.fragments.UploadFileFragment;
import com.ua.plamber_android.fragments.UploadFileOfflineFragment;
import com.ua.plamber_android.utils.PreferenceUtils;

import butterknife.BindView;

public class UploadActivity extends SingleFragmentActivity {

    public static final int REQUEST_SELECT_FILE = 205;
    public static final int REQUEST_SELECT_CATEGORY = 105;
    public static final int REQUEST_SELECT_LANGUAGE = 123;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected Fragment createFragment() {
        PreferenceUtils preferenceUtils = new PreferenceUtils(this);
        String bookId = getIntent().getStringExtra(DetailBookFragmentOffline.BOOK_OFFLINE_KEY);
        boolean isOfflineBook = getIntent().getBooleanExtra(DetailBookFragmentOffline.IS_OFFLINE_BOOK, false);
        if (!preferenceUtils.readLogic(PreferenceUtils.OFFLINE_MODE)) {
            UploadFileFragment uploadFileFragment = new UploadFileFragment();
            Bundle args = new Bundle();
            args.putString(DetailBookFragmentOffline.BOOK_OFFLINE_KEY, bookId);
            args.putBoolean(DetailBookFragmentOffline.IS_OFFLINE_BOOK, isOfflineBook);
            uploadFileFragment.setArguments(args);
            return uploadFileFragment;
        }
        else
            return new UploadFileOfflineFragment();
    }

    @Override
    protected int setToolbarTitle() {
        return R.string.upload_book_activity;
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

    public static Intent startUploadActivity(Context context) {
        return new Intent(context, UploadActivity.class);
    }
}
