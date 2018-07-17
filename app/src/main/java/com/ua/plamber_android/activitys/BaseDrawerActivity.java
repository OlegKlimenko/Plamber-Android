package com.ua.plamber_android.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ua.plamber_android.BuildConfig;
import com.ua.plamber_android.R;
import com.ua.plamber_android.api.PlamberAPI;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.database.utils.BookUtilsDB;
import com.ua.plamber_android.fragments.dialogs.ChangeAvatarDialog;
import com.ua.plamber_android.fragments.dialogs.UploadAvatarDialog;
import com.ua.plamber_android.interfaces.callbacks.ProfileCallback;
import com.ua.plamber_android.model.User;
import com.ua.plamber_android.utils.PreferenceUtils;
import com.ua.plamber_android.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseDrawerActivity extends AppCompatActivity {

    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    ImageView mProfileImage;
    LinearLayout mHeaderContainer;

    public static final String BACK_WITH_MENU = "BackWithMenu";
    public static final String START_WITH_MENU = "SartWithMenu";
    private static final String TAG = "BaseDrawerActivity";

    private PreferenceUtils preferenceUtils;
    private WorkAPI workAPI;
    private Utils utils;
    private BookUtilsDB bookUtilsDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceUtils = new PreferenceUtils(this);
        workAPI = new WorkAPI(this);
        utils = new Utils(this);
        bookUtilsDB = new BookUtilsDB(this);
    }


    @Override
    public void setContentView(int layoutResID) {
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        ButterKnife.bind(this);
        mProfileImage = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.header_profile_avatar);
        mHeaderContainer = (LinearLayout) mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_container);
        setHeaderBackground();
        initAccountDetailListener();
        saveProfileData();
    }

    private void initAccountDetailListener() {
        mHeaderContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAvatarDialog changeAvatarDialog = new ChangeAvatarDialog();
                changeAvatarDialog.setCancelable(false);
                changeAvatarDialog.show(getSupportFragmentManager(), ChangeAvatarDialog.TAG);
            }
        });
    }

    public void logoutApplication() {
        PreferenceUtils preferenceUtils = new PreferenceUtils(getApplicationContext());
        preferenceUtils.removePreference();
        utils.deleteAllPdfFiles();
        utils.deleteAllImageFiles();
        bookUtilsDB.deleteAllFromDB();
        finish();
        Intent intent = LoginActivity.startLoginActivity(getApplicationContext());
        startActivity(intent);
    }

    public void startSetting() {
        Intent intent = SettingActivity.startSettingActivity(getApplicationContext());
        startActivity(intent);
    }

    public void backToLibrary() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_local_books:
                        returnItemMenu(0);
                        break;
                    case R.id.nav_user_books:
                        returnItemMenu(1);
                        break;
                    case R.id.nav_library:
                        returnItemMenu(2);
                        break;
                    case R.id.nav_recommended:
                        returnItemMenu(3);
                        break;
                    case R.id.nav_upload_book:
                        returnItemMenu(4);
                        break;
                    case R.id.nav_setting:
                        returnItemMenu(1);
                        startSetting();
                        break;

                    case R.id.nav_logout:
                        logoutApplication();
                        break;
                }
                return true;
            }
        });
    }

    public void returnItemMenu(int number) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(BACK_WITH_MENU, number);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void setToggle(Toolbar toolbar) {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, getDrawerLayout(), toolbar, R.string.drawer_open, R.string.drawer_close);
        getDrawerLayout().addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public NavigationView getNavigationView() {
        return mNavigationView;
    }

    private void setHeaderBackground() {
        ImageView headerImage = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_image);
        Glide.with(getApplicationContext()).load(R.drawable.main_background).apply(new RequestOptions().transform(new CenterCrop())).into(headerImage);
    }

    public void setAvatar() {
        String url = BuildConfig.END_POINT;
        String currentUrl = url.substring(0, url.length() - 1) + preferenceUtils.readPreference(PreferenceUtils.USER_PHOTO);
        Glide.with(getApplicationContext()).load(currentUrl).into(mProfileImage);
    }


    private void setProfileName(String name) {
        TextView profileName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.header_profile_name);
        profileName.setText(name);
    }

    private void setEmail(String email) {
        TextView userEmail = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.header_profile_email);
        userEmail.setText(email);
    }

    private void setProfile(String name, String email) {
        setAvatar();
        setProfileName(name);
    }

    private void saveProfileData() {
        workAPI.getProfileData(new ProfileCallback() {
            @Override
            public void onSuccess(@NonNull User.ProfileData profileData) {
                preferenceUtils.writePreference(PreferenceUtils.USER_NAME, profileData.getUserName());
                preferenceUtils.writePreference(PreferenceUtils.USER_EMAIL, profileData.getUserEmail());
                preferenceUtils.writePreference(PreferenceUtils.USER_PHOTO, profileData.getUserPhotoUrl() + "?" + String.valueOf(System.currentTimeMillis()));
                setProfile(profileData.getUserName(), profileData.getUserEmail());
            }

            @Override
            public void onError(@NonNull Throwable t) {
                Log.i(TAG, t.getLocalizedMessage());
            }
        });

    }

    public SwitchCompat getOfflineSwitcher() {
        SwitchCompat offlineModeSwitch = (SwitchCompat) getNavigationView().getMenu().findItem(R.id.nav_mode_switch).getActionView().findViewById(R.id.menu_switch);
        if (!preferenceUtils.checkPreference(PreferenceUtils.OFFLINE_MODE)) {
            preferenceUtils.writeLogic(PreferenceUtils.OFFLINE_MODE, false);
        } else {
            offlineModeSwitch.setChecked(preferenceUtils.readLogic(PreferenceUtils.OFFLINE_MODE));
        }
        return offlineModeSwitch;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SettingActivity.REQUEST_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            Bundle args = new Bundle();
            args.putString(UploadAvatarDialog.UPLOAD_AVATAR, data.getStringExtra(FilePickActivity.FILE_PATH));
            UploadAvatarDialog uploadAvatar = new UploadAvatarDialog();
            uploadAvatar.setArguments(args);
            uploadAvatar.setCancelable(false);
            uploadAvatar.show(getSupportFragmentManager(), UploadAvatarDialog.TAG);
        }
    }
}


