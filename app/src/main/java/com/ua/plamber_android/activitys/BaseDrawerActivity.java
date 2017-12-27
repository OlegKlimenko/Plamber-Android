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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ua.plamber_android.R;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.api.PlamberAPI;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.interfaces.callbacks.ProfileCallback;
import com.ua.plamber_android.model.User;
import com.ua.plamber_android.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseDrawerActivity extends AppCompatActivity {

    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    public static final String BACK_WITH_MENU = "BackWithMenu";
    public static final String START_WITH_MENU = "SartWithMenu";
    private static final String TAG = "BaseDrawerActivity";

    private PreferenceUtils preferenceUtils;
    private WorkAPI workAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceUtils = new PreferenceUtils(this);
        workAPI = new WorkAPI(this);
    }


    @Override
    public void setContentView(int layoutResID) {
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        ButterKnife.bind(this);
        setHeadrBackground();
        saveProfileData();
    }

    public void logoutApplication() {
        PreferenceUtils preferenceUtils = new PreferenceUtils(getApplicationContext());
        preferenceUtils.removePreference();
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
                    case R.id.nav_user_books:
                        returnItemMenu(0);
                        break;
                    case R.id.nav_library:
                        returnItemMenu(1);
                        break;
                    case R.id.nav_recommended:
                        returnItemMenu(2);
                        break;
                    case R.id.nav_upload_book:
                        returnItemMenu(3);
                        break;
                    case R.id.nav_setting:
                        returnItemMenu(0);
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

    private void setHeadrBackground() {
        ImageView headerImage = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_image);
        Glide.with(getApplicationContext()).load(R.drawable.main_background).apply(new RequestOptions().transform(new CenterCrop())).into(headerImage);
    }

    private void setAvatar(String urlAvatar) {
        String url = PlamberAPI.ENDPOINT;
        String currentUrl = url.substring(0, url.length() - 1) + urlAvatar;
        ImageView profileImage = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.header_profile_avatar);
        Glide.with(getApplicationContext()).load(currentUrl).into(profileImage);
    }

    private void setAvatar(int drawable) {
        ImageView profileImage = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.header_profile_avatar);
        profileImage.setColorFilter(getResources().getColor(R.color.colorAccent));
        Glide.with(getApplicationContext()).load(drawable).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into(profileImage);
    }

    private void setProfileName(String name) {
        TextView profileName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.header_profile_name);
        profileName.setText(name);
    }

    private void setEmail(String email) {
        TextView userEmail = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.header_profile_email);
        userEmail.setText(email);
    }

    private void setProfile(String name, String email, String photo) {
        if (!preferenceUtils.checkPreference(PreferenceUtils.USER_PHOTO)) {
            setAvatar(R.drawable.ic_account_circle_black_48dp);
        } else {
            setAvatar(photo);
        }
        setEmail(email);
    }

    private void setProfile(User.ProfileData profileData) {
        if (profileData.getUserPhotoUrl() == null) {
            setAvatar(R.drawable.ic_account_circle_black_48dp);
        } else {
            setAvatar(profileData.getUserPhotoUrl());
        }
        setEmail(profileData.getUserEmail());
    }

    private void saveProfileData() {
        if (preferenceUtils.checkPreference(PreferenceUtils.USER_NAME)) {
            String name = preferenceUtils.readPreference(PreferenceUtils.USER_NAME);
            String email = preferenceUtils.readPreference(PreferenceUtils.USER_EMAIL);
            String photo = preferenceUtils.readPreference(PreferenceUtils.USER_PHOTO);
            setProfile(name, email, photo);
        } else {
            workAPI.getProfileData(new ProfileCallback() {
                @Override
                public void onSuccess(@NonNull User.ProfileData profileData) {
                    preferenceUtils.writePreference(PreferenceUtils.USER_NAME, profileData.getUserName());
                    preferenceUtils.writePreference(PreferenceUtils.USER_EMAIL, profileData.getUserEmail());
                    preferenceUtils.writePreference(PreferenceUtils.USER_PHOTO, profileData.getUserPhotoUrl());
                    setProfile(profileData);
                }

                @Override
                public void onError(@NonNull Throwable t) {
                    Log.i(TAG, t.getLocalizedMessage());
                }
            });
        }
    }

    public SwitchCompat getOfflineSwitcher() {
        SwitchCompat offlineModeSwitch = (SwitchCompat) getNavigationView().getMenu().findItem(R.id.nav_mode_switch).getActionView().findViewById(R.id.offline_mode_switch);
        if (!preferenceUtils.checkPreference(PreferenceUtils.OFFLINE_MODE)) {
            preferenceUtils.writeOfflineMode(false);
        } else {
            offlineModeSwitch.setChecked(preferenceUtils.readStatusOffline());
        }
        return offlineModeSwitch;
    }
}


