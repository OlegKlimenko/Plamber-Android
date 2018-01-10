package com.ua.plamber_android.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.ua.plamber_android.R;
import com.ua.plamber_android.fragments.SettingFragmant;
import com.ua.plamber_android.fragments.UploadAvatarDialog;
import com.ua.plamber_android.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {

    public static final int REQUEST_SELECT_IMAGE = 56;

    @BindView(R.id.toolbar)
    Toolbar mToolbarSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbarSetting);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle(R.string.setting_title);
        getSupportActionBar().setElevation(10);

        getSupportFragmentManager().beginTransaction().replace(R.id.setting_container, new SettingFragmant()).commit();
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

    public static Intent startSettingActivity(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            Bundle args = new Bundle();
            args.putString(UploadAvatarDialog.UPLOAD_AVATAR, data.getStringExtra(FilePickActivity.FILE_PATH));
            UploadAvatarDialog uploadAvatar = new UploadAvatarDialog();
            uploadAvatar.setArguments(args);
            uploadAvatar.setCancelable(false);
            uploadAvatar.show(getSupportFragmentManager(), UploadAvatarDialog.TAG);
        }
    }
}
