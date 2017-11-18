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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ua.plamber_android.R;
import com.ua.plamber_android.utils.TokenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseDrawerActivity extends AppCompatActivity {

    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    public static final String BACK_WITH_MENU = "BackWithMenu";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        ButterKnife.bind(this);
        setImage();
        setAvatar();
    }

    public void logoutApplication() {
        TokenUtils tokenUtils = new TokenUtils(getApplicationContext());
        tokenUtils.removeToken();
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

    private void setImage() {
        ImageView headerImage = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_header_image);
        Glide.with(this).load(R.drawable.main_background).apply(new RequestOptions().transform(new CenterCrop())).into(headerImage);
    }

    private void setAvatar() {
        ImageView profileImage = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.header_profile_avatar);
        Glide.with(this).load(R.drawable.logo2).into(profileImage);
    }

}


