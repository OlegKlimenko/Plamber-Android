package com.ua.plamber_android.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.MainActivity;
import com.ua.plamber_android.activitys.LoginActivity;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.database.utils.CategoryDBUtils;
import com.ua.plamber_android.database.utils.LanguageDBUtils;
import com.ua.plamber_android.interfaces.callbacks.CategoryCallback;
import com.ua.plamber_android.interfaces.callbacks.StringListCallback;
import com.ua.plamber_android.model.Library;
import com.ua.plamber_android.utils.PreferenceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashFragment extends Fragment {
    @BindView(R.id.iv_spash_plamber_logo)
    ImageView mLogo;
    @BindView(R.id.progress_load_splash)
    ProgressBar mLoadSplash;

    private PreferenceUtils shared;
    private WorkAPI workAPI;
    private CategoryDBUtils categoryDBUtils;
    private LanguageDBUtils languageDBUtils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash_fragment, container, false);
        ButterKnife.bind(this, view);
        if (getActivity() != null)
            Glide.with(getActivity()).load(R.drawable.plamber_logo_mini).into(mLogo);
        shared = new PreferenceUtils(getActivity());
        workAPI = new WorkAPI(getActivity());
        categoryDBUtils = new CategoryDBUtils(getActivity());
        languageDBUtils = new LanguageDBUtils(getActivity());
        if (shared.checkPreference(PreferenceUtils.TOKEN) && !shared.readLogic(PreferenceUtils.OFFLINE_MODE)) {
            showProgress();
            workAPI.getAllCategory(new CategoryCallback() {
                @Override
                public void onSuccess(@NonNull List<Library.LibraryData> categories) {
                    categoryDBUtils.addCategoryToDB(categories);
                    getAllLanguage();
                }

                @Override
                public void onError(@NonNull Throwable t) {
                    hideProgress();
                    startApp();
                }
            });
            return view;
        }

        startActivity(LoginActivity.startLoginActivity(getActivity()));
        if (getActivity() != null)
            getActivity().finish();

        return view;
    }

    private void startApp() {
        startActivity(MainActivity.startLibraryActivity(getActivity()));
        if (getActivity() != null)
            getActivity().finish();
    }

    private void getAllLanguage() {
        workAPI.getAllLanguage(new StringListCallback() {
            @Override
            public void onSuccess(@NonNull List<String> stringsList) {
                languageDBUtils.addLanguageToDB(stringsList);
                hideProgress();
                if (getActivity() != null)
                    startApp();
            }

            @Override
            public void onError(@NonNull Throwable t) {
                hideProgress();
                startApp();
            }
        });
    }

    private void showProgress() {
        mLoadSplash.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mLoadSplash.setVisibility(View.GONE);
    }
}
